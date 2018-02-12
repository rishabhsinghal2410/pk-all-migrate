package com.review.firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.internal.FirebaseAppStore;
import com.google.firestore.admin.v1beta1.FirestoreAdminProto;
import com.review.model.InvalidUser;
import com.review.model.Post;
import com.review.model.PostPic;
import com.review.model.User;
import com.review.repository.PostPicRepository;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class Admin {
    private final PostPicRepository postPicRepository;
    List<InvalidUser> invalidUsers = new ArrayList<>();

    public Admin(PostPicRepository repository) {
        this.postPicRepository = repository;
    }

    public void init() throws IOException {
        InputStream inputStream = new DefaultResourceLoader()
                .getResource("testproject-service-account.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl("https://testproject-fd4c5.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    public void deleteAllUsers() throws ExecutionException, InterruptedException {
        List<ApiFuture<Void>> apiFutures = new ArrayList<>();

        ListUsersPage listUsersPage = FirebaseAuth.getInstance().listUsersAsync(null).get();
        int count = 0;
        Iterable<ExportedUserRecord> values = listUsersPage.getValues();
        for (ExportedUserRecord user : values) {
            if (count == 5) {
                apiFutures.forEach(future -> {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
                count = 0;
                apiFutures.clear();
            }
            apiFutures.add(FirebaseAuth.getInstance().deleteUserAsync(user.getUid()));
            count++;
        }
        apiFutures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public String createUser(User user) {
        try {
            return FirebaseAuth.getInstance().createUserAsync(createUserRequest(user)).get().getUid();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void createUsers(List<User> users) throws ExecutionException, InterruptedException {
        Map<User, ApiFuture<UserRecord>> apiFutures = new HashMap<>();

        for (User user : users) {
            try {
                UserRecord.CreateRequest userRequest = createUserRequest(user);
                apiFutures.put(user, FirebaseAuth.getInstance().createUserAsync(userRequest));
            } catch (Exception e) {
                this.invalidUsers.add(new InvalidUser(user, e));
            }
        }

        apiFutures.keySet().forEach(user -> {
            ApiFuture<UserRecord> future = apiFutures.get(user);
            try {
                UserRecord userRecord = future.get();
                System.out.println("User Added SuccessFully ==> " + userRecord.getDisplayName() + " with uid " + userRecord.getUid());
            } catch (InterruptedException | ExecutionException e) {
                this.invalidUsers.add(new InvalidUser(user, e));
            }
        });

        this.invalidUsers.forEach(invalidUser -> {
            System.out.println("Invalid User " + invalidUser.user);
            handleInvalidUser(invalidUser);
//            invalidUser.e.printStackTrace();
        });
    }

    private void handleInvalidUser(InvalidUser invalidUser) {
        Throwable cause = invalidUser.e;
        do {
            if (cause.getMessage().contains("PHONE_NUMBER_EXISTS")) {
                invalidUser.user.setContactNo("");
                System.out.println("Retrying create user with no phone number " + invalidUser.user);
                createUser(invalidUser.user);
                break;
            } else if (cause.getMessage().contains("password must be at least 6 characters long")) {
                invalidUser.user.setPassword("RandomP@ssword!");
                System.out.println("Retrying create user with randomPassword " + invalidUser.user);
                createUser(invalidUser.user);
                break;
            }
            cause = cause.getCause();
        } while (cause != null);
    }

    public void addPosts(List<Post> posts) throws ExecutionException, InterruptedException {
        ListUsersPage users = FirebaseAuth.getInstance().listUsersAsync(null).get();
        Map<String, ExportedUserRecord> userMap = new HashMap<>();
        users.iterateAll().forEach(user -> userMap.put(user.getEmail(), user));


        posts.forEach(post -> {
            UserRecord author = userMap.get(post.getUserCreated());
            if(author != null) {
                Map<String, Object> postData = new HashMap<>();
                Map<String, Object> authorData = new HashMap<>();

                authorData.put("displayName", author.getDisplayName());
                authorData.put("uid", author.getUid());

                postData.put("title", post.getPostHeader());
                postData.put("content", post.getPostContent());
                postData.put("author", authorData);
                postData.put("likes", 0);
                postData.put("images", getImageData(post.getId()));

                System.out.println(postData);
            }
//            db.collection("posts").document();
        });

    }

    private Object getImageData(String postId) {
        return postPicRepository.findByReviewId(Integer.parseInt(postId)).stream().map(pic -> {
            Map<String, Object> imageData = new HashMap<>();
            String picName = pic.getPicUrl().split("/")[2];

            imageData.put("name", picName);

            if (pic.getCoverPic() == 1)
                imageData.put("cover", true);

            return imageData;
        }).collect(Collectors.toList());

    }


    private UserRecord.CreateRequest createUserRequest(User user) {
        if (user.isValid()) {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest();

            request.setDisplayName(user.getDisplayName());
            request.setEmail(user.getEmailId());
            request.setPassword(user.getPassword());
            if (user.getContactNo() != null) {
                request.setPhoneNumber(user.getContactNo());
            }
            request.setEmailVerified(true);
            request.setDisabled(false);
            return request;
        }

        throw new RuntimeException("Local Preconditions Failed");
    }

}

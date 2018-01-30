package com.review.firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.review.model.User;

import java.util.concurrent.ExecutionException;

public class Database {

    private Firestore db;

    /**
     * Initialize Firestore using default project ID.
     */
    public Database() {
        this("testproject-fd4c5");
        // [START fs_initialize]
        //Firestore db = FirestoreOptions.getDefaultInstance().getService();
        // [END fs_initialize]
    }

    public Database(String projectId) {
        // [START fs_initialize_project_id]
        FirestoreOptions firestoreOptions =
                FirestoreOptions.getDefaultInstance().toBuilder()
                        .setProjectId(projectId)
                        .build();
        Firestore db = firestoreOptions.getService();
        // [END fs_initialize_project_id]
        this.db = db;
    }

    public void addDocument(String collectionName, String documentName, Object document) throws ExecutionException, InterruptedException {
        if(!collectionName.isEmpty() && !documentName.isEmpty()) {
            ApiFuture<WriteResult> future = db.collection(collectionName).document(documentName).set(document);
            System.out.println(future.get().getUpdateTime());
        }
    }

    public User getUser(String userName) throws ExecutionException, InterruptedException {
        DocumentReference user = db.collection("users").document(userName);
        ApiFuture<DocumentSnapshot> future = user.get();
        DocumentSnapshot document = future.get();
        User dbUser = null;
        if (document.exists()) {
            // convert document to POJO
            dbUser = document.toObject(User.class);
            System.out.println(dbUser);
        } else {
            System.out.println("No such document!");
        }
        return dbUser;
    }
}

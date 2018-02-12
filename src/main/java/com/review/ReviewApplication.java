package com.review;

import com.review.firebase.Admin;
import com.review.model.Post;
import com.review.model.PostPic;
import com.review.model.User;
import com.review.repository.PostPicRepository;
import com.review.repository.PostRepository;
import com.review.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewApplication.class, args);
    }

    @Bean
    CommandLineRunner clr(UserRepository userRepository, Admin admin, PostRepository postRepository) throws IOException {

        return args -> {
            admin.init();
//            admin.deleteAllUsers();

//            List<User> users = new ArrayList<>();
//            userRepository.findAll().forEach(users::add);
//            admin.createUsers(users);

            List<Post> posts = new ArrayList<>();
            postRepository.findAll().forEach(posts::add);
            admin.addPosts(posts);
        };
    }
}

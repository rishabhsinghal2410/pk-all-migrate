package com.review.controller;

import com.review.firebase.Database;
import com.review.model.Post;
import com.review.model.User;
import com.review.repository.PostRepository;
import com.review.repository.UserRepository;
import com.review.util.PasswordSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutionException;

@RestController
public class ReviewController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    Database database = new Database();

    @GetMapping("/users")
    public Iterable<User> getAllUser(){
        Iterable<User> all = userRepository.findAll();
        all.forEach(user -> {
            try {
                user.setPassword(PasswordSecurity.createHash(user.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception occured while hashing the password");
            } catch (InvalidKeySpecException e) {
                System.out.println("Exception occured while hashing the password");
            }
            user.toString();});
        return all;
    }

    @GetMapping("/users/updateFirebase")
    public Iterable<User> updateFirebase(){
        Iterable<User> all = userRepository.findAll();
        all.forEach(user -> {
            try {
                user.setPassword(PasswordSecurity.createHash(user.getPassword()));
                database.addDocument("users", user.getEmailId(), user);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Exception occured while hashing the password");
            } catch (InvalidKeySpecException e) {
                System.out.println("Exception occured while hashing the password");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            user.toString();});
        return all;
    }

    @RequestMapping("/")
    public String returnHello(){
        return "Hello";
    }

    @RequestMapping("/posts")
    public Iterable<Post> getAllposts(){
        Iterable<Post> all = postRepository.findAll();
        all.forEach(post -> {
            post.toString();});
        return all;
    }

    @RequestMapping("/posts/updateFirebase")
    public Iterable<Post> updateAllposts(){
        Iterable<Post> all = postRepository.findAll();
        all.forEach(post -> {
            try {
                database.addDocument("posts", post.getId(), post);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            post.toString();});
        return all;
    }

    @ExceptionHandler(Exception.class)
    public String handleStorageFileNotFound(Exception exc, Model model) {
        model.addAttribute("message", "Exception Occurred while processing your request : " + exc.getMessage());
        return "error";
    }
}

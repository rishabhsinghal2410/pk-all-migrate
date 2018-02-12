package com.review.repository;

import com.review.model.PostPic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostPicRepository extends CrudRepository<PostPic, Integer> {

    List<PostPic> findByReviewId(int reviewId);
}

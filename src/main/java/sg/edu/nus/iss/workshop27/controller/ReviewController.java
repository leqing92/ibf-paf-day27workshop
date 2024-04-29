package sg.edu.nus.iss.workshop27.controller;

import java.util.Date;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import sg.edu.nus.iss.workshop27.model.EditedComment;
import sg.edu.nus.iss.workshop27.model.Review;
import sg.edu.nus.iss.workshop27.service.ReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class ReviewController {
    @Autowired
    ReviewService reviewService;
//workshop day 27 (a)
    @PostMapping(path = "/review", produces = "application/json")
    public ResponseEntity<String> postReview(@RequestBody MultiValueMap <String, String> payload) {
        Review review = new Review();
        review.setUsername(payload.getFirst("name"));
        review.setRating(Integer.parseInt(payload.getFirst("rating")));
        review.setComment(payload.getFirst("comment"));
        review.setGameId(Integer.parseInt(payload.getFirst("game id")));
        review.setEdited(new LinkedList<>());
        return reviewService.insertReview(review);
    }
//workshop day 27 (c)  
    @GetMapping(path = "/review/{id}", produces = "application/json")
    public ResponseEntity<String> getReviewById(@PathVariable ("id") String reviewId) {
        return reviewService.getReviewById(reviewId);
    }
//workshop day 27 (d)  
    @GetMapping(path = "/review/{id}/history", produces = "application/json")
    public ResponseEntity<String> getReviewByIdWithEdited(@PathVariable ("id") String reviewId) {
        return reviewService.getReviewByIdWithEdited(reviewId);
    }
//workshop day 27 (b)
//putmapping not working form but ok for application/json (cannot use multivalue map)
    @PutMapping(path = "/review/{id}", produces = "application/json")
    public ResponseEntity<String> updateReviewById(@PathVariable ("id") String reviewId, 
                                                @RequestBody EditedComment newComment) {
        // EditedComment newComment = new EditedComment();
        // System.out.println("\n\npayload:" + payload);
        // newComment.setComment(payload.getFirst("comment"));
        // newComment.setRating(Integer.parseInt(payload.getFirst("rating")));
        newComment.setPosted(new Date());        
        
        return reviewService.updateReviewById(reviewId, newComment);
    }

    // @PostMapping(path = "/review/{id}", produces = "application/json")
    // public ResponseEntity<String> updateReviewById(@PathVariable ("id") String reviewId, 
    //                                             @RequestBody MultiValueMap<String, String> payload) {
    //     EditedComment newComment = new EditedComment();
    //     // System.out.println("\n\npayload:" + payload);
    //     newComment.setComment(payload.getFirst("comment"));
    //     newComment.setRating(Integer.parseInt(payload.getFirst("rating")));
    //     newComment.setPosted(new Date());        
        
    //     return reviewService.updateReviewById(reviewId, newComment);
    // }
    
    @DeleteMapping(path = "/review/{id}", produces = "application/json")
    public ResponseEntity<String> updateReviewById(@PathVariable ("id") String reviewId){
        return reviewService.removeReview(reviewId);
    }
//putmapping not work for multivaluemapping , need either String or object.class
    @PutMapping (path = "/test")
    public ResponseEntity<String> testPut(@RequestBody String payload){
        System.out.println(payload);
        return ResponseEntity.ok().body("ok");
    }
}

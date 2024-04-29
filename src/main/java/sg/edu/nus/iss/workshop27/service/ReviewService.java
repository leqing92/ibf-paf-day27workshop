package sg.edu.nus.iss.workshop27.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop27.exception.NotFoundException;
import sg.edu.nus.iss.workshop27.model.EditedComment;
import sg.edu.nus.iss.workshop27.model.GameDetail;
import sg.edu.nus.iss.workshop27.model.Review;
import sg.edu.nus.iss.workshop27.repository.GameRepository;
import sg.edu.nus.iss.workshop27.repository.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    GameRepository gameRepo;
    
    public ResponseEntity<String> insertReview (Review review){
        String id = String.valueOf(review.getGameId());
        List <GameDetail> gameDetail = gameRepo.getGameDetailByGameId(id);
        if(gameDetail.isEmpty()){
            throw new NotFoundException("No game is available at ID = " + id);
        }
        else{
            review.setGameName(gameDetail.get(0).getName());
            review.setPosted(new Date());
            reviewRepo.insertReview(review);

            return ResponseEntity.status(201).body(review.toJson().toString());
        }
    }
    
    public ResponseEntity<String> getReviewById (String reviewId){
        Review review = reviewRepo.getReview(reviewId);
        if(null == review){
            throw new NotFoundException("No review is available at ID = " + reviewId);
        }
        else{
            return ResponseEntity.status(200).body(toJsonWithEditedBoolean(review).toString());
        }
    }

    public ResponseEntity<String> getReviewByIdWithEdited (String reviewId){
        Review review = reviewRepo.getReview(reviewId);
        if(null == review){
            throw new NotFoundException("No review is available at ID = " + reviewId);
        }
        else{
            return ResponseEntity.status(200).body(toJsonWithEdited(review).toString());
        }
    }

    public ResponseEntity<String> updateReviewById (String reviewId, EditedComment newComment){
        Review review = reviewRepo.getReview(reviewId);
        if(null == review){
            throw new NotFoundException("No review is available at ID = " + reviewId);
        }
        else{
            reviewRepo.updateReview(reviewId, newComment);
            Review updatedReview = reviewRepo.getReview(reviewId);

            return ResponseEntity.status(200).body(updatedReview.toJson().toString());
        }
    }

    public ResponseEntity<String> removeReview (String reviewId){
        if(reviewRepo.removeReview(reviewId))
        return ResponseEntity.status(200).body("Review ID " + reviewId + " is deleted");
        else
        throw new NotFoundException("No review is available at ID = " + reviewId);
    }

    public JsonObject toJsonWithEditedBoolean(Review review){
        return Json.createObjectBuilder()
                .add("user", review.getUsername())
                .add("rating", review.getRating())
                .add("comment", review.getComment())
                .add("ID", review.getGameId())
                .add("posted", review.getPosted().toString())
                .add("name", review.getGameName())
                .add("edited", review.getEdited().isEmpty() ? false : true)
                .add("timestamp", new Date().toString())                
                .build();
    }

    public JsonObject toJsonWithEdited(Review review){
        return Json.createObjectBuilder()
                .add("user", review.getUsername())
                .add("rating", review.getRating())
                .add("comment", review.getComment())
                .add("ID", review.getGameId())
                .add("posted", review.getPosted().toString())
                .add("name", review.getGameName())
                .add("edited", review.editedtoJson())
                .add("timestamp", new Date().toString())                
                .build();
    }
    
}

package sg.edu.nus.iss.workshop27.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import sg.edu.nus.iss.workshop27.model.EditedComment;
import sg.edu.nus.iss.workshop27.model.Review;
import sg.edu.nus.iss.workshop27.util.Constants;

@Repository
public class ReviewRepository {
    @Autowired
    MongoTemplate mongoTemplate;
//Create
    /*
    db.reviews.insert({
        user: "lq",
        rating: 1,
        comment: "testing",
        gid: 34,
        posted: new Date(),
        name : "Arkham Horror",
        edited: []
     })
     */
    public void insertReview (Review review){
        mongoTemplate.insert(review);
    }

//Read    
    /*
    db.reviews.find(
        {_id:ObjectId("6628f3be8d257b31e2eac55d")}
    )
     */
    public Review getReview (String reviewId){
        return mongoTemplate.findById(reviewId, Review.class);
    }
    /*
    db.reviews.find(
        {gid:34}
    )
     */
    public List<Review> getReviewByGameId (String gid){
        Query query = Query.query(Criteria.where("gid").is(Integer.parseInt(gid)));

        return mongoTemplate.find(query, Review.class);
    }

//Update
    /*
    db.reviews.update(
        {_id:ObjectId("6628ecf49bd86f50b069b822")},
        {
            $set:{            
                rating: 1,
                comment: "new comment",
                posted: new Date()
            }
        ,
        $push: {edited: {
            comment: "old comment",
            rating: 2,
            posted: new Date()
        }}    
        }
    )
    */
    public void updateReview (String reviewId, EditedComment newComment){
        Review review = getReview(reviewId);
        EditedComment previousComment = new EditedComment(review.getRating(), review.getComment(), review.getPosted());
        Query query = new Query(Criteria.where("reviewId").is(reviewId));
        if(newComment.getComment().equals("")){
            newComment.setComment(previousComment.getComment());
        }
        Update update = new Update()
                        .set("rating", newComment.getRating())
                        .set("comment", newComment.getComment())
                        .set("posted", newComment.getPosted())
                        .push("edited", previousComment);
        
        mongoTemplate.updateFirst(query, update, Review.class);

        // return result.getModifiedCount();
    }

//Delete
    /*
    db.reviews.remove({
        _id:ObjectId("6628ecf49bd86f50b069b822")
    })
     */
    public Boolean removeReview (String reviewId){
        Query query = Query.query(Criteria.where("_id").is(reviewId));
        DeleteResult result = mongoTemplate.remove(query, Constants.C_REVIEW);
        
        return result.getDeletedCount() > 0;
    }
}

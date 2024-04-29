package sg.edu.nus.iss.workshop27.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Document (collection = "reviews")
public class Review {
    @Id
    private String reviewId;
    @Field ("user")
    private String username;
    private int rating;
    private String comment;
    @Field("gid")
    private int gameId;
    @JsonFormat (pattern = "EEE MMM dd HH:mm:ss zzz yyyy", timezone = "Asia/Singapore")
    private Date posted;
    @Field ("name")
    private String gameName;
    private List<EditedComment> edited;

    public Review() {
    }      

    public Review(String reviewId, String username, int rating, String comment, int gameId, Date posted,
            String gameName, List<EditedComment> edited) {
        this.reviewId = reviewId;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.gameId = gameId;
        this.posted = posted;
        this.gameName = gameName;
        this.edited = edited;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public List<EditedComment> getEdited() {
        return edited;
    }

    public void setEdited(List<EditedComment> edited) {
        this.edited = edited;
    } 

    @Override
    public String toString() {
        return "Review [username=" + username + ", rating=" + rating + ", comment=" + comment + ", gameId=" + gameId
                + ", posted=" + posted + ", gameName=" + gameName + "]";
    }

    public JsonObject toJson(){
        return Json.createObjectBuilder()
                .add("reviewId", getReviewId())
                .add("user", getUsername())
                .add("rating", getRating())
                .add("comment", getComment())
                .add("ID", getGameId())
                .add("posted", getPosted().toString())
                .add("name", getGameName())
                .add("edited", editedtoJson())
                .build();
    }
// convert edited to Json
    public JsonArray editedtoJson(){
        
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();
        //doesnt work ????
        // if(null != this.getEdited()){
        //     List<JsonObjectBuilder> editedComments = this.getEdited()
        //                                                 .stream().map(t -> toJsonString())
        //                                                 .toList();
        // }
        if(null != this.getEdited()){
            List<EditedComment> editedComments = this.getEdited();
            for(EditedComment editedComment : editedComments){            
                jArrayBuilder.add(editedComment.toJsonString());
            }
            return jArrayBuilder.build();
        }

        return jArrayBuilder.build();
    }

       
    
}

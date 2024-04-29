package sg.edu.nus.iss.workshop27.model;

import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// @Document (collection = "editedComment")
public class EditedComment {
    
    private int rating;
    private String comment;
    private Date posted;

    public EditedComment() {
    }    
    public EditedComment(int rating, String comment, Date posted) {
        
        this.rating = rating;
        this.comment = comment;
        this.posted = posted;
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
    public Date getPosted() {
        return posted;
    }
    public void setPosted(Date posted) {
        this.posted = posted;
    }   

    public JsonObject toJsonString() {
        return Json.createObjectBuilder()                
                .add("comment", getComment())
                .add("rating", getRating())
                .add("posted", getPosted().toString())
                .build();
    }
}

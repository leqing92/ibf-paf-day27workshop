package sg.edu.nus.iss.workshop27.service;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop27.exception.NotFoundException;
import sg.edu.nus.iss.workshop27.model.Game;
import sg.edu.nus.iss.workshop27.model.GameDetail;
import sg.edu.nus.iss.workshop27.model.Review;
import sg.edu.nus.iss.workshop27.repository.GameRepository;
import sg.edu.nus.iss.workshop27.repository.ReviewRepository;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepo;

    @Autowired
    ReviewRepository reviewRepo;

    public ResponseEntity<String> getAllGame (int limit, int skip){
        List <Game> games = gameRepo.getAllGame(limit, skip);
        if(games.isEmpty()){
            throw new NotFoundException("No game is available at skip = " + skip );        
        }

        return ResponseEntity.ok().body(gameResponseString(games, limit, skip));
    }

    public ResponseEntity<String> getAllGameByRank (int limit, int skip){
        List <Game> games = gameRepo.getAllGameByRank(limit, skip);
        if(games.isEmpty()){
            throw new NotFoundException("No game is available at skip = " + skip );        
        }

        return ResponseEntity.ok().body(gameResponseString(games, limit, skip));
    }

    public ResponseEntity<String> getGameDetailById (String id){
        // List <GameDetail> gameDetail = gameRepo.getGameDetailById(id);
        // if(gameDetail.isEmpty())
        // throw new NotFoundException("No game is available at ID = " + id );

        // return ResponseEntity.ok().body(gameDetailResponseString(gameDetail.get(0)));
        
        GameDetail gameDetail = gameRepo.getGameDetailById(id);
        if(null == gameDetail){
            throw new NotFoundException("No game is available at ID = " + id );
        }

        return ResponseEntity.ok().body(gameDetailResponseString(gameDetail));
    } 

    public ResponseEntity<String> getGameDetailByGameId (String id){
        List <GameDetail> gameDetail = gameRepo.getGameDetailByGameId(id);
        if(gameDetail.isEmpty()){
            throw new NotFoundException("No game is available at ID = " + id );
        }

        return ResponseEntity.ok().body(gameDetailResponseString(gameDetail.get(0)));
    }

    public ResponseEntity<String> getReviewbyGameId (String id){
        List <GameDetail> gameDetails = gameRepo.getGameDetailByGameId(id);
        
        if(gameDetails.isEmpty()){
            throw new NotFoundException("No game is available at ID = " + id );
        }

        GameDetail gameDetail = gameDetails.get(0);
        List<Review> reviews = reviewRepo.getReviewByGameId(id);

        JsonArrayBuilder reviewArrayBuilder = Json.createArrayBuilder();
        for(Review review : reviews){            
            reviewArrayBuilder.add(review.toJson());
        }

        JsonObject jObject = Json.createObjectBuilder()
                                .add("_id", gameDetail.get_id())
                                .add("gid", gameDetail.getGame_id())
                                .add("name", gameDetail.getName())
                                .add("year", gameDetail.getYear())
                                .add("ranking", gameDetail.getRanking())
                                .add("average", gameRepo.getAverageRatingByGameId(gameDetail.getGame_id()))
                                .add("url", gameDetail.getUrl())
                                .add("thumbnail", gameDetail.getThumbnail())
                                .add("reviews", reviewArrayBuilder)
                                .add("timestamp", new Date().toString())
                                .build();

        return ResponseEntity.ok().body(jObject.toString());
    }

    public ResponseEntity<String> getGameDetailsBySortedRating (String sortOrder){
        List<Document> results = gameRepo.getGameDetailsBySortedRating(sortOrder);
        if(results.isEmpty()){
            throw new NotFoundException("No review is available");
        }

        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();
        for(Document doc : results){
            JsonObject jObj = Json.createObjectBuilder()
                                .add("_id", doc.getInteger("_id"))
                                .add("name", doc.getString("name"))
                                .add("rating", doc.getInteger("rating"))
                                .add("user", doc.getString("user"))
                                .add("comment", doc.getString("comment"))
                                .add("review_id", doc.getObjectId("review_id").toHexString())
                                .build();
            jArrayBuilder.add(jObj);
        }

        JsonObject reply = Json.createObjectBuilder()
                                .add("rating", sortOrder)
                                .add("games", jArrayBuilder.build())
                                .add("timestamp", new Date().toString())
                                .build();

        return ResponseEntity.ok().body(reply.toString());
    }

    public ResponseEntity<String> getCommentbyId (String id){
        return ResponseEntity.ok().body(gameRepo.getCommentbyId(id));
    }

//construct response entity json string
    private String gameResponseString (List<Game> games, int limit, int skip){
        JsonArrayBuilder gameArrayBuilder = Json.createArrayBuilder();
        
        for(Game game : games){
            JsonObject jObj = Json.createObjectBuilder()
                                        .add("_id", game.get_id())
                                        .add("game_id", String.valueOf(game.getGame_id()))
                                        .add("name", game.getName())
                                        .build();
            gameArrayBuilder.add(jObj);
        }

        JsonObject jObject = Json.createObjectBuilder()
                                .add("games", gameArrayBuilder.build())
                                .add("offset", skip)
                                .add("limit", limit)
                                .add("total", gameRepo.countAllGame())
                                .add("timestamp", new Date().toString())
                                .build();
                                
        return jObject.toString();
    }

    private String gameDetailResponseString (GameDetail gameDetail){
        JsonObject jObject = Json.createObjectBuilder()
                                .add("_id", gameDetail.get_id())
                                .add("gid", gameDetail.getGame_id())
                                .add("name", gameDetail.getName())
                                .add("year", gameDetail.getYear())
                                .add("ranking", gameDetail.getRanking())
                                .add("average", gameRepo.getAverageRatingByGameId(gameDetail.getGame_id()))
                                .add("url", gameDetail.getUrl())
                                .add("thumbnail", gameDetail.getThumbnail())
                                .add("timestamp", new Date().toString())
                                .build();

        return jObject.toString();
    }
    
}

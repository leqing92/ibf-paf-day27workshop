package sg.edu.nus.iss.workshop27.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import sg.edu.nus.iss.workshop27.model.Game;
import sg.edu.nus.iss.workshop27.model.GameDetail;
import sg.edu.nus.iss.workshop27.util.Constants;

@Repository
public class GameRepository {
    
    @Autowired
    MongoTemplate mongoTemplate;
//Create
    public void createGame(Game game){
        mongoTemplate.save(game);
    }
//Read
    /*
    db.games.find({},{_id:1, gid:1, name:1})
              .sort({gid:1})
               .limit(25).skip(5);
    */
    public List<Game> getAllGame(int limit, int skip){
        
        Query query = new Query()
                        .with(Sort.by(Sort.Direction.ASC, Constants.GAME_ID))
                        .limit(limit)
                        .skip(skip);
        
        return mongoTemplate.find(query, Game.class);
    }

    /*
    db.games.find({},{_id:1, gid:1, name:1})
              .sort({ranking:1})
              .limit(25).skip(5);
    */
    public List<Game> getAllGameByRank(int limit, int skip){
        
        Query query = new Query()
                        .with(Sort.by(Sort.Direction.ASC, Constants.RANKING))
                        .limit(limit)
                        .skip(skip);

        //by the following can projection already
        query.fields().include(Constants._ID, Constants.GAME_ID, Constants.NAME);
        // can uncomment following to verify
        // List<Document> results = mongoTemplate.find(query, Document.class, Constants.C_GAME);
        // for(Document doc : results){
        //     System.out.println();
        //     System.out.println(doc.toString());
        // }

        return mongoTemplate.find(query, Game.class);
    }

    /*
    db.games.find({}).count();
     */
    public long countAllGame(){
        Query query = new Query();

        return mongoTemplate.count(query, Constants.C_GAME);
    }

// find by _id
    /*
    db.games.find({
      _id:ObjectId("66279659fd07800205352645")
    })
    */
    public GameDetail getGameDetailById(String _id){

        return mongoTemplate.findById(_id, GameDetail.class);
    }

// find by gid
    /*
    db.games.find({
      gid:1
    });
    */
    public List<GameDetail> getGameDetailByGameId(String gid){       
        Query query = new Query(Criteria.where(Constants.GAME_ID).is(Integer.valueOf(gid)));
        
        return mongoTemplate.find(query, GameDetail.class);
    }
//from Collection "comments"
    /*
     db.comments.aggregate([
        {
            $group:{
                _id: "$gid",    
                average: {$avg: "$rating"}
//            ,count: {$sum: 1},
//            rating_sum: {$sum : "$rating"}
            }
        },
        {$match:{_id:2}}
    ]);
    */
    public double getAverageRatingByGameId(int gid){
        GroupOperation groupOperation = Aggregation.group(Constants.GAME_ID)
                                                .avg("rating").as("average");
        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(gid));

        Aggregation pipeline = Aggregation.newAggregation(groupOperation, matchOperation);
        AggregationResults<Document> result = mongoTemplate.aggregate(pipeline, "comments" , Document.class);
        
        return result.getMappedResults().get(0).getDouble("average");
    }

    public String getCommentbyId(String id){
        return mongoTemplate.findById(id, String.class, "comments");
    }
    /*
    db.games.aggregate([
        {
            $lookup:{
                from: "reviews",
                foreignField:"gid",
                localField:"gid",         
                as:"reviews"
            }        
        },
        {
            $unwind : "$reviews"
        },
        {
            $project:{
                _id:"$gid",      
                name:1,
                rating: "$reviews.rating",
                user: "$reviews.user",
                comment : "$reviews.comment",
                review_id: "$reviews._id"
            }        
        },
        {
            $sort :{rating :1}
        },
        {
            $limit:25
        },
    ])
     */
    public List<Document> getGameDetailsBySortedRating (String sortOrder){
        
        LookupOperation lookupOperation = Aggregation.lookup(Constants.C_REVIEW, Constants.GAME_ID,Constants.GAME_ID, "reviews");
        UnwindOperation unwindOperation = Aggregation.unwind("reviews");
        ProjectionOperation projectionOperation = Aggregation.project()
                                                        .andInclude("name", "reviews.rating", "reviews.user", "reviews.comment")
                                                        .and("gid").as("_id")
                                                        .and( "reviews._id").as("review_id");
        //by default set as desc
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "rating");
        if ("lowest".equals(sortOrder.toLowerCase())) {
            sortOperation = Aggregation.sort(Sort.Direction.ASC, "rating");
        }

        Aggregation pipeline = Aggregation.newAggregation(lookupOperation, unwindOperation, projectionOperation, sortOperation);

        AggregationResults<Document> result = mongoTemplate.aggregate(pipeline, Constants.C_GAME , Document.class);
        
        return result.getMappedResults();
    }
    
//Update
    public long updateGame(Game game){
        Query query = new Query(Criteria.where(Constants.GAME_ID).is(game.getGame_id()));
        Update update = new Update()
                            .set(Constants.NAME, game.getName());
        
        UpdateResult result = mongoTemplate.updateFirst(query, update, Game.class);

        return result.getModifiedCount();
    }

//Delete
    public long deleteGame (String id){
        Criteria criteria = Criteria.where(Constants.GAME_ID).is(id);
        Query query = Query.query(criteria);

        DeleteResult result = mongoTemplate.remove(query, Constants.C_GAME);

        return result.getDeletedCount();
    }


}

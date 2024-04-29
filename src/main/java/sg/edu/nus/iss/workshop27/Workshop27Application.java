package sg.edu.nus.iss.workshop27;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import org.springframework.expression.spel.ast.Projection;

import sg.edu.nus.iss.workshop27.model.Game;
import sg.edu.nus.iss.workshop27.repository.GameRepository;
import sg.edu.nus.iss.workshop27.util.Constants;

@SpringBootApplication
public class Workshop27Application implements CommandLineRunner {

	@Autowired
	GameRepository gameRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Workshop27Application.class, args);

		TimeZone timeZone = TimeZone.getTimeZone("Asia/Singapore");
		TimeZone.setDefault(timeZone);
		System.out.println("Date in Singapore Time Zone (SGT): " + new Date());
	}

	@Override
	public void run(String... args) throws Exception {

		// Document review = new Document();
		// review.append("user", "LQ")
		// 	.append("rating", 2)
		// 	.append("comment", "insert via document")
		// 	.append("gid", 2);
		// mongoTemplate.insert(review, "reviews");
//test		
		// LookupOperation lookupOperation = Aggregation.lookup(Constants.C_REVIEW, Constants.GAME_ID,Constants.GAME_ID, "reviews");
        // UnwindOperation unwindOperation = Aggregation.unwind("reviews");
        // ProjectionOperation projectionOperation = Aggregation.project()
        //                                                 .andInclude("name", "reviews.rating", "reviews.user", "reviews.comment", "reviews._id")
        //                                                 .and("gid").as("_id");
        // SortOperation sortOperation = Aggregation.sort(Direction.DESC, "rating");
        // Aggregation pipeline = Aggregation.newAggregation(lookupOperation, unwindOperation, projectionOperation, sortOperation);

        // AggregationResults<Document> result = mongoTemplate.aggregate(pipeline, Constants.C_GAME , Document.class);
		// List<Document> docs = mongoTemplate.aggregate(pipeline, "games", Document.class).getMappedResults();
		// for(Document doc : docs){
		// 	System.out.println(doc + "\n\n");
		// }

//revision day 29 by me hvnt complete
		/*
		db.games.aggregate([
			{
			$match:{gid:1}
			},  
			{
				$lookup:{
					from: "comments",
					foreignField:"gid",
					localField:"gid",
					as:"comments"
				}        
			},
			{
				$project:{
					gid:1,      
					name:1,
					year:1,
					ranking:1,
					users_rated:1,
					url:1,
					image:1,
					comments:{               
						$slice: ["$comments", 5] 
					}
				},
				
			},
			{
				$unwind: "$comments" // If you want to unwind the comments array
			},
			{
				$sort: { "comments.rating": -1 } // Sort comments by rating in descending order
			},
			{
				$group:{
					_id:"$gid",
					name: { $first: "$name" },
					year: { $first: "$year" },
					ranking: { $first: "$ranking" },
					users_rated: { $first: "$users_rated" },
					url: { $first: "$url" },
					image: { $first: "$image" },
					comments: { $push: "$comments" }
				}
			},
			{
				$limit:5
			}
		])
		*/
		// MatchOperation matchOperation = Aggregation.match(Criteria.where("gid").is(1));
		// LookupOperation lookupOperation = Aggregation.lookup("comments", "gid", "gid", "comments");
		// ProjectionOperation projectionOperation = Aggregation.project(null);

//revision day 29 by chuk (completed)
		/*
		db.games.aggregate([
			{
			$match:
				{name:{$regex:"ticket", $options: "i"}}
			},
			{
			$sort: {"ranking": -1}  
			},  
			{
				$lookup:{
					from: "comments",
					foreignField:"gid",
					localField:"gid",
					pipeline:[
						{$sort:{"rating": -1}},
						{$limit:5}
					],            
					as:"comments"
				}        
			},
			{
				$project:{
					_id:"$gid",      
					name:1,
					year:1,
					ranking:1,
					users_rated:1,
					url:1,
					image:1,
					comments:1
				}        
			}
		]);
		 */
		// MatchOperation matchOperation2 = Aggregation.match(Criteria.where("name").regex("ticket", "i"));
		// SortOperation sortOperation2 = Aggregation.sort(Sort.by(Sort.Direction.DESC,"ranking"));
		// ProjectionOperation projectionOperation2 = Aggregation.project()
		// 												.andInclude("gid", "name", "year", "ranking", "user_rated", "url", "image", "comments")
		// 												.and("gid").as("_id");		
		// LookupOperation lookupOperation2 = Aggregation.lookup()
		// 										.from("comments")
		// 										.localField("gid")	
		// 										.foreignField("gid")
		// 										.pipeline(
		// 											Aggregation.sort(Sort.by(Direction.DESC, "rating")),
		// 											Aggregation.limit(5)
		// 										)
		// 										.as("comments");
		// Aggregation pipeline = Aggregation.newAggregation(matchOperation2, sortOperation2, projectionOperation2, lookupOperation2);
		// List<Document> docs = mongoTemplate.aggregate(pipeline, "games", Document.class).getMappedResults();
		// for(Document doc : docs){
		// 	System.out.println(doc + "\n\n");
		// }
		
		
	}

}

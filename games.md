## command in studio e# for games 
use boardgames;

db.games.find({});

db.games.find({},{_id:1, gid:1, name:1, ranking:1})
        .sort({ranking:1})
        .limit(25).skip(50);

db.games.find({}).count();

db.games.find({
       _id:ObjectId("66279659fd07800205354ec5")
      });
      
db.games.find({
       gid:1
      });
      
db.games.aggregate([
    {
        $match: {gid:34}
    },
    {
    $lookup: {
        from:"comments",
        foreignField:"gid",
        localField:"gid",
        as:"reviews"
        },
    }
]);

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
        $unwind: "$comments" 
    },
    {
        $sort: { "comments.rating": -1 } 
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
]);

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

## for reviews

db.getCollection("reviews").find({});

db.reviews.find(
    {_id:ObjectId("66290c2245f59a066541315b")}
)


db.reviews.find({
    _id:ObjectId("6628ecf49bd86f50b069b822")
})

db.reviews.insert({
        user: "lq",
        rating: 1,
        comment: "testing",
        gid: 34,
        posted: new Date(),
        name : "Arkham Horror",
        edited: []
})
     
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

## for comments
db.getCollection("comments").find({});

db.comments.aggregate([
{$match:{gid:2}},
    {
        $group:{
            _id: "$gid",
//            count: {$sum: 1},
//            rating_sum: {$sum : "$rating"},
            average_rating: {$avg: "$rating"}
        }
    }
    
]);

db.comments.aggregate([
    {
        $group:{
            _id:"$gid",
//            count:{$sum: 1}
            }
    },
]).toArray().length;

db.comments.distinct("gid").length;

db.comments.createIndex(
    {c_text: "text"}
)

db.comments.find(
{
    $text:{
        $search : "love hate"
    }
},
{
    gid: 1,
    score :{
        $meta : "textScore"
    }
},

)
.sort({score: {$meta: "textScore"}})
//.projection({score: {$meta : "textScore"}, gid : 1});

db.comments.aggregate([
    {
        $match: {
            $text: {
                $search:"love hate"
            }
        }
    },
    {
        $addFields:{
            score: {$meta : "textScore"}
        }
    },
    {
        $sort: {score: -1}
    },
    {
        $limit: 5
    }
]);

db.comments.aggregate([
    {
        $match: {
            $text: {
                $search:"love hate"
            }
        }
    },
    {
        $project :{
            score: {$meta : "textScore"}
        }
    },
    {
        $sort: {score: {$meta : "textScore"}}
    }
]);
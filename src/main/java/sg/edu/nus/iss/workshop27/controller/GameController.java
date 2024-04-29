package sg.edu.nus.iss.workshop27.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.workshop27.service.GameService;

@RestController
public class GameController {
    
    @Autowired
    GameService gameSvc;
//workshop day 26 (a)
    @GetMapping (path = "/games", produces = "application/json") 
    public ResponseEntity<String> getAllGame(@RequestParam (value = "limit", defaultValue = "25")  int limit, 
                                            @RequestParam (value ="skip", defaultValue = "0") int skip){ 
        return gameSvc.getAllGame(limit, skip);
    } 
//workshop day 27 (b)
    @GetMapping (path = "/games/rank", produces = "application/json")
    public ResponseEntity<String> getAllGameByRank(@RequestParam (value = "limit", defaultValue = "25")  int limit, 
                                            @RequestParam (value ="skip", defaultValue = "0") int skip){ 
        return gameSvc.getAllGameByRank(limit, skip);
    } 
//workshop day 27 (c)
    @GetMapping(path = "/games/{id}", produces = "application/json") 
    public ResponseEntity<String> getGameDetailByGameId(@PathVariable ("id") String id) {
        return gameSvc.getGameDetailByGameId(id);
    }
    
    @GetMapping(path = "/comments/{id}")
    public ResponseEntity<String> getComments(@PathVariable ("id") String id) {
        return gameSvc.getCommentbyId(id);
    }
    
//workshop 28 (a)
    @GetMapping(path = "/games/{id}/reviews", produces = "application/json")
    public ResponseEntity<String> getReviewsbyGameId(@PathVariable ("id") String gid) {
        return gameSvc.getReviewbyGameId(gid);
    }

//workshop 28 (b)
    @GetMapping(path = "/games/highest", produces = "application/json")
    public ResponseEntity<String> sortByHighest() {

        return gameSvc.getGameDetailsBySortedRating("highest");
    }

    @GetMapping(path = "/games/lowest", produces = "application/json")
    public ResponseEntity<String> sortByLowest() {

        return gameSvc.getGameDetailsBySortedRating("lowest");
    }
    
}

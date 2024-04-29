package sg.edu.nus.iss.workshop27.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document (collection = "games")
public class Game {
    @Id
    private String _id;
    //in mongo it name as gid so need field to match
    @Field("gid")
    private int game_id;
    
    private String name;
    
    public Game() {
    }
    public Game(String _id, int game_id, String name) {
        this._id = _id;
        this.game_id = game_id;
        this.name = name;
    }

    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public int getGame_id() {
        return game_id;
    }
    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "{_id=" + _id + ", game_id=" + game_id + ", name=" + name + "}";
    }

    

}

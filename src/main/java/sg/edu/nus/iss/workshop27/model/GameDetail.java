package sg.edu.nus.iss.workshop27.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document (collection = "games")
public class GameDetail {
    private String _id;
    //in mongo it name as gid so need field to match
    @Field("gid")
    private int game_id;
    private String name;
    private int year;
    private int ranking;
    private int usersRated;
    private String url;
    //in mongo it name as iamge so need field to match
    @Field("image")
    private String thumbnail;

    public GameDetail() {
    }    

    public GameDetail(String _id, int game_id, String name, int year, int ranking, int usersRated, String url,
            String thumbnail) {
        this._id = _id;
        this.game_id = game_id;
        this.name = name;
        this.year = year;
        this.ranking = ranking;
        this.usersRated = usersRated;
        this.url = url;
        this.thumbnail = thumbnail;
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
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getRanking() {
        return ranking;
    }
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
    public int getUsersRated() {
        return usersRated;
    }
    public void setUsersRated(int usersRated) {
        this.usersRated = usersRated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    

    
}

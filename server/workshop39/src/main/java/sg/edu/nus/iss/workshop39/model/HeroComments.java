package sg.edu.nus.iss.workshop39.model;

import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class HeroComments {
    private int id;
    private List<String> comments;

    public HeroComments() {
    }

    public HeroComments(int id, List<String> comments) {
        this.id = id;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public JsonObject toJsonObject() {
        JsonArrayBuilder jsArray = Json.createArrayBuilder();

        for (String comment : comments) {
            jsArray.add(comment);
        }

        return Json.createObjectBuilder()
                .add("id", id)
                .add("comments", jsArray)
                .build();
    }

    public static HeroComments convertFromDocument(Document d) {
        HeroComments hc = new HeroComments();
        hc.setId(d.getInteger("id"));
        hc.setComments(d.getList("comments", String.class));
        return hc;
    }

}

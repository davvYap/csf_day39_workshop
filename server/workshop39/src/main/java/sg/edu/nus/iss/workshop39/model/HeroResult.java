package sg.edu.nus.iss.workshop39.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

public class HeroResult {
    private long id;
    private String name;
    private String description;
    private Thumbnail thumbnail;
    private String resourceURI;
    private Comics comics;

    public long getID() {
        return id;
    }

    public void setID(long value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail value) {
        this.thumbnail = value;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String value) {
        this.resourceURI = value;
    }

    public Comics getComics() {
        return comics;
    }

    public void setComics(Comics value) {
        this.comics = value;
    }

    public static HeroResult convertFromJson(JsonObject json) {
        HeroResult rs = new HeroResult();
        rs.setID(json.getJsonNumber("id").longValue());
        rs.setName(json.getString("name"));
        rs.setDescription(json.getString("description"));

        JsonObject thumbnailObj = json.getJsonObject("thumbnail");
        Thumbnail thumbnail = Thumbnail.convertFromJson(thumbnailObj);
        rs.setThumbnail(thumbnail);

        rs.setResourceURI(json.getString("resourceURI"));

        JsonObject comicsObj = json.getJsonObject("comics");
        Comics comics = Comics.convertFromJson(comicsObj);
        rs.setComics(comics);

        return rs;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("thumbnail", thumbnail.toJsonObjectBuilder())
                .add("resourceURI", resourceURI)
                .add("comics", comics.toJsonObjectBuilder());
    }

    public static HeroResult convertFromJsonString(String json) throws IOException {
        HeroResult hero = new HeroResult();
        if (json != null) {
            try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
                JsonReader reader = Json.createReader(is);
                JsonObject js = reader.readObject();
                hero.setID(js.getInt("id"));
                hero.setName(js.getString("name"));
                hero.setDescription(js.getString("description"));

                JsonObject thumbnailObj = js.getJsonObject("thumbnail");
                Thumbnail thumbnail = Thumbnail.convertFromJson(thumbnailObj);
                hero.setThumbnail(thumbnail);

                hero.setResourceURI(js.getString("resourceURI"));

                JsonObject comicsObj = js.getJsonObject("comics");
                Comics comics = Comics.convertFromJson(comicsObj);
                hero.setComics(comics);
            }
        }
        return hero;
    }

}

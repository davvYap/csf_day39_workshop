package sg.edu.nus.iss.workshop39.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Thumbnail {
    private String path;
    private String extension;

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String value) {
        this.extension = value;
    }

    public static Thumbnail convertFromJson(JsonObject json) {
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setPath(json.getString("path"));
        thumbnail.setExtension(json.getString("extension"));
        return thumbnail;

    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("extension", extension)
                .add("path", path);
    }
}

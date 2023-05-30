package sg.edu.nus.iss.workshop39.model;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class ComicsItems {
    private String resourceURI;
    private String name;

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ComicsItems convertFromJson(JsonObject json) {
        ComicsItems comicsItems = new ComicsItems();
        comicsItems.setResourceURI(json.getString("resourceURI"));
        comicsItems.setName(json.getString("name"));
        return comicsItems;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("resourceURI", resourceURI)
                .add("name", name);
    }

}

package sg.edu.nus.iss.workshop39.model;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import java.util.ArrayList;
import java.util.List;

public class Comics {
    private long available;
    private String collectionURI;
    private List<ComicsItems> items = new ArrayList<>();

    public long getAvailable() {
        return available;
    }

    public void setAvailable(long value) {
        this.available = value;
    }

    public String getCollectionURI() {
        return collectionURI;
    }

    public void setCollectionURI(String value) {
        this.collectionURI = value;
    }

    public List<ComicsItems> getItems() {
        return items;
    }

    public void setItems(List<ComicsItems> items) {
        this.items = items;
    }

    public static Comics convertFromJson(JsonObject json) {
        Comics comics = new Comics();
        comics.setAvailable(json.getJsonNumber("available").longValue());
        comics.setCollectionURI(json.getString("collectionURI"));

        List<ComicsItems> comicsItems = json.getJsonArray("items").stream()
                .map(js -> (JsonObject) js)
                .map(ComicsItems::convertFromJson)
                .toList();
        comics.setItems(comicsItems);
        return comics;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {

        JsonArrayBuilder jsArr = Json.createArrayBuilder();

        for (ComicsItems comicsItems : items) {
            jsArr.add(comicsItems.toJsonObjectBuilder());
        }

        return Json.createObjectBuilder()
                .add("collectionURI", collectionURI)
                .add("available", available)
                .add("items", jsArr);
    }
}

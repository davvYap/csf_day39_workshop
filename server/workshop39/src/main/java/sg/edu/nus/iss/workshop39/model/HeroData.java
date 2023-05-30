package sg.edu.nus.iss.workshop39.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class HeroData {
    private long offset;
    private long limit;
    private long total;
    private long count;
    private List<HeroResult> heroResults = new ArrayList<>();

    public long getOffset() {
        return offset;
    }

    public void setOffset(long value) {
        this.offset = value;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long value) {
        this.limit = value;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long value) {
        this.total = value;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long value) {
        this.count = value;
    }

    public List<HeroResult> getHeroResults() {
        return heroResults;
    }

    public void setHeroResults(List<HeroResult> heroResults) {
        this.heroResults = heroResults;
    }

    public static HeroData createFromJson(JsonObject js) {
        HeroData heroData = new HeroData();
        heroData.setLimit(js.getInt("limit"));
        heroData.setOffset(js.getInt("offset"));
        heroData.setTotal(js.getInt("total"));
        heroData.setCount(js.getInt("count"));

        List<HeroResult> heroResults = js.getJsonArray("results").stream()
                .map(rs -> (JsonObject) rs)
                .map(HeroResult::convertFromJson)
                .toList();
        heroData.setHeroResults(heroResults);

        return heroData;
    }

    public JsonObjectBuilder toJsonObjectBuilder() {

        JsonArrayBuilder jsArr = Json.createArrayBuilder();
        for (HeroResult heroResult : heroResults) {
            jsArr.add(heroResult.toJsonObjectBuilder());
        }

        return Json.createObjectBuilder()
                .add("offset", offset)
                .add("limit", limit)
                .add("total", total)
                .add("count", count)
                .add("results", jsArr);
    }

}

package sg.edu.nus.iss.workshop39.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Response {
    private long code;
    private String status;
    private String copyright;
    private HeroData data;

    public long getCode() {
        return code;
    }

    public void setCode(long value) {
        this.code = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String value) {
        this.copyright = value;
    }

    public HeroData getData() {
        return data;
    }

    public void setData(HeroData data) {
        this.data = data;
    }

    public static Response convertFromJson(String json) throws IOException {
        Response response = new Response();
        if (json != null) {
            try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
                JsonReader reader = Json.createReader(is);
                JsonObject js = reader.readObject();
                response.setCode(js.getJsonNumber("code").longValue());
                response.setStatus(js.getString("status"));
                response.setCopyright(js.getString("copyright"));

                JsonObject data = js.getJsonObject("data");
                HeroData heroData = HeroData.createFromJson(data);
                response.setData(heroData);
            }
        }
        return response;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("code", code)
                .add("data", data.toJsonObjectBuilder())
                .build();
    }

}

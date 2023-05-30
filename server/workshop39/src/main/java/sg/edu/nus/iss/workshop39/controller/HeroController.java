package sg.edu.nus.iss.workshop39.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import sg.edu.nus.iss.workshop39.model.HeroComments;
import sg.edu.nus.iss.workshop39.model.HeroResult;
import sg.edu.nus.iss.workshop39.model.Response;
import sg.edu.nus.iss.workshop39.service.HeroService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class HeroController {

    @Autowired
    private HeroService heroSvc;

    @GetMapping(path = "/characters")
    public ResponseEntity<String> getHeroes(@RequestParam String heroName, @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) throws IOException {
        Optional<Response> responseOpt = heroSvc.getHeroes(limit, offset, heroName);
        if (responseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"No heroes found\"}");
        }

        Response response = responseOpt.get();
        List<HeroResult> heroes = response.getData().getHeroResults();
        heroSvc.saveHeroes(heroes);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toJson().toString());

    }

    @GetMapping(path = "/characters/{id}")
    public ResponseEntity<String> getHero(@PathVariable int id) throws IOException {

        // GET FROM REDIS
        HeroResult hero = heroSvc.getHeroFromRedis(id);

        if (hero == null) {
            // GET FROM MARVEL API
            Optional<Response> responseOpt = heroSvc.getHeroById(id);

            if (responseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Json.createObjectBuilder()
                                .add("message", "No hero with ID: %d found".formatted(id))
                                .build().toString());
            }

            // SAVE TO REDIS
            List<HeroResult> newHero = new ArrayList<>();
            newHero.add(responseOpt.get().getData().getHeroResults().get(0));
            heroSvc.saveHeroes(newHero);

            HeroResult heroAPI = responseOpt.get().getData().getHeroResults().get(0);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(heroAPI.toJsonObjectBuilder().build().toString());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(hero.toJsonObjectBuilder().build().toString());

    }

    @PostMapping(path = "/character/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> postComments(@PathVariable int id, @RequestBody MultiValueMap<String, String> form) {
        String comments = form.getFirst("comments");
        long upsertCount = heroSvc.postComments(id, comments);

        if (upsertCount > 0) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Posted", "Successfully - %d".formatted(id))
                            .build().toString());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Json.createObjectBuilder()
                        .add("Posted", "Failed - %d".formatted(id))
                        .build().toString());
    }

    @GetMapping(path = "/character/{id}/comments")
    public ResponseEntity<String> getComments(@PathVariable int id) {
        HeroComments hc = heroSvc.getComments(id);
        if (hc == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("Failed", "%d has no comments".formatted(id))
                            .build().toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(hc.toJsonObject().toString());
    }

}

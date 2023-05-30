package sg.edu.nus.iss.workshop39.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop39.model.HeroComments;
import sg.edu.nus.iss.workshop39.model.HeroResult;
import sg.edu.nus.iss.workshop39.model.Response;
import sg.edu.nus.iss.workshop39.repository.HeroRepository;

@Service
public class HeroService {

    @Autowired
    private HeroRepository heroRepo;

    @Value("${marvel_private_key}")
    private String marvelPrivateKey;

    @Value("${marvel_public_key}")
    private String marvelPublicKey;

    private static final String MARVEL_API_URL = "http://gateway.marvel.com/v1/public/characters";

    public String convertToMD5Hash() {
        long timestamp = System.currentTimeMillis();
        String toHash = timestamp + marvelPrivateKey + marvelPublicKey;
        // System.out.println("before hash >>> " + toHash);
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(toHash.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    // GET FROM MARVEL API
    public Optional<Response> getHeroes(int limit, int offset, String heroName) throws IOException {
        long timestamp = System.currentTimeMillis();
        String hash = convertToMD5Hash();

        String url = UriComponentsBuilder.fromUriString(MARVEL_API_URL)
                .queryParam("nameStartsWith", heroName)
                .queryParam("orderBy", "name")
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("ts", timestamp)
                .queryParam("apikey", marvelPublicKey)
                .queryParam("hash", hash)
                .toUriString();

        RequestEntity req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;

        try {
            resp = template.exchange(req, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response response = null;

        if (resp != null) {
            response = Response.convertFromJson(resp.getBody());
        }
        return Optional.ofNullable(response);

    }

    // GET MARVEL HERO BY ID - PATH VARIABLE - MARVEL API
    public Optional<Response> getHeroById(int id) throws IOException {
        long timestamp = System.currentTimeMillis();
        String hash = convertToMD5Hash();
        String newUrl = MARVEL_API_URL.concat("/").concat(String.valueOf(id));
        System.out.println("Path variable URL >>> " + newUrl);

        String url = UriComponentsBuilder.fromUriString(newUrl)
                .queryParam("ts", timestamp)
                .queryParam("apikey", marvelPublicKey)
                .queryParam("hash", hash)
                .toUriString();

        RequestEntity req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;

        try {
            resp = template.exchange(req, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response response = null;

        if (resp != null) {
            response = Response.convertFromJson(resp.getBody());
        }
        return Optional.ofNullable(response);
    }

    // SAVE LIST OF HEROES INTO REDIS
    public void saveHeroes(List<HeroResult> heroes) {

        heroRepo.saveHeroes(heroes);
    }

    // GET HERO FROM REDIS
    public HeroResult getHeroFromRedis(Integer id) throws IOException {
        return heroRepo.getHeroFromRedis(id);
    }

    // POST HERO COMMENTS TO MONGO
    public long postComments(int id, String comm) {
        return heroRepo.postComments(id, comm);
    }

    // GET COMMENT FROM MONGO
    public HeroComments getComments(int id) {
        return heroRepo.getComments(id);
    }
}

package sg.edu.nus.iss.workshop39.repository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import sg.edu.nus.iss.workshop39.model.HeroComments;
import sg.edu.nus.iss.workshop39.model.HeroResult;

@Repository
public class HeroRepository {
    @Autowired
    private RedisTemplate<String, String> redis;

    @Autowired
    private MongoTemplate mongo;

    // SAVE HEROES INTO REDIS
    public void saveHeroes(List<HeroResult> heroes) {

        long timeoutInSeconds = 600; // 10 mins

        for (HeroResult hero : heroes) {
            String key = "hero/".concat(String.valueOf(hero.getID()));

            redis.opsForValue().set(key, hero.toJsonObjectBuilder().build().toString(),
                    timeoutInSeconds, TimeUnit.SECONDS);
        }
    }

    // GET HERO FROM REDIS
    public HeroResult getHeroFromRedis(Integer id) throws IOException {
        String key = "hero/".concat(String.valueOf(id));

        String heroJson = redis.opsForValue().get(key);

        if (heroJson != null) {
            return HeroResult.convertFromJsonString(heroJson);
        }

        return null;
    }

    // POST COMMENTS TO MONGO
    public long postComments(int id, String comments) {

        Query query = Query.query(Criteria.where("id").is(id));

        // Document heroComment =
        // Document.parse(heroComments.toJsonObject().toString());

        Update updateOps = new Update()
                .set("id", id)
                .push("comments", comments);

        UpdateResult updateResult = mongo.upsert(query, updateOps, "heroes");

        return updateResult.getModifiedCount();
    }

    // GET COMMENTS FROM MONGO
    public HeroComments getComments(int id) {
        Query query = Query.query(Criteria.where("id").is(id));

        Document d = mongo.findOne(query, Document.class, "heroes");

        if (d == null) {
            return null;
        }

        return HeroComments.convertFromDocument(d);

    }
}

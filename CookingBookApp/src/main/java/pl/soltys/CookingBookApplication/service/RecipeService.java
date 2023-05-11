package pl.soltys.CookingBookApplication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Streams;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.soltys.CookingBookApplication.model.Recipe;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class RecipeService {
    private final String API_URL = "https://tasty.p.rapidapi.com";
    private final String API_KEY = "40faa789d4mshbc3b0eb07e98efap1429f8jsn889b682016c1";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public List<Recipe> getRecipesFromApi(String phrase) {
        phrase = phrase.replaceAll(" ", "+");

        var URL = API_URL + "/recipes/list?from=0&size=20&q=" + phrase;
        var response = Unirest.get(URL)
                .header("X-RapidAPI-Key", "40faa789d4mshbc3b0eb07e98efap1429f8jsn889b682016c1")
                .header("X-RapidAPI-Host", "tasty.p.rapidapi.com")
                .asJson()
                .getBody();

        if (response.getObject().getInt("count") == 0) {
            log.warn("No recipes found!");
            return Collections.emptyList();
        }

        var results = new ArrayList<Recipe>();
        response.getObject().getJSONArray("results").forEach(json -> results.add(parseJson((JSONObject)json)));

        return results;
    }

    private Recipe parseJson(JSONObject recipe){
        return Recipe.builder()
                .Name(recipe.getString("name"))
                .Poster(recipe.getString("thumbnail_url"))
                .API_ID(recipe.getInt("id"))
                .build();
    }
}

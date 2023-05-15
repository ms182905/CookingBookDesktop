package pl.soltys.CookingBookApplication.service;

import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import pl.soltys.CookingBookApplication.model.RecipeDetails;

import java.util.*;

@Slf4j
@Service
public class RecipeDetailsService {
    private final String API_URL = "https://tasty.p.rapidapi.com";
    private final String API_KEY = "40faa789d4mshbc3b0eb07e98efap1429f8jsn889b682016c1";

    @SneakyThrows
    public RecipeDetails getRecipeDetailsFromApi(int API_ID) {
        var URL = API_URL + "/recipes/get-more-info?id=" +API_ID;
        var response = Unirest.get(URL)
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", "tasty.p.rapidapi.com")
                .asJson()
                .getBody();

        return parseJson(response.getObject());
    }

    private RecipeDetails parseJson(JSONObject recipe){
        return RecipeDetails.builder()
                .Name(recipe.getString("name"))
                .API_ID(recipe.getInt("id"))
                .Description(getDescriptionFromJSON(recipe))
                .PictureLink(recipe.getString("thumbnail_url"))
                .Ingredients(getIngredientsFromJSON(recipe))
                .Instructions(getInstructionsFromJSONObject(recipe))
                .build();
    }

    private String getDescriptionFromJSON(JSONObject recipe) {
        String description = "-";

        try {
            description = recipe.getString("description");
            description = description.replaceAll("<a.*a>", "");
        }
        catch (JSONException e) {
            log.info("No description for " + recipe.getString("name"));
        }

        return description;
    }

    private List<String> getIngredientsFromJSON(JSONObject recipe) {
        var result = new ArrayList<String>();
        JSONArray sections = recipe.getJSONArray("sections");

        for (int i = 0; i < sections.length(); i++) {
            JSONObject section = sections.getJSONObject(i);
            JSONArray components = section.getJSONArray("components");

            for(int y = 0; y < components.length(); y++) {
                JSONObject component = components.getJSONObject(y);
                result.add(component.getString("raw_text"));
            }
        }

        return result;
    }

    private List<String> getInstructionsFromJSONObject (JSONObject recipe) {
        var result = new ArrayList<String>();
        JSONArray instructions = recipe.getJSONArray("instructions");

        for (int i = 0; i < instructions.length(); i++) {
            result.add(instructions.getJSONObject(i).getString("display_text"));
        }

        return result;
    }
}

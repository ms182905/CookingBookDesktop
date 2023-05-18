/* (C)2023 */
package pl.soltys.CookingBookApplication.service;

import java.util.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.Unirest;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import pl.soltys.CookingBookApplication.model.Recipe;

@Slf4j
@Service
public class RecipeListService {
  private final String API_URL = "https://tasty.p.rapidapi.com";
  private final String API_KEY = "40faa789d4mshbc3b0eb07e98efap1429f8jsn889b682016c1";

  @SneakyThrows
  public List<Recipe> getRecipeListFromApi(String phrase, int number) {
    phrase = phrase.replaceAll(" ", "+");

    var URL = API_URL + "/recipes/list?from=0&size=" + number + "&q=" + phrase;
    var response =
        Unirest.get(URL)
            .header("X-RapidAPI-Key", API_KEY)
            .header("X-RapidAPI-Host", "tasty.p.rapidapi.com")
            .asJson()
            .getBody();

    if (response.getObject().getInt("count") == 0) {
      log.warn("No recipes found!");
      return Collections.emptyList();
    }

    var result = new ArrayList<Recipe>();
    response
        .getObject()
        .getJSONArray("results")
        .forEach(json -> result.addAll(resolveResponse((JSONObject) json)));

    return result;
  }

  private List<Recipe> resolveResponse(JSONObject json) {
    ArrayList<Recipe> result = new ArrayList<>();

    if (json.has("recipes")) {
      log.info("Many recipes for: " + json.getInt("id"));
      JSONArray recipes = json.getJSONArray("recipes");

      for (int i = 0; i < recipes.length(); i++) {
        result.add(parseJson(recipes.getJSONObject(i)));
      }
    } else {
      result.add(parseJson(json));
    }

    return result;
  }

  private Recipe parseJson(JSONObject recipe) {
    return Recipe.builder()
        .name(recipe.getString("name"))
        .API_ID(recipe.getInt("id"))
        .description(getDescriptionFromJSON(recipe))
        .picture(getImageViewFromUrl(recipe.getString("thumbnail_url")))
        .build();
  }

  private ImageView getImageViewFromUrl(String url) {
    Image image = new Image(url);
    ImageView imageView = new ImageView();

    if (image.getWidth() > image.getHeight()) {
      imageView.setFitWidth(200);
      imageView.setTranslateY((200 - (200 / image.getWidth()) * image.getHeight()) / 2);
    } else {
      imageView.setFitHeight(200);
      imageView.setTranslateX((200 - (200 / image.getHeight()) * image.getWidth()) / 2);
    }

    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);
    imageView.setCache(true);
    imageView.setImage(image);

    return imageView;
  }

  private String getDescriptionFromJSON(JSONObject recipe) {
    String description = "-";

    try {
      description = recipe.getString("description");
      description = description.replaceAll("<a.*a>", "");
    } catch (JSONException e) {
      log.info("No description for " + recipe.getString("name"));
    }

    return description;
  }
}

package pl.soltys.CookingBookApplication.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class RecipeDetails {
    private int API_ID;
    private String Name;
    private List<String> Ingredients;
    private List<String> Instructions;
    private String PictureLink;
    private String Description;

    @Override
    public String toString() {
        return "RecipeDetails{" +
                "API_ID=" + API_ID +
                ", Name='" + Name + '\'' +
                ", Ingredients=" + Ingredients +
                ", Instructions=" + Instructions +
                ", PictureLink='" + PictureLink + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }
}

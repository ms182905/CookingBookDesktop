package pl.soltys.CookingBookApplication.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Recipe {
    private int ID;
    private int API_ID;
    private String Name;
    private String Poster;
}

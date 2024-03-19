/* (C)2023 */
package pl.soltys.CookingBookApplication.model;

import java.util.List;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class RecipeDetails {
  private int API_ID;
  private String name;
  private List<String> ingredients;
  private List<String> instructions;
  private String pictureURL;
  private String description;
}

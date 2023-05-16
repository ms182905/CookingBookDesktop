/* (C)2023 */
package pl.soltys.CookingBookApplication.model;

import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Recipe {
  private int API_ID;
  private String Name;
  private String Description;
  private ImageView Picture;
}

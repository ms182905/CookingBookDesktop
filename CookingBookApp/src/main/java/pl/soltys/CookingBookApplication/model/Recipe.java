/* (C)2023 */
package pl.soltys.CookingBookApplication.model;

import javafx.scene.image.Image;
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

  public Recipe(RecipeDBModel pattern) {
    this.API_ID = pattern.getAPI_ID();
    this.Description = pattern.getDescription();
    this.Name = pattern.getName();
    this.Picture = getImageViewFromUrl(pattern.getPictureURL());
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
}

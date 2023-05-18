/* (C)2023 */
package pl.soltys.CookingBookApplication.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import pl.soltys.CookingBookApplication.model.RecipeDBModel;
import pl.soltys.CookingBookApplication.model.RecipeDetails;
import pl.soltys.CookingBookApplication.service.FavouriteRecipeService;
import pl.soltys.CookingBookApplication.service.RecipeDetailsService;

@Controller
@RequiredArgsConstructor
@Slf4j
@FxmlView("RecipeDetailsStage.fxml")
public class RecipeDetailsController {
  private Stage mainStage;

  @FXML private Pane mainPane;
  @FXML private Label recipeTitleLabel;
  @FXML private ImageView recipePhoto;
  @FXML private ListView<String> recipeIngredients;
  @FXML private ListView<String> recipeSteps;
  @FXML private Label recipeDescriptionLabel;
  @FXML private Button addToFavouritesButton;
  private final RecipeDetailsService recipeDetailsService;
  private final FavouriteRecipeService favouriteRecipeService;
  private RecipeDetails recipeDetails;

  @FXML
  public void initialize() {
    setCellFactoryForListView(recipeIngredients);
    setCellFactoryForListView(recipeSteps);

    mainStage = new Stage();
    mainStage.getIcons().add(new Image("file:src/main/resources/icon.png"));
    mainStage.setTitle("Recipe details");
    mainStage.setScene(new Scene(mainPane));
    recipePhoto.setSmooth(true);
    recipePhoto.setCache(true);
    recipePhoto.setPreserveRatio(true);
  }

  public void show(int API_ID) {
    addToFavouritesButton.setText(
        favouriteRecipeService.contains(API_ID) ? "Remove from favourites" : "Add to favourites");

    displayData(API_ID);
    mainStage.show();
  }

  private void displayData(int API_ID) {
    recipeDetails = recipeDetailsService.getRecipeDetailsFromApi(API_ID);

    recipeTitleLabel.setText(recipeDetails.getName());
    recipeDescriptionLabel.setText(recipeDetails.getDescription());
    setImageViewFromUrl(recipeDetails.getPictureURL());
    recipeIngredients.setItems(FXCollections.observableList(recipeDetails.getIngredients()));
    recipeSteps.setItems(FXCollections.observableList(recipeDetails.getInstructions()));
  }

  private void setImageViewFromUrl(String url) {
    Image image = new Image(url);
    recipePhoto.setTranslateY(0);
    recipePhoto.setTranslateX(0);

    if (image.getWidth() > image.getHeight()) {
      recipePhoto.setTranslateY((340 - (340 / image.getWidth()) * image.getHeight()) / -2);
    } else {
      recipePhoto.setTranslateX((340 - (340 / image.getHeight()) * image.getWidth()) / 2);
    }

    recipePhoto.setImage(image);
  }

  public static void setCellFactoryForListView(ListView<String> listView) {
    listView.setCellFactory(
        param ->
            new ListCell<>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                  setGraphic(null);
                  setText(null);
                } else {
                  setMinWidth(param.getWidth());
                  setMaxWidth(param.getWidth());
                  setPrefWidth(param.getWidth());
                  setWrapText(true);
                  setText(item);
                }
              }
            });
  }

  @FXML
  public void manageFavouriteRecipe() {
    if (favouriteRecipeService.contains(recipeDetails.getAPI_ID())) {
      log.info(
          "Removing from repository: " + recipeDetails.getAPI_ID() + " " + recipeDetails.getName());
      favouriteRecipeService.delete(recipeDetails.getAPI_ID());
      addToFavouritesButton.setText("Add to favourites");
    } else {
      log.info(
          "Adding to repository: " + recipeDetails.getAPI_ID() + " " + recipeDetails.getName());
      favouriteRecipeService.add(
          new RecipeDBModel(
              recipeDetails.getAPI_ID(),
              recipeDetails.getName(),
              recipeDetails.getDescription(),
              recipeDetails.getPictureURL()));
      addToFavouritesButton.setText("Remove from favourites");
    }
  }

  public Stage getMainStage() {
    return this.mainStage;
  }
}

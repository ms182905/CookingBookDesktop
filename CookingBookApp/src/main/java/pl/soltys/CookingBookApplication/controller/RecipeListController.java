/* (C)2023 */
package pl.soltys.CookingBookApplication.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import pl.soltys.CookingBookApplication.model.Recipe;
import pl.soltys.CookingBookApplication.service.RecipeListService;

@Component
@FxmlView("RecipeListStage.fxml")
@Slf4j
public class RecipeListController extends ListController {
  private final FxControllerAndView<RecipeDetailsController, VBox> recipeDetailsController;
  private final FxControllerAndView<FavouriteRecipeListController, VBox>
      favouriteRecipeListController;
  private final RecipeListService recipeListService = new RecipeListService();

  @FXML public Button searchButton;
  @FXML public Button favouritesButton;
  @FXML public TextField inputTextField;

  public RecipeListController(
      FxControllerAndView<RecipeDetailsController, VBox> recipeDetailsController,
      FxControllerAndView<FavouriteRecipeListController, VBox> favouriteRecipeListController) {
    this.recipeDetailsController = recipeDetailsController;
    this.favouriteRecipeListController = favouriteRecipeListController;
  }

  @FXML
  public void initialize() {
    mainTableView.setOnMousePressed(
        event -> {
          if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            log.info("Clicked: " + mainTableView.getSelectionModel().getSelectedItem().getAPI_ID());
            recipeDetailsController
                .getController()
                .show(mainTableView.getSelectionModel().getSelectedItem().getAPI_ID());
          }
        });

    favouritesButton.setOnMouseClicked(
        event -> favouriteRecipeListController.getController().show());
  }

  @FXML
  public void findRecipes() {
    progressIndicator.setVisible(true);
    String phrase = inputTextField.getText();

    Task<List<Recipe>> task =
        new Task<>() {
          @Override
          protected List<Recipe> call() {
            log.info("Starting getRecipesFromApi");
            return recipeListService.getRecipeListFromApi(phrase, 10);
          }
        };

    task.setOnSucceeded(
        t -> {
          List<Recipe> recipes = task.getValue();
          ObservableList<Recipe> data = FXCollections.observableList(recipes);

          setColumnForTableView(mainTableView);
          wrapTextForTableColumn(nameTableColumn);
          wrapTextForTableColumn(descriptionTableColumn);

          progressIndicator.setVisible(false);
          mainTableView.setItems(data);
        });

    Thread thread = new Thread(task);
    thread.start();
  }
}

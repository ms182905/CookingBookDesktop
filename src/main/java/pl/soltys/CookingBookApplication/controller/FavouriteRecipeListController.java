/* (C)2023 */
package pl.soltys.CookingBookApplication.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import pl.soltys.CookingBookApplication.model.Recipe;
import pl.soltys.CookingBookApplication.model.RecipeDBModel;
import pl.soltys.CookingBookApplication.service.FavouriteRecipeService;

@Controller
@RequiredArgsConstructor
@FxmlView("FavouriteRecipeStage.fxml")
@Slf4j
public class FavouriteRecipeListController extends ListController {
  private final FxControllerAndView<RecipeDetailsController, VBox> recipeDetailsController;
  private Stage mainStage;
  @FXML private Pane mainPane;
  @FXML public Label recipeNameLabel = new Label();

  private final FavouriteRecipeService favouriteRecipeService;

  @FXML
  public void initialize() {
    mainTableView.setOnMousePressed(
        event -> {
          if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            log.info("Clicked: " + mainTableView.getSelectionModel().getSelectedItem().getAPI_ID());
            recipeDetailsController
                .getController()
                .show(mainTableView.getSelectionModel().getSelectedItem().getAPI_ID());

            recipeDetailsController
                .getController()
                .getMainStage()
                .setOnCloseRequest(we -> getRecipesFromDb());
          }
        });

    mainStage = new Stage();
    mainStage.getIcons().add(new Image("file:src/main/resources/icon.png"));
    mainStage.setTitle("Favourite recipes");
    mainStage.setScene(new Scene(mainPane));
  }

  public void show() {
    mainStage.show();
    getRecipesFromDb();
  }

  @FXML
  public void getRecipesFromDb() {
    progressIndicator.setVisible(true);

    Task<ObservableList<Recipe>> task =
        new Task<>() {
          @Override
          protected ObservableList<Recipe> call() {

            log.info("Starting getRecipesFromDb");
            mainTableView.getItems().clear();
            List<RecipeDBModel> recipes = favouriteRecipeService.getAll();
            List<Recipe> convertedRecipes = convertRecipeDBModelToRecipe(recipes);
            ObservableList<Recipe> data = FXCollections.observableList(convertedRecipes);

            setColumnForTableView(mainTableView);
            wrapTextForTableColumn(nameTableColumn);
            wrapTextForTableColumn(descriptionTableColumn);

            return data;
          }
        };

    task.setOnSucceeded(
        t -> {
          progressIndicator.setVisible(false);
          mainTableView.setItems(task.getValue());
        });

    Thread thread = new Thread(task);
    thread.start();
  }

  private List<Recipe> convertRecipeDBModelToRecipe(List<RecipeDBModel> recipes) {
    List<Recipe> result = new ArrayList<>();
    for (var recipeDBModel : recipes) {
      result.add(new Recipe(recipeDBModel));
    }

    return result;
  }
}

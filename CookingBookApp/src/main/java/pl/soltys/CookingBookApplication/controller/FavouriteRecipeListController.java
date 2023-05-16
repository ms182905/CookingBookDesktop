/* (C)2023 */
package pl.soltys.CookingBookApplication.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import pl.soltys.CookingBookApplication.model.Recipe;
import pl.soltys.CookingBookApplication.model.RecipeDBModel;
import pl.soltys.CookingBookApplication.service.FavouriteRecipeService;

@Controller
@Component
@RequiredArgsConstructor
@FxmlView("FavouriteRecipeStage.fxml")
@Slf4j
public class FavouriteRecipeListController {
  private final FxControllerAndView<RecipeDetailsController, VBox> recipeDetailsController;
  private Stage stage;
  @FXML private Pane pane_1;
  @FXML public Label recipeNameLabel = new Label();
  @FXML public TableView<Recipe> mainTableView = new TableView<>();
  @FXML public TableColumn<Recipe, ImageView> pictureTableColumn = new TableColumn<>("Picture");
  @FXML public TableColumn<Recipe, String> nameTableColumn = new TableColumn<>("Name");

  @FXML
  public TableColumn<Recipe, String> descriptionTableColumn = new TableColumn<>("Description");

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
                .getStage()
                .setOnCloseRequest(
                    new EventHandler<WindowEvent>() {
                      public void handle(WindowEvent we) {
                        getRecipesFromDb();
                      }
                    });
          }
        });

    this.stage = new Stage();
    stage.setScene(new Scene(pane_1));
  }

  public void show() {
    stage.show();
    getRecipesFromDb();
  }

  @FXML
  public void getRecipesFromDb() {

    Task<List<RecipeDBModel>> task =
        new Task<>() {
          @Override
          protected List<RecipeDBModel> call() {
            log.info("Starting getRecipesFromDb");
            return favouriteRecipeService.getAll();
          }
        };

    task.setOnSucceeded(
        t -> {
          List<RecipeDBModel> recipes = task.getValue();
          List<Recipe> convertedRecipes = convertRecipeDBModelToRecipe(recipes);
          ObservableList<Recipe> data = FXCollections.observableList(convertedRecipes);

          setColumnForTableView(mainTableView);
          wrapTextForTableColumn(nameTableColumn);
          wrapTextForTableColumn(descriptionTableColumn);

          mainTableView.setItems(data);
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

  public void setColumnForTableView(TableView<?> tableView) {
    pictureTableColumn.setCellValueFactory((new PropertyValueFactory<>("Picture")));
    nameTableColumn.setCellValueFactory((new PropertyValueFactory<>("Name")));
    descriptionTableColumn.setCellValueFactory((new PropertyValueFactory<>("Description")));
    tableView.setFixedCellSize(200);
  }

  public static void wrapTextForTableColumn(TableColumn<Recipe, String> tableColumn) {
    tableColumn.setCellFactory(
        tc -> {
          TableCell<Recipe, String> cell = new TableCell<>();
          Text text = new Text();
          cell.setGraphic(text);
          cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
          text.wrappingWidthProperty().bind(tableColumn.widthProperty());
          text.textProperty().bind(cell.itemProperty());
          return cell;
        });
  }
}

package pl.soltys.CookingBookApplication.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import pl.soltys.CookingBookApplication.model.Recipe;
import pl.soltys.CookingBookApplication.service.RecipeListService;

@Component
@FxmlView("RecipeListStage.fxml")
@Slf4j
public class RecipeListController {
  private final FxControllerAndView<RecipeDetailsController, VBox> recipeDetailsController;
  private final RecipeListService recipeListService = new RecipeListService();

  @FXML public Button button_1;
  @FXML public TextField inputTextField;
  @FXML public TableView<Recipe> mainTableView = new TableView<>();
  @FXML public TableColumn<Recipe, ImageView> pictureTableColumn = new TableColumn<>("Picture");
  @FXML public TableColumn<Recipe, String> nameTableColumn = new TableColumn<>("Name");

  @FXML
  public TableColumn<Recipe, String> descriptionTableColumn = new TableColumn<>("Description");

  public RecipeListController(
      FxControllerAndView<RecipeDetailsController, VBox> recipeDetailsController) {
    this.recipeDetailsController = recipeDetailsController;
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
  }

  @FXML
  public void findRecipes() {
    String phrase = inputTextField.getText();

    Task<List<Recipe>> task =
        new Task<>() {
          @Override
          protected List<Recipe> call() {
            log.info("Starting getRecipesFromApi");
            return recipeListService.getRecipesFromApi(phrase, 10);
          }
        };

    task.setOnSucceeded(
        t -> {
          List<Recipe> recipes = task.getValue();
          ObservableList<Recipe> data = FXCollections.observableList(recipes);

          setColumnForTableView(mainTableView);
          wrapTextForTableColumn(nameTableColumn);
          wrapTextForTableColumn(descriptionTableColumn);

          mainTableView.setItems(data);
        });

    Thread thread = new Thread(task);
    thread.start();
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

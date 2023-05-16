/* (C)2023 */
package pl.soltys.CookingBookApplication.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pl.soltys.CookingBookApplication.model.Recipe;

@Controller
@Component
@RequiredArgsConstructor
@FxmlView("FavouriteRecipeStage.fxml")
@Slf4j
public class FavouriteRecipeListController {
    private Stage stage;
    @FXML private Pane pane_1;
    @FXML
    public Label recipeNameLabel = new Label();
    @FXML public TableView<Recipe> mainTableView = new TableView<>();
    @FXML public TableColumn<Recipe, ImageView> pictureTableColumn = new TableColumn<>("Picture");
    @FXML public TableColumn<Recipe, String> nameTableColumn = new TableColumn<>("Name");

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(pane_1));
    }

    public void show() {
        System.out.println("sdf");
        stage.show();
    }
}

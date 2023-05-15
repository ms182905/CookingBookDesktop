package pl.soltys.CookingBookApplication.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.soltys.CookingBookApplication.model.Recipe;
import pl.soltys.CookingBookApplication.service.RecipeDetailsService;

import java.util.List;

@Controller
@FxmlView("RecipeDetailsStage.fxml")
public class RecipeDetailsController {
    private Stage stage;

    @FXML
    private Pane pane_1;
    @FXML
    private Label recipeTitleLabel;
    @FXML
    private ImageView recipePhoto;
    @FXML
    private ListView<String> recipeIngredients;
    @FXML
    private ListView<String> recipeSteps;
    @FXML
    private Label recipeDescriptionLabel;
    private final RecipeDetailsService recipeDetailsService = new RecipeDetailsService();

    @FXML
    public void initialize() {
        setCellFactoryForListView(recipeIngredients);
        setCellFactoryForListView(recipeSteps);

        this.stage = new Stage();
        stage.setScene(new Scene(pane_1));
        recipePhoto.setSmooth(true);
        recipePhoto.setCache(true);
        recipePhoto.setPreserveRatio(true);
    }

    public void show(int API_ID) {
        displayData(API_ID);
        stage.show();
    }


    private void displayData(int API_ID) {
        var recipeDetails = recipeDetailsService.getRecipeDetailsFromApi(API_ID);

        recipeTitleLabel.setText(recipeDetails.getName());
        recipeDescriptionLabel.setText(recipeDetails.getDescription());
        setImageViewFromUrl(recipeDetails.getPictureLink());
        recipeIngredients.setItems(FXCollections.observableList(recipeDetails.getIngredients()));
        recipeSteps.setItems(FXCollections.observableList(recipeDetails.getInstructions()));
    }

    private void setImageViewFromUrl(String url) {
        Image image = new Image(url);
        recipePhoto.setTranslateY(0);
        recipePhoto.setTranslateX(0);

        if (image.getWidth() > image.getHeight()) {
            recipePhoto.setTranslateY((340 - (340 / image.getWidth()) * image.getHeight()) / -2);
        }
        else {
            recipePhoto.setTranslateX((340 - (340 / image.getHeight()) * image.getWidth()) / 2);
        }

        recipePhoto.setImage(image);
    }

    public static void setCellFactoryForListView(ListView<String> listView) {
        listView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setGraphic(null);
                    setText(null);
                }
                else{
                    setMinWidth(param.getWidth());
                    setMaxWidth(param.getWidth());
                    setPrefWidth(param.getWidth());
                    setWrapText(true);
                    setText(item);
                }
            }
        });
    }
}

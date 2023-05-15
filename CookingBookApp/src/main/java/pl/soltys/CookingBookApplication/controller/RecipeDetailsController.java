package pl.soltys.CookingBookApplication.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
@FxmlView("RecipeDetailsStage.fxml")
public class RecipeDetailsController {
    private Stage stage;

    @FXML
    private Pane pane_1;
    @FXML
    private Label recipeTitleLabel;

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(pane_1));

    }

    public void show() {
        stage.show();
        recipeTitleLabel.setText("Clicked");
    }
}

package pl.soltys.CookingBookApplication.application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.soltys.CookingBookApplication.controller.RecipeListController;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {
  private final FxWeaver fxWeaver;

  @Autowired
  public PrimaryStageInitializer(FxWeaver fxWeaver) {
    this.fxWeaver = fxWeaver;
  }

  @Override
  public void onApplicationEvent(StageReadyEvent event) {
    Stage stage = event.stage;
    Scene scene = new Scene(fxWeaver.loadView(RecipeListController.class));
    stage.setScene(scene);
    stage.setResizable(false);
    stage.setTitle("CookingBookApplication");
    stage.show();
  }
}

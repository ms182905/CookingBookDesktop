/* (C)2023 */
package pl.soltys.CookingBookApplication.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.soltys.CookingBookApplication.CookingBookApplication;

public class JavaSpringBootApplication extends Application {
  private ConfigurableApplicationContext context;

  @Override
  public void init() {
    this.context =
        new SpringApplicationBuilder()
            .sources(CookingBookApplication.class)
            .run(getParameters().getRaw().toArray(new String[0]));
  }

  @Override
  public void start(Stage primaryStage) {
    context.publishEvent(new StageReadyEvent(primaryStage));
  }

  @Override
  public void stop() {
    context.close();
    Platform.exit();
  }
}

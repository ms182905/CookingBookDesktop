package pl.soltys.CookingBookApplication;

import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.spring.InjectionPointLazyFxControllerAndViewResolver;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import pl.soltys.CookingBookApplication.application.JavaSpringBootApplication;
import javafx.application.Application;
import javafx.scene.Node;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import pl.soltys.CookingBookApplication.service.RecipeService;

@SpringBootApplication
public class CookingBookApplication {

	public static void main(String[] args) {
		Application.launch(JavaSpringBootApplication.class, args);
		var dd = new RecipeService();
		System.out.println(dd.getRecipesFromApi("chicken soup"));
	}

	@Bean
	public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
		return new SpringFxWeaver(applicationContext);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public <C, V extends Node> FxControllerAndView<C, V> controllerAndView(FxWeaver fxWeaver,
																		   InjectionPoint injectionPoint) {
		return new InjectionPointLazyFxControllerAndViewResolver(fxWeaver)
				.resolve(injectionPoint);
	}
}

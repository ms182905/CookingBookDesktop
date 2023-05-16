package pl.soltys.CookingBookApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.soltys.CookingBookApplication.model.RecipeDBModel;

@Repository
public interface FavouriteRecipeRepository extends JpaRepository<RecipeDBModel, Integer> {}

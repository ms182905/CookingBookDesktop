/* (C)2023 */
package pl.soltys.CookingBookApplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.soltys.CookingBookApplication.model.RecipeDBModel;
import pl.soltys.CookingBookApplication.repository.FavouriteRecipeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavouriteRecipeService {

  private final FavouriteRecipeRepository favouriteRecipeRepository;

  public void add(RecipeDBModel recipe) {
    favouriteRecipeRepository.save(recipe);
  }

  public boolean contains(int API_ID) {
    return favouriteRecipeRepository.existsById(API_ID);
  }

  public void delete(int API_ID) {
    favouriteRecipeRepository.deleteById(API_ID);
  }

  public List<RecipeDBModel> getAll() {
    return favouriteRecipeRepository.findAll();
  }
}

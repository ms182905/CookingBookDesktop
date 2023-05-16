package pl.soltys.CookingBookApplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
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
}
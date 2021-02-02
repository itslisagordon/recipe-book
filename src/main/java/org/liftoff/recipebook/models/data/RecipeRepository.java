package org.liftoff.recipebook.models.data;

import org.liftoff.recipebook.models.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe,Integer> {
    Recipe findById(int id);
    Iterable <Recipe> getAllRecipesByUserId(int user_id);

}

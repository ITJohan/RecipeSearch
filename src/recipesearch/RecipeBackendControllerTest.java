package recipesearch;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeBackendControllerTest {

    @org.junit.Test
    public void setCuisine() {
        RecipeBackendController recipeBackendController = new RecipeBackendController();
        recipeBackendController.setCuisine("Random");

    }
    @Test
    public void getRecipeTest(){
        RecipeBackendController recipeBackendController = new RecipeBackendController();
        recipeBackendController.setCuisine("Sverige");
        recipeBackendController.setDifficulty("Lätt");
        recipeBackendController.getRecipes().forEach(System.out::println);
    }
}
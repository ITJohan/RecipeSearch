package recipesearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;


import java.io.IOException;

/**
 * Ska användas som Komponent för varje recept som ska visas upp i GUI't
 */
public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;
    @FXML private ImageView recipeImage;
    @FXML private TextField recipeLabel;

    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        recipeImage = new ImageView(recipe.getFXImage());
        recipeLabel = new TextField(recipe.getName());
    }
}


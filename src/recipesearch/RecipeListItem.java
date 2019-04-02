package recipesearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;


import java.io.IOException;

/**
 * Ska användas som Komponent för varje recept som ska visas upp i GUI't
 */
public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;

    @FXML private Label itemHeader;
    @FXML private Label itemTimeLabel;
    @FXML private Label itemCostLabel;
    @FXML private Label itemDescLabel;

    @FXML private ImageView itemImage;
    @FXML private ImageView itemFlag;
    @FXML private ImageView itemIngredient;
    @FXML private ImageView itemDifficulty;


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

        initializeImages();
        initializeLabels();
    }

    public void onClick(MouseEvent mouseEvent) {
        parentController.openRecipeView(recipe);
    }

    public void initializeImages(){
        itemImage.setImage(recipe.getFXImage());
        itemFlag.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        itemIngredient.setImage(parentController.getIngredientImage(recipe.getMainIngredient()));
        itemDifficulty.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
    }

    public void initializeLabels(){
        itemHeader.setText(recipe.getName());
        itemTimeLabel.setText(recipe.getTime() + " minuter");
        itemCostLabel.setText(recipe.getPrice()+ " kr");
        itemDescLabel.setText(recipe.getDescription());
    }


    // TODO - Frågor om spinner klicks, och updatera värden
    // Todo - Fråga om hur vi får en border runt en image
}


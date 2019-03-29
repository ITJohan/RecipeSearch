
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    @FXML FlowPane recipeListFlowPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateRecipeList();
    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        RecipeBackendController cc = new RecipeBackendController();
        cc.getRecipes().forEach(recipe ->
                recipeListFlowPane.getChildren().add(new RecipeListItem(recipe, this)));
    }

}
package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.Arrays;
import java.util.List;

public class RecipeBackendController {

    private String cuisine = null;
    private String mainIngredient = null;
    private String difficulty = null;
    private int maxPrice = 0;
    private int maxTime = 0;
    public final List<String> ACCEPTED_CUISINES = Arrays.asList("Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
    public final List<String> ACCEPTED_MAIN_INGREDIENT = Arrays.asList("Kött", "Fisk", "Kyckling", "Vegetarisk");
    public final List<String> ACCEPTED_DIFFICULTIES = Arrays.asList("Lätt", "Mellan", "Svår");

    public List<Recipe> getRecipes(){
        return RecipeDatabase.getSharedInstance().search(new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient));
    }

    public void setCuisine(String cuisine) {
        updateStringValueIfAllowed(ACCEPTED_CUISINES, this.cuisine, cuisine);
    }

    public void setMainIngredient(String mainIngredient) {
      updateStringValueIfAllowed(ACCEPTED_MAIN_INGREDIENT, this.mainIngredient, mainIngredient);
    }

    public void setDifficulty(String difficulty) {
        updateStringValueIfAllowed(ACCEPTED_DIFFICULTIES, this.difficulty, difficulty);
    }

    public void setMaxPrice(int maxPrice) {
        if(maxPrice>0){
            this.maxPrice = maxPrice;
        }
    }

    public void setMaxTime(int maxTime) {
        if((maxTime>=10 && maxTime<=150) && maxTime % 10 == 0){
            this.maxTime = maxTime;
        }
    }

    private void updateStringValueIfAllowed(List<String> values, String valueToUpdate, String searchValue){
        if(values.contains(searchValue)){
            valueToUpdate = searchValue;
        }
        else{
            System.out.println("Not a valid option, the valid options are: ");
            values.forEach(System.out::println);
        }
    }

    public int getMaxTime() {
        return maxTime;
    }
}

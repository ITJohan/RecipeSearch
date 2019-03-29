
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    @FXML FlowPane recipeListFlowPane;
    @FXML ComboBox<String> ingredientComboBox;
    @FXML ComboBox<String> cuisineComboBox;
    @FXML RadioButton allButton;
    @FXML RadioButton easyButton;
    @FXML RadioButton averageButton;
    @FXML RadioButton hardButton;
    @FXML Spinner<Integer> priceSpinner;
    @FXML Label timeLabel;
    @FXML Slider timeSlider;
    RecipeBackendController backendCC = new RecipeBackendController();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateRecipeList();
        initializeIngredientComboBox();
        initializeCuisineComboBox();
        initializeRadioButtons();
        initializeSpinner();
        initializeSlider();
    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        RecipeBackendController cc = new RecipeBackendController();
        cc.getRecipes().forEach(recipe ->
                recipeListFlowPane.getChildren().add(new RecipeListItem(recipe, this)));
    }

    private void initializeIngredientComboBox(){
        ingredientComboBox.getItems().addAll(backendCC.ACCEPTED_MAIN_INGREDIENT);
        ingredientComboBox.getSelectionModel().select(0);
        ingredientComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            backendCC.setMainIngredient(newValue);
            updateRecipeList();
        });
    }

    private void initializeCuisineComboBox(){
        cuisineComboBox.getItems().addAll(backendCC.ACCEPTED_CUISINES);
        cuisineComboBox.getSelectionModel().select(0);
        cuisineComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            backendCC.setCuisine(newValue);
            updateRecipeList();
        });
    }

    private void initializeRadioButtons(){
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        allButton.setToggleGroup(difficultyToggleGroup);
        easyButton.setToggleGroup(difficultyToggleGroup);
        averageButton.setToggleGroup(difficultyToggleGroup);
        hardButton.setToggleGroup(difficultyToggleGroup);
        allButton.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(difficultyToggleGroup.getSelectedToggle()!=null){
                RadioButton seledted = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                backendCC.setDifficulty(seledted.getText());
                updateRecipeList();
            }
        });
    }

    /**
     * Behöver fixa så man kan lägga in egna värden, bör vara focusedProperty, men halvsketchy
     */
    private void initializeSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,500,50,10);
        priceSpinner.setValueFactory(valueFactory);

        priceSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            backendCC.setMaxPrice(newValue);
            updateRecipeList();
        });


        priceSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                int value = Integer.valueOf(priceSpinner.getEditor().getText());
                backendCC.setMaxPrice(value);
                updateRecipeList();
            }
        });

    }

    private void initializeSlider(){
        timeSlider.setMin(10);
        timeSlider.setMax(150);
        timeSlider.setBlockIncrement(timeSlider.getWidth()/14);
        timeSlider.setShowTickMarks(true);
        timeSlider.setValue(20);
        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null && !newValue.equals(oldValue) && timeSlider.isValueChanging()){
                backendCC.setMaxTime(newValue.intValue());
                timeLabel.setText(String.valueOf(backendCC.getMaxTime()));
            }
        });

    }

}
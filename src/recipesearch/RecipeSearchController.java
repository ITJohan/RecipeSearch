
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {


    private RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendCC = new RecipeBackendController();
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
    @FXML Label aRecipeLabel;
    @FXML ImageView aImageView;
    @FXML Button aCloseButton;
    @FXML SplitPane splitSearchPane;
    @FXML AnchorPane recipeDetailPane;
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<>();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateMap();
        updateRecipeList();
        initializeIngredientComboBox();
        initializeCuisineComboBox();
        initializeRadioButtons();
        initializeSpinner();
        initializeSlider();
        populateMainIngredientComboBox();
        populateCuisineComboBox();
        Platform.runLater(()->ingredientComboBox.requestFocus());
    }

    private void updateRecipeList(){
        recipeListFlowPane.getChildren().clear();
        backendCC.getRecipes().forEach(recipe ->
                recipeListFlowPane.getChildren().add(recipeListItemMap.get(recipe.getName())));

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
                RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                backendCC.setDifficulty(selected.getText());
                updateRecipeList();
            }
        });
    }


    private void initializeSpinner(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,500,50,10);
        priceSpinner.setValueFactory(valueFactory);
        priceSpinner.setEditable(true);

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
        timeSlider.setValue(10);
        timeSlider.setBlockIncrement(timeSlider.getWidth() / (timeSlider.getMax() - timeSlider.getMin()) );
        timeSlider.setShowTickMarks(true);


        timeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null && !newValue.equals(oldValue) && timeSlider.isValueChanging()){
                backendCC.setMaxTime(newValue.intValue());
                timeLabel.setText(String.valueOf(backendCC.getMaxTime()));
                updateRecipeList();
            }
        });

    }

    private void populateMap(){
        for(Recipe recipe : backendCC.getRecipes()){
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }
    }

    private void populateRecipeDetailView(Recipe recipe){
        aRecipeLabel.setText(recipe.getName());
        aImageView.setImage(recipe.getFXImage());
    }

    /**
     * Var tvungen att kalla splitPanens .toBack() funktion, kunde ej calla, anchorpanens.toFront().
     * @param recipe
     */
    public void openRecipeView(Recipe recipe){
        populateRecipeDetailView(recipe);
        splitSearchPane.toBack();
    }

    @FXML
    public void closeRecipeView(){
        splitSearchPane.toFront();
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        ingredientComboBox.setButtonCell(cellFactory.call(null));
        ingredientComboBox.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox () {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineComboBox.setButtonCell(cellFactory.call(null));
        cuisineComboBox.setCellFactory(cellFactory);
    }

}
package com.lindenbaum.spanapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.lindenbaum.spanapp.model.*;

public class SpanEditUIController {
	
	@FXML
	private Label SpanNumberLabel;

    @FXML
    private TextField lengthField;
    @FXML
    private TextField InertiaField;
    @FXML
    private Stage dialogStage;
    @FXML    
    private SpanModel SpanModel;
    @FXML    
    private boolean okClicked = false;
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     * 
     * @param person
     */
    public void setSpanModel(SpanModel SpanModel) {
        this.SpanModel = SpanModel;
        SpanNumberLabel.setText(SpanModel.getSpanNumber().toString());
        lengthField.setText(SpanModel.getlength().toString());
        InertiaField.setText(SpanModel.getInertia().toString());
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	SpanModel.setlength(Double.parseDouble(lengthField.getText()));
        	SpanModel.setInertia(Double.parseDouble(InertiaField.getText()));
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        SpanModel.decSpanNumber_pt();
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        // error message for length
        if (lengthField.getText().matches("-\\d+.\\d+") || lengthField.getText().matches("-\\d+")){
        	errorMessage += "Negative figures are not valid for Length Filed!\n"; 
        	//though it would return true for numbers larger than an int
        }
        else if (! lengthField.getText().matches("\\d+.\\d+") && ! lengthField.getText().matches("\\d+") && ! lengthField.getText().matches("\\d.\\d+E-?\\d+")){
        	errorMessage += "Characters are not valid for Length Filed!\n"; 
        	//though it would return true for numbers larger than an int
        }
        else if(Double.parseDouble(lengthField.getText()) <= 0){
        	errorMessage += "length must be larger than 0!\n"; 
        }

        //error message for intertia
        if (InertiaField.getText().matches("-\\d+.\\d+") || InertiaField.getText().matches("-\\d+")){
        	errorMessage += "Negative figures are not valid for Inertia Filed!\n"; 
        	//though it would return true for numbers larger than an int
        }
        else if (! InertiaField.getText().matches("\\d+.\\d+") && ! InertiaField.getText().matches("\\d+") && ! InertiaField.getText().matches("\\d.\\d+E-?\\d+")){
        	errorMessage += "Characters are not valid for Inertia Filed!\n"; 
        	//though it would return true for numbers larger than an int
        }
        else if(Double.parseDouble(InertiaField.getText()) <= 0){
        	errorMessage += "Inertia must be larger than 0!\n"; 
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
        	Alert alert = new Alert(AlertType.ERROR);
			
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			
			alert.showAndWait();
            return false;
        }
    }
}

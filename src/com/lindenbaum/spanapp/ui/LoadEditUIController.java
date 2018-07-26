package com.lindenbaum.spanapp.ui;

import com.lindenbaum.spanapp.model.SpanModel;
import com.lindenbaum.spanapp.model.LoadModel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoadEditUIController {
	
	@FXML
	private Label SpanNumberLabel;
	@FXML
	private Label LengthLabel;
	@FXML
	private Label StartLabel;
	@FXML
	private Label EndLabel;
	@FXML
	private Label Value1Lable;
	@FXML
	private Label Value2Lable;

    @FXML
    private TextField LocationField;
    @FXML
    private TextField ValueField;
    @FXML
    private Stage dialogStage;
    @FXML    
    private SpanModel SpanModel;
    @FXML    
    private LoadModel LoadModel;
    @FXML    
    private boolean okClicked = false;
    @FXML
	private ToggleButton TB1 = new ToggleButton (); //text initialize not working
	@FXML
	private ToggleButton TB2 = new ToggleButton ();
	@FXML
	private ToggleButton TB3 = new ToggleButton ();
	@FXML
	private ToggleButton TB4 = new ToggleButton ();
	
	ToggleGroup group = new ToggleGroup();
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	TB1.setToggleGroup(group);
    	TB2.setToggleGroup(group);
    	TB3.setToggleGroup(group);
    	TB4.setToggleGroup(group);
    	//TB1.setSelected(true);
    	
    	LocationField.setDisable(true);
    	ValueField.setDisable(true);
    	
    	//for TB1 actions
    	TB1.setOnAction((event) -> {
    		LocationField.setDisable(false);
    		ValueField.setDisable(false);
//    		Value1Lable.setTextAlignment(TextAlignment.RIGHT);
//    		Value2Lable.setTextAlignment(TextAlignment.RIGHT);
    		Value1Lable.setText("   d (m):");  		
    		Value2Lable.setText("P (N):");
    		
		});
    	
    	//for TB2 actions
    	TB2.setOnAction((event) -> {
    		LocationField.setDisable(true);
    		ValueField.setDisable(false);
//    		Value1Lable.setTextAlignment(TextAlignment.RIGHT);
//    		Value2Lable.setTextAlignment(TextAlignment.RIGHT);
    		Value1Lable.setText("");
    		Value2Lable.setText("P (N):");
    		
		});
    	
    	//for TB3 actions
    	TB3.setOnAction((event) -> {
    		LocationField.setDisable(false);
    		ValueField.setDisable(false);
//    		Value1Lable.setTextAlignment(TextAlignment.RIGHT);
//    		Value2Lable.setTextAlignment(TextAlignment.RIGHT);
    		Value1Lable.setText("   P1 (N):");
    		Value2Lable.setText("P2 (N):");
		});
    	
    	//for TB4 actions
    	TB4.setOnAction((event) -> {
    		LocationField.setDisable(false);
    		ValueField.setDisable(false);
//    		Value1Lable.setTextAlignment(TextAlignment.RIGHT);
//    		Value2Lable.setTextAlignment(TextAlignment.RIGHT);
    		Value1Lable.setText("   d (m):");
    		Value2Lable.setText("M (N):");
		});
//----------------overrride----------------one button always pressed--------------------------------------    	
    	group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    toggle.setSelected(true);
                                     
                }
            }
        });
//----------------------------------------------------------------------      	
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
    public void setSpanModel(SpanModel SpanModel, LoadModel LoadModel) {
    	Double StartCoordiate;
    	
        this.SpanModel = SpanModel;
        this.LoadModel = LoadModel;
        SpanNumberLabel.setText(LoadModel.getSpanNumber().toString());
        LengthLabel.setText(SpanModel.getlength().toString());
        EndLabel.setText(SpanModel.getNodeModel().getXCoordinate().toString());
        //get start coordinate by subtract length from xcoordinate
        StartCoordiate = SpanModel.getNodeModel().getXCoordinate() - SpanModel.getlength();
        StartLabel.setText(StartCoordiate.toString());
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
        	if(TB1.isSelected()==true){
        		LoadModel.setPLoadPosition(Double.parseDouble(LocationField.getText())); 
        		LoadModel.setPLoadValue(Double.parseDouble(ValueField.getText()));
        		LoadModel.SetType("PLoad");
        	}else if(TB2.isSelected()==true){
        		LoadModel.setDLoadStart(Double.parseDouble(ValueField.getText())); 
        		LoadModel.setDLoadEnd(Double.parseDouble(ValueField.getText()));
        		LoadModel.SetType("DLoad");
        	}else if(TB3.isSelected()==true){
        		LoadModel.setDLoadStart(Double.parseDouble(LocationField.getText())); 
        		LoadModel.setDLoadEnd(Double.parseDouble(ValueField.getText()));
        		LoadModel.SetType("DLoad");
        	}else if(TB4.isSelected()==true){
        		LoadModel.setMomPosition(Double.parseDouble(LocationField.getText())); 
        		LoadModel.setMomValue(Double.parseDouble(ValueField.getText()));
        		LoadModel.SetType("Mom");
        	}      	
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";
        // error message for Location field
        if(TB2.isSelected() == false){ // coz weirded errors
        	
        	//for debugging purpose
//    		System.out.println("Start: " + Double.parseDouble(StartLabel.getText()));
//    		System.out.println("End: " + Double.parseDouble(EndLabel.getText()));
//    		System.out.println("Number: " + Double.parseDouble(LocationField.getText()));
        	
	        if (! LocationField.getText().matches("\\d+.\\d+") && ! LocationField.getText().matches("\\d+")){
	        	errorMessage += "Characters are not valid for Length Filed!\n"; 
	        	//though it would return true for numbers larger than an int
	        	//this is to decide the range
        	}else if(Double.parseDouble(LocationField.getText()) > Double.parseDouble(EndLabel.getText()) - Double.parseDouble(StartLabel.getText())){
        		errorMessage += "Not in the correct range!\n";
        	}
        }

        //error message for value field
        if (! ValueField.getText().matches("\\d+.\\d+") && ! ValueField.getText().matches("\\d+") && ! ValueField.getText().matches("-\\d+")){    
        	errorMessage += "Characters are not valid for Inertia Filed!\n"; 
        	//though it would return true for numbers larger than an int
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

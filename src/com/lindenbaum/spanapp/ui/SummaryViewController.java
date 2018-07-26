package com.lindenbaum.spanapp.ui;

import com.lindenbaum.spanapp.MainApp;
import com.lindenbaum.spanapp.model.ResultModel1;
import com.lindenbaum.spanapp.model.ResultModel2;
import com.lindenbaum.spanapp.model.ResultModel3;
import com.lindenbaum.spanapp.model.ResultModel4;
import com.lindenbaum.spanapp.model.SpanModel;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SummaryViewController {
	//just the result view

    @FXML
    private Stage dialogStage;
    
    // table1
    @FXML
    private TableView<ResultModel1> NodeResult;
    @FXML
    private TableColumn<ResultModel1, String> NodeNo;
    @FXML
    private TableColumn<ResultModel1, Number> Deflection;
    @FXML
    private TableColumn<ResultModel1, Number> Reaction;
    
    // table2
    @FXML
    private TableView<ResultModel2> SpanEndResult;
    @FXML
    private TableColumn<ResultModel2, String> SpanNo;
    @FXML
    private TableColumn<ResultModel2, Number> Shear;
    @FXML
    private TableColumn<ResultModel2, Number> Moments;
    
    // table3
    @FXML
    private TableView<ResultModel3> SpanResult;
    @FXML
    private TableColumn<ResultModel3, Number> DistanceFromLeft;
    @FXML
    private TableColumn<ResultModel3, Number> Deflection3;
    @FXML
    private TableColumn<ResultModel3, Number> Shear3;
    @FXML
    private TableColumn<ResultModel3, Number> Moments3;
    
    
    // table4
    @FXML
    private TableView<ResultModel4> SpanSummary;
    @FXML
    private TableColumn<ResultModel4, Number> SpanNo4;
    @FXML
    private TableColumn<ResultModel4, Number> Deflection4;
    @FXML
    private TableColumn<ResultModel4, String> Shearrange;
    @FXML
    private TableColumn<ResultModel4, String> Momentsrange;
	
    // Reference to the main application.
    private MainApp mainApp;


    @FXML
    private void initialize() {

        // Initialize the Result1 table with the three columns.
    	// fix integer
    	NodeNo.setCellValueFactory(cellData -> cellData.getValue().NodeNoProperty());
    	Deflection.setCellValueFactory(cellData -> cellData.getValue().DeflectionProperty());
    	Reaction.setCellValueFactory(cellData -> cellData.getValue().ReactionProperty());
        
    	// Initialize the Result2 table with the three columns.
    	// fix integer
    	SpanNo.setCellValueFactory(cellData -> cellData.getValue().SpanNoProperty());
    	Shear.setCellValueFactory(cellData -> cellData.getValue().ShearProperty());
    	Moments.setCellValueFactory(cellData -> cellData.getValue().MomentsProperty());
        
    	// Initialize the Result3 table with the three columns.
    	// fix integer
    	DistanceFromLeft.setCellValueFactory(cellData -> cellData.getValue().DistanceFromLeftProperty());
    	Deflection3.setCellValueFactory(cellData -> cellData.getValue().Deflection3Property());
    	Shear3.setCellValueFactory(cellData -> cellData.getValue().Shear3Property());
    	Moments3.setCellValueFactory(cellData -> cellData.getValue().Moments3Property());
        
    	// Initialize the Result4 table with the three columns.
    	// fix integer
    	SpanNo4.setCellValueFactory(cellData -> cellData.getValue().SpanNo4Property());
    	Deflection4.setCellValueFactory(cellData -> cellData.getValue().Deflection4Property());
    	Shearrange.setCellValueFactory(cellData -> cellData.getValue().ShearrangeProperty());
    	Momentsrange.setCellValueFactory(cellData -> cellData.getValue().MomentsrangeProperty());
    }
	
    public void SummaryViewController(){	
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        NodeResult.setItems(mainApp.getResultModel1Data());
        SpanEndResult.setItems(mainApp.getResultModel2Data());
        SpanResult.setItems(mainApp.getResultModel3Data());
        SpanSummary.setItems(mainApp.getResultModel4Data());
    }
	
}

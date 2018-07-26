package com.lindenbaum.spanapp;

import java.io.IOException;

import com.lindenbaum.spanapp.model.LoadModel;
import com.lindenbaum.spanapp.model.ResultModel1;
import com.lindenbaum.spanapp.model.ResultModel2;
import com.lindenbaum.spanapp.model.ResultModel3;
import com.lindenbaum.spanapp.model.ResultModel4;
import com.lindenbaum.spanapp.model.SpanModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.lindenbaum.spanapp.ui.*;

public class MainApp extends Application {
	

	AnchorPane rootLayout;
	Stage primaryStage;
	
    private ObservableList<SpanModel> SpanModelData = FXCollections.observableArrayList();
    private ObservableList<LoadModel> LoadModelData = FXCollections.observableArrayList();
    public ObservableList<ResultModel1> ResultModel1data = FXCollections.observableArrayList();
    public ObservableList<ResultModel2> ResultModel2data = FXCollections.observableArrayList();
    public ObservableList<ResultModel3> ResultModel3data = FXCollections.observableArrayList();
    public ObservableList<ResultModel4> ResultModel4data = FXCollections.observableArrayList();

    public MainApp() {
        // Add some sample data
/*        SpanModelData.add(new SpanModel(1, 100.0, 100.0));
        SpanModelData.add(new SpanModel(3, 100.0, 100.0));
        SpanModelData.add(new SpanModel(6, 55.0, 100.0));
        SpanModelData.add(new SpanModel(4, 100.0, 100.0));
        SpanModelData.add(new SpanModel(5, 100.0, 100.0));*/
 /*   	
    	ResultModel1data.add(new ResultModel1(1.1,1.1,1.1));
    	ResultModel1data.add(new ResultModel1(1.1,1.1,1.1));
    	ResultModel1data.add(new ResultModel1(1.1,1.1,1.1));
    	ResultModel1data.add(new ResultModel1(1.1,1.1,1.1));
    	
    	ResultModel2data.add(new ResultModel2(1.1,1.1,1.1));
    	ResultModel2data.add(new ResultModel2(1.1,1.1,1.1));
    	ResultModel2data.add(new ResultModel2(1.1,1.1,1.1));
    	ResultModel2data.add(new ResultModel2(1.1,1.1,1.1));

    	ResultModel3data.add(new ResultModel3(1.1,1.1,1.1,1.1));
    	ResultModel3data.add(new ResultModel3(1.1,1.1,1.1,1.1));
    	ResultModel3data.add(new ResultModel3(1.1,1.1,1.1,1.1));
    	ResultModel3data.add(new ResultModel3(1.1,1.1,1.1,1.1));
    	
    	ResultModel4data.add(new ResultModel4(1.1,1.1,"test","test"));
    	ResultModel4data.add(new ResultModel4(1.1,1.1,"test","test"));
    	ResultModel4data.add(new ResultModel4(1.1,1.1,"test","test"));
    	ResultModel4data.add(new ResultModel4(1.1,1.1,"test","test"));
    	*/
    }
    

    /**
     * Returns the data as an observable list of SpanModel. 
     * @return
     */
    public ObservableList<SpanModel> getSpanModelData() {
        return SpanModelData;
    }
    
    /**
     * Returns the data as an observable list of LoadModel. 
     * @return
     */
    public ObservableList<LoadModel> getLoadModelData() {
        return LoadModelData;
    }

    /**
     * Returns the data as an observable list of Result1. 
     * @return
     */
    public ObservableList<ResultModel1> getResultModel1Data() {
        return ResultModel1data;
    }
    /**
     * Returns the data as an observable list of Result1. 
     * @return
     */
    public ObservableList<ResultModel2> getResultModel2Data() {
        return ResultModel2data;
    }
    /**
     * Returns the data as an observable list of Result1. 
     * @return
     */
    public ObservableList<ResultModel3> getResultModel3Data() {
        return ResultModel3data;
    }
    /**
     * Returns the data as an observable list of Result1. 
     * @return
     */
    public ObservableList<ResultModel4> getResultModel4Data() {
        return ResultModel4data;
    }
    
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

        this.primaryStage.setTitle("SpanApp");
        
        initRootLayout();
        
	}
	/**
	 * Constructor
	 */

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ui/SpanAppUI.fxml"));
			rootLayout =(AnchorPane)loader.load();
			
			//Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();

            // Give the controller access to the main app.
	        SpanAppUIController controller = loader.getController();
	        controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified Span. If the user
     * clicks OK, the changes are saved into the provided SpanModel object and true
     * is returned.
     * 
     * @param SpanModel the SpanModel object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showSpanEditUI(SpanModel SpanModel) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ui/SpanEditUI.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit SpanModel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            SpanEditUIController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSpanModel(SpanModel);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Opens a dialog to edit Load details for the specified Span. If the user
     * clicks OK, the changes are saved into the provided LoadModel object and true
     * is returned.
     * 
     * @param SpanModel the SpanModel object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showLoadEditUI(SpanModel SpanModel, LoadModel LoadModel) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ui/LoadEditUI.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit LoadModel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            LoadEditUIController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSpanModel(SpanModel,LoadModel);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * show result UI
     * @return
     */
    public boolean showResultUI(){

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ui/SummaryView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("SummaryView");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the into the controller.
            SummaryViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // here to care
	        controller.setMainApp(this);
            //controller.setSpanModel(SpanModel);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return true;
          //  return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * show About UI
     * @return
     */
    public boolean showAboutUI(){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ui/AboutUI.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("About");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AboutUIController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

	public static void main(String[] args) {
		launch(args);
	}
	

}

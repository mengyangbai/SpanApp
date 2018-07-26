package com.lindenbaum.spanapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AboutUIController {
	
    @FXML
    private Stage dialogStage;
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
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks Credit.
     */
    @FXML
    private void handleCredit() {
    	String Message = "";
        	
    	Alert CreditInfo = new Alert(AlertType.INFORMATION);
		
    	//CreditInfo.setTitle("Credit Information");
    	CreditInfo.setHeaderText("Credit");
    	Message = "Finite Element Algorithm: Xiaojun Chen\nGraphic User Interface: Joe Jiang and Mengyang Bai";
    	CreditInfo.setContentText(Message);
		
    	CreditInfo.showAndWait();

        okClicked = true;
//        dialogStage.close();

    }
    
    /**
     * Called when the user clicks Credit.
     */
    @FXML
    private void handleLicense() {
    	String Message = "";
    	
    	Alert LicenseInfo = new Alert(AlertType.INFORMATION);
		
    	//CreditInfo.setTitle("Credit Information");
    	LicenseInfo.setHeaderText("License");
    	Message = "SpanApp Confidential\n"
    			+"--------------------------------\n"
    			+"[2014]-[2015] Lindenbaum Pty Ltd\n"
    			+"All Rights Reserved\n\n"
    			+"Notice:\n"
    			+"All information contained herein is, and remains\n"
    			+"the property of Lindenbaum Oty LTd and its suppliers,\n"
    			+"if any. The intellectual and technical concepts contained\n"
    			+"herein are proprietary to Lindeanbaum Pty Ltd and its suppliers\n"
    			+"and may be covered by Australian and Foreign Patents,\n"
    			+"patent in process, and are protected by trade secret or copyright law."
    			+"Dissemination of this information\n or reproducation of this material"
    			+"is strictly \nforbidden unless prior written permission is obtained\n"
    			+"from Lindenbaum Pty Ltd.";
    	
    	LicenseInfo.setContentText(Message);
		
    	LicenseInfo.showAndWait();
        okClicked = true;
//        dialogStage.close();
    }

    /**
     * Called when the user clicks close.
     */
    @FXML
    private void handleClose() {

        dialogStage.close();
    }

}

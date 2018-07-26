package com.lindenbaum.spanapp.ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.util.StringConverter;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;

import com.lindenbaum.spanapp.model.LoadModel;
import com.lindenbaum.spanapp.model.ModelSolver;
import com.lindenbaum.spanapp.model.NodeModel;
import com.lindenbaum.spanapp.model.ResultModel1;
import com.lindenbaum.spanapp.model.ResultModel2;
import com.lindenbaum.spanapp.model.ResultModel3;
import com.lindenbaum.spanapp.model.ResultModel4;
import com.lindenbaum.spanapp.model.SpanModel;

import javafx.scene.control.ComboBox;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

//import ObservableList;

import com.lindenbaum.spanapp.MainApp;

public class SpanAppUIController {
	
	private ModelSolver solver;
	
	// save button
	@FXML
	private Button Save = new Button();
	// load button
	@FXML
	private Button Load = new Button();
	
	// for geometry table
    @FXML
    private TableView<SpanModel> GeometryTable;
    @FXML
    private TableColumn<SpanModel, Number> SpanNumber;
    @FXML
    private TableColumn<SpanModel, Number> length;
    @FXML
    private TableColumn<SpanModel, Number> Inertia;
 // for load table
    @FXML
    private TableView<LoadModel> LoadTable;
    @FXML
    private TableColumn<LoadModel, Number> SpanNumberForLoad;
    @FXML
    private TableColumn<LoadModel, String> LoadType;
    @FXML
    private TableColumn<LoadModel, Number> LoadValue1;
    @FXML
    private TableColumn<LoadModel, Number> LoadValue2;
    
    
    @FXML
    private TextField YoungModulus;
    
    @FXML
    private ComboBox<NodeModel> SupportComboBox;
	private ObservableList<NodeModel> SupportComboBoxData = FXCollections.observableArrayList();
	
	@FXML
    private ComboBox<SpanModel> LoadComboBox;
	private ObservableList<SpanModel> LoadComboBoxData = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> BoundaryConditionComboBox;
	private ObservableList<String> BoundaryConditionData = FXCollections.observableArrayList();
	
	@FXML 
	private Button SpanButtonEdit = new Button();
	@FXML 
	private Button SpanButtonDelete = new Button();
	@FXML 
	private Button LoadButtonAdd = new Button();
	@FXML 
	private Button LoadButtonEdit = new Button();
	@FXML 
	private Button LoadButtonDelete = new Button();
	@FXML 
	private Button CalculateButton = new Button();
	
	@FXML
	private ToggleButton TB_SupportX; 
	//= new ToggleButton ("B"); 
	//text initialize not working
	@FXML
	private ToggleButton TB_SupportY; 
	//= new ToggleButton ("B");
	@FXML
	private ToggleButton  TB_SupportM;
	//= new ToggleButton ("B");

//	
//	//right output table1
//	@FXML
//    final CategoryAxis xAxis1 = new CategoryAxis();
//	@FXML
//    final NumberAxis yAxis1 = new NumberAxis();
//	@FXML
//	private LineChart<String,Number> ResultTable1 = new LineChart<String,Number>(xAxis1,yAxis1);
//	 

    @FXML
    private ScrollPane pane;
	@FXML 
	private Canvas BeamCanvas;
	// drawing
	private GraphicsContext gc;

	//right output table2
	@FXML
    final NumberAxis xAxis2 = new NumberAxis();
	@FXML
    final NumberAxis yAxis2 = new NumberAxis();
	@FXML
	private LineChart<Number,Number> ResultTable2 = new LineChart<Number,Number>(xAxis2,yAxis2);

	//right output table3
	@FXML
    final NumberAxis xAxis3 = new NumberAxis();
	@FXML
    final NumberAxis yAxis3 = new NumberAxis();
	@FXML
	private LineChart<Number,Number> ResultTable3 = new LineChart<Number,Number>(xAxis3,yAxis3);

	//right output table4
	@FXML
    final NumberAxis xAxis4 = new NumberAxis();
	@FXML
    final NumberAxis yAxis4 = new NumberAxis();
	@FXML
	private LineChart<Number,Number> ResultTable4 = new LineChart<Number,Number>(xAxis4,yAxis4);
	
	//set relative part.
	@FXML
	private TextField distance;
	@FXML
	private Text deflection = new Text();
	@FXML
	private Text shear = new Text();
	@FXML
	private Text moment = new Text();
	@FXML
	private Text deflection1 = new Text();
	@FXML
	private Text shear1 = new Text();
	@FXML
	private Text moment1 = new Text();
	
	// result button
	@FXML
	private Button viewresult;
	
    // Reference to the main application.
    private MainApp mainApp;
    //this list is for deleting associated loadmodels of the specific spanmodel
    private ObservableList<LoadModel> CopyLoadModelData = FXCollections.observableArrayList();
    

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public SpanAppUIController() {  
    	BoundaryConditionData.add("Pin");
    	BoundaryConditionData.add("Roller");
    	BoundaryConditionData.add("Fix");
    	BoundaryConditionData.add("Free");
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    
    //draw the picture
    public void draw(){
		gc =BeamCanvas.getGraphicsContext2D();
		gc.clearRect(0, 0, 580, 150);
    	//canvas 580 150
    	if(!mainApp.getSpanModelData().isEmpty()){
    		int number = mainApp.getSpanModelData().size();
    		gc.setLineWidth(5);
    		// line is 540 length
    		gc.strokeLine(20, 112.5, 560, 112.5);
    		double[] Nodes = new double[number+1];
    		Nodes[0] = 0;
    		for(int i = 0; i < number; i++){
    			Nodes[i+1] = Nodes[i]+ mainApp.getSpanModelData().get(i).getlength();
    		}
    		// the oval is to small so set to 10 
    		double radius = 10;
    		// remember to calculate radius
    		double totallength = Nodes[number];
    		for(int i = 0; i < number; i++){
    			//starting point
    			double tmpX = 20 + 540 * Nodes[i+1]/totallength - radius/2;
    			double tmpY = 112.5-radius/2;
        		//draw the string under the line is too ugly i put it above the line
        		tmpX = tmpX - 540 * mainApp.getSpanModelData().get(i).getlength() /totallength /2;
        		tmpY = 100;
        		String tmpString = mainApp.getSpanModelData().get(i).getlength() + " m";
        		gc.fillText(tmpString,tmpX,tmpY);

        		//draw  the Load
    			double starttmpX = 20 + 540 * Nodes[i]/totallength;
    			double starttmpY = 112.5;
        		//to avoid draw same line multiple times, use boolean to avoid
        		boolean[] check = new boolean[LoadTable.getItems().size()];
        		for(int j = 0; j<LoadTable.getItems().size();j++){
        			//
        			if((check[j] == false)&& i == (LoadTable.getItems().get(j).getSpanNumberInt()-1)){
        				// PLoad
        				if(LoadTable.getItems().get(j).getType().equals("PLoad")){
        					double tmpPosition = LoadTable.getItems().get(j).getPLoadPosition();
        					double tmpValue = LoadTable.getItems().get(j).getPLoadValue();
        					for(int h = j+1; h<LoadTable.getItems().size();h++){
        						// if the span and the type and the position is the same
        						if(
        								(i == LoadTable.getItems().get(h).getSpanNumberInt()-1)
        								&& LoadTable.getItems().get(h).getType().equals("PLoad")
        								&&(tmpPosition == LoadTable.getItems().get(h).getPLoadPosition())
           								){
        							//clear the check
        							check[h] = true;
        							tmpValue += LoadTable.getItems().get(h).getPLoadValue();
        						}
        					}
        					//start to draw starttmpX starttmpY
        					
        					//calculate the newX
        					double newX = starttmpX + (tmpPosition / totallength) * 540;
        
        					gc.setFill(Color.RED);
        		    		gc.setLineWidth(3);
        					//draw a length of 40
        		    		gc.setStroke(Color.RED);
        					gc.strokeLine(newX, starttmpY - 60, newX, starttmpY);
        					gc.strokePolygon(new double[]{newX,newX-3,newX+3}, new double[]{starttmpY,starttmpY-3,starttmpY-3}, 3);
        		    		gc.fillText(tmpValue+"N",newX-10,starttmpY-65);
        					gc.setStroke(Color.BLACK);
        					gc.setFill(Color.BLACK);
        		    		gc.setLineWidth(5);
        		    		
        					//System.out.println("Ploadposition:" + tmpPosition+"\t" + "Ploadvalue:"+tmpValue);
        				}
        				// DLoad
        				else if(LoadTable.getItems().get(j).getType().equals("DLoad")){
        					double tmpValue1 = LoadTable.getItems().get(j).getDLoadStart();
        					double tmpValue2 = LoadTable.getItems().get(j).getDLoadEnd();

        					for(int h = j+1; h<LoadTable.getItems().size();h++){
        						//if the span and type are the same
        						if(
        								(i == LoadTable.getItems().get(h).getSpanNumberInt()-1)
        								&& LoadTable.getItems().get(h).getType().equals("DLoad")
        								){
        							//clear the check
        							check[h] = true;
        							//add
        							tmpValue1 += LoadTable.getItems().get(h).getDLoadStart();
                					tmpValue2 += LoadTable.getItems().get(h).getDLoadEnd();
        						}
        					}
    						//start to draw starttmpX 
        		    		double tmpEnd = 20 + 540 * Nodes[i+1]/totallength;
        		    		gc.setLineWidth(3);
        					gc.setFill(Color.BLUE);
        		    		gc.setStroke(Color.BLUE);
        					gc.strokeLine(starttmpX, starttmpY - 40, starttmpX, starttmpY);
        					gc.strokeLine(tmpEnd, starttmpY - 40, tmpEnd, starttmpY);
        					gc.strokeLine(starttmpX, starttmpY - 40, tmpEnd, starttmpY  - 40);
        					gc.strokePolygon(new double[]{starttmpX,starttmpX-3,starttmpX+3}, new double[]{starttmpY-5,starttmpY-8,starttmpY-8}, 3);
        					gc.strokePolygon(new double[]{tmpEnd,tmpEnd-3,tmpEnd+3}, new double[]{starttmpY-5,starttmpY-8,starttmpY-8}, 3);
        					

        					gc.fillText(tmpValue1+"N",starttmpX,starttmpY-45);
        					gc.fillText(tmpValue2+"N",tmpEnd-25,starttmpY-45);
        					gc.setStroke(Color.BLACK);
        					gc.setFill(Color.BLACK);
        		    		gc.setLineWidth(5);
        					//System.out.println("Dload1:" + tmpValue1+"\t" + "Dload2:"+tmpValue2);

        				}
        				// Moments
        				else if(LoadTable.getItems().get(j).getType().equals("Mom")){

        					double tmpPosition = LoadTable.getItems().get(j).getMomPosition();
        					double tmpValue = LoadTable.getItems().get(j).getMomValue();

        					for(int h = j+1; h<LoadTable.getItems().size();h++){
        						// if the span and the type and the position is the same
        						if(
        								(i == LoadTable.getItems().get(h).getSpanNumberInt()-1)
        								&& LoadTable.getItems().get(h).getType().equals("Mom")
        								&&(tmpPosition == LoadTable.getItems().get(h).getMomPosition())
           								){
        							//clear the check
        							check[h] = true;
        							tmpValue += LoadTable.getItems().get(h).getMomValue();
        						}
        					}
        					//start to draw
        					if(tmpValue !=0){
            					//calculate the newX
            					double newX = starttmpX + (tmpPosition / totallength) * 540;

            					//draw a length of 40
            		    		gc.setLineWidth(3);
            					gc.setFill(Color.PURPLE);
            		    		gc.setStroke(Color.PURPLE);
            		    		//draw the flower"wan"
            					gc.strokeLine(newX-20, starttmpY - 20, newX, starttmpY - 20);
            					gc.strokeLine(newX, starttmpY - 20, newX, starttmpY+20);
            					gc.strokeLine(newX, starttmpY + 20, newX+20, starttmpY+20);
            					gc.strokePolygon(new double[]{newX-23,newX-20,newX-20}, new double[]{starttmpY-20,starttmpY-23,starttmpY-17}, 3);
            					gc.strokePolygon(new double[]{newX+23,newX+20,newX+20}, new double[]{starttmpY+20,starttmpY+23,starttmpY+17}, 3);
            		    		//compare
            					if(tmpValue>0){
                					gc.fillText(tmpValue+"Nm",newX-20,starttmpY-25);
            		    		}
            		    		else{
                					gc.fillText(tmpValue+"Nm",newX,starttmpY+35);
            		    		}
            					gc.setStroke(Color.BLACK);
            					gc.setFill(Color.BLACK);
            		    		gc.setLineWidth(5);
        					}
        					//System.out.println("Momposition:" + tmpPosition+"\t" + "Momvalue:"+tmpValue);
        				}
        			}
        		}
        		
    		}
    		number =SupportComboBoxData.size();
    		
    		//draw the node
    		for(int i = 0; i <number ;i++){

    			double tmpX = 20 + 540 * Nodes[i]/totallength - radius/2;
    			double tmpY = 112.5-radius/2;
    			if(SupportComboBoxData.get(i).getSupportX() == true &&SupportComboBoxData.get(i).getSupportY()== true
    					&& SupportComboBoxData.get(i).getSupportM() == false){
    				//aka FFB pin

            		gc.fillOval(tmpX, tmpY, radius, radius);
            		gc.setLineWidth(1);
            		gc.strokePolygon(new double[]{tmpX+radius/2,tmpX,tmpX+radius}, new double[]{tmpY+radius,tmpY+radius*2,tmpY+radius*2}, 3);  				
    			}
    			else if(SupportComboBoxData.get(i).getSupportX() == false &&SupportComboBoxData.get(i).getSupportY()== true
    					&& SupportComboBoxData.get(i).getSupportM() == false){
    				//aka BFB roller

            		gc.fillOval(tmpX, tmpY, radius, radius);   	
            		gc.setLineWidth(1);
    				gc.strokeLine(tmpX, tmpY-radius+5, tmpX+radius, tmpY-radius+5);
    				gc.strokeLine(tmpX, tmpY+radius+5, tmpX+radius, tmpY+radius+5);
    			}
    			else if(SupportComboBoxData.get(i).getSupportX() == true &&SupportComboBoxData.get(i).getSupportY()== true
    					&& SupportComboBoxData.get(i).getSupportM() == true){
    				//aka BFB fix
					int tmp = 3;
    				if(i == 0){
    					gc.fillRect(tmpX, tmpY-tmp, radius/2, radius+tmp*2);
                		gc.fillOval(tmpX, tmpY, radius, radius); 
    				}
    				else if(i == (number - 1)){
    					gc.fillRect(tmpX+radius/2, tmpY-tmp, radius/2, radius+tmp*2);
                		gc.fillOval(tmpX, tmpY, radius, radius); 
    				}
    				else{
    					gc.fillRect(tmpX, tmpY-tmp*2, radius, radius+tmp*2);
                		gc.fillOval(tmpX, tmpY, radius, radius); 
    				}
    			}
    			else{
    				//draw regular
            		gc.fillOval(tmpX, tmpY, radius, radius);   				
    			}
    		}
    		
    		
    	}
    	
    }
    
    @FXML
    private void initialize() {
    	
    	
        // Initialize the Span table with the three columns.
    	//fix integer
    	SpanNumber.setCellValueFactory(cellData -> cellData.getValue().SpanNumberProperty());
    	length.setCellValueFactory(cellData -> cellData.getValue().lengthProperty());
    	Inertia.setCellValueFactory(cellData -> cellData.getValue().InertiaProperty());
    	 // Initialize the Load table with the three columns.
    	//fix integer
    	SetCellValueForLoadTable();

    	//disbale when nothing
    	disbaleGeometryTableButtons();
    	//disable when nothing
		LoadButtonAdd.setDisable(true);
    	disableLoadTableButtons();
    	// Init SupportComboBox items.
    	SupportComboBox.setItems(SupportComboBoxData);
//    	SupportComboBox.setEditable(true);   
    	
		// Define rendering of the list of values in support ComboBox drop down. 
    	SupportComboBox.setCellFactory((comboBox) -> {
			return new ListCell<NodeModel>() {
				@Override
				protected void updateItem(NodeModel item, boolean empty) {
					super.updateItem(item, empty);
					
					if (item == null || empty) {
						setText(null);
					} else {
						setText("Node " + item.getNodeNumberinString()+ " XCoordinate: "+ item.getXCoordinate());
					}
				}
			};
		});
		
		// Define rendering of selected value shown in ComboBox.
    	SupportComboBox.setConverter(new StringConverter<NodeModel>() {
			@Override
			public String toString(NodeModel selectedNodeModel) {
				if (selectedNodeModel == null) {
					return null;
				} else {
					return "Node " + selectedNodeModel.getNodeNumberinString()+ " XCoordinate: "+ selectedNodeModel.getXCoordinate();
				}
			}

			@Override
			public NodeModel fromString(String tmpNodeModelString) {
				return null; // No conversion fromString needed.
			}
		});
    	
    	//for SupportComboBox actions
    	SupportComboBox.setOnAction((event) -> {
    		//----------set to empty when SupportComboBox changes--------------------
    		BoundaryConditionComboBox.valueProperty().set(null);
    		//---------------------------
			NodeModel selectedNodeModel = SupportComboBox.getSelectionModel().getSelectedItem();
			if(selectedNodeModel!=null){
				TB_SupportX.setSelected(selectedNodeModel.getSupportX().booleanValue());
				TB_SupportY.setSelected(selectedNodeModel.getSupportY().booleanValue());
				TB_SupportM.setSelected(selectedNodeModel.getSupportM().booleanValue());
				setToggleButtonTexts(TB_SupportX, selectedNodeModel.getSupportX().booleanValue());
				setToggleButtonTexts(TB_SupportY, selectedNodeModel.getSupportY().booleanValue());
				setToggleButtonTexts(TB_SupportM, selectedNodeModel.getSupportM().booleanValue());
			}
		});
//----------------------------------------------------------------------------------------------------------   
    	BoundaryConditionComboBox.setItems(BoundaryConditionData);
    	//for BoundaryConditionComboBox actions
    	BoundaryConditionComboBox.setOnAction((event) -> {
			NodeModel selectedNodeModel = SupportComboBox.getSelectionModel().getSelectedItem();
			String option = BoundaryConditionComboBox.getValue();
			//System.out.println(option);
			if(selectedNodeModel!=null){
				if(option=="Pin"){
					selectedNodeModel.setSupportX(true);
					selectedNodeModel.setSupportY(true);
					selectedNodeModel.setSupportM(false);
				}
				else if(option == "Roller"){
					selectedNodeModel.setSupportX(true);
					selectedNodeModel.setSupportY(false);
					selectedNodeModel.setSupportM(true);				
				}
				else if(option == "Fix"){
					selectedNodeModel.setSupportX(true);
					selectedNodeModel.setSupportY(true);
					selectedNodeModel.setSupportM(true);	
				}
				else if(option == "Free"){
					selectedNodeModel.setSupportX(false);
					selectedNodeModel.setSupportY(false);
					selectedNodeModel.setSupportM(false);	
				}
				
				TB_SupportX.setSelected(selectedNodeModel.getSupportX().booleanValue());
				TB_SupportY.setSelected(selectedNodeModel.getSupportY().booleanValue());
				TB_SupportM.setSelected(selectedNodeModel.getSupportM().booleanValue());
				setToggleButtonTexts(TB_SupportX, selectedNodeModel.getSupportX().booleanValue());
				setToggleButtonTexts(TB_SupportY, selectedNodeModel.getSupportY().booleanValue());
				setToggleButtonTexts(TB_SupportM, selectedNodeModel.getSupportM().booleanValue());	
				draw();

			}else{
				// Show the error message.
	        	Alert alert = new Alert(AlertType.ERROR);				
				alert.setTitle("Invalid SupportComboBox Input");
				alert.setHeaderText("Please Select a Node from SupportComboBox");				
				alert.showAndWait();
			}
		});
 //----------------------------------------------------------------------------------------------------------------------------------   	
    	// Init LoadComboBox items.
    	LoadComboBox.setItems(LoadComboBoxData);
//    	SupportComboBox.setEditable(true);   
    	
		// Define rendering of the list of values in ComboBox drop down. 
    	LoadComboBox.setCellFactory((comboBox) -> {
			return new ListCell<SpanModel>() {
				@Override
				protected void updateItem(SpanModel item, boolean empty) {
					super.updateItem(item, empty);
					
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.getSpanNumberinString());
					}
				}
			};
		});
		
		// Define rendering of selected value shown in ComboBox.
    	LoadComboBox.setConverter(new StringConverter<SpanModel>() {
			@Override
			public String toString(SpanModel selectedSpanModel) {
				if (selectedSpanModel == null) {
					return null;
				} else {
					return selectedSpanModel.getSpanNumberinString();
				}
			}

			@Override
			public SpanModel fromString(String tmpLoadModelString) {
				return null; // No conversion fromString needed.
			}
		});
    	
		//for LoadComboBox actions
    	LoadComboBox.setOnAction((event) -> {    		
			//currently there should be no actions
    		LoadButtonAdd.setDisable(false);  		
		});
    	
    	//add canvas
        // add canvas;
        BeamCanvas = new Canvas(580,150);
        pane.setContent(BeamCanvas);
        
        // set image of X Y M toggle buttons
        
        Image image = new Image(getClass().getResourceAsStream("XToggleB.jpg"));
        
//        setToggleButtonImageX(TB_SupportX);
//        setToggleButtonImageY(TB_SupportY);
//        setToggleButtonImageM(TB_SupportM);
    	
    }
    
    /**
     * handle save
     * @throws IOException 
     */
    @FXML
    private void handleSave() throws IOException{

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Results");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Data files", "*.csv"),
                new ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
        	PrintWriter fileWriter = null;
        	fileWriter  = new PrintWriter(file);
        	
        	//start to write
        	//geometry table
        	fileWriter.println("GeometryTable:");
        	for(SpanModel temp:mainApp.getSpanModelData()){
        		fileWriter.write(temp.getlength() + "," + temp.getInertia() + "\n");
        	}
        	
        	//young modulus
        	fileWriter.println("Young Modulus:");
        	//may cause bug
        	fileWriter.println(YoungModulus.getText());
        	
        	//support combo box
            //NodeRestraion
        	fileWriter.println("Support combobox:");
            for(NodeModel temp: SupportComboBoxData){
            	fileWriter.print(temp.getSupportX()+","+ temp.getSupportY()
            			+","+temp.getSupportM()+"\n");
            }
            
        	//load table
        	fileWriter.println("LoadTable:");
        	for(LoadModel temp:mainApp.getLoadModelData()){
        		fileWriter.write(temp.getSpanNumber()+","+temp.getType()+","+temp.getValue1ForDisplay()
        				+","+temp.getValue2ForDisplay()+"\n");
        	}
        	//beamCon
        	fileWriter.println("BeamCon:");
        	
            for(int i = 0; i < solver.beamCon.length;i++){
            	for(int j = 0; j <solver.beamCon[i].length; j++){
            		fileWriter.print(solver.beamCon[i][j] + ",");
            	}
            	fileWriter.println();
            }
            
            //nodeCorrdniates
        	fileWriter.println("nodeCoordinates:");

            for(int i = 0; i < solver.nodeCoordinates.length;i++){
            	for(int j = 0; j <solver.nodeCoordinates[i].length; j++){
            		fileWriter.print(solver.nodeCoordinates[i][j] + ",");
            	}
            	fileWriter.println();
            }
            
            //NodeRestraion
        	fileWriter.println("nodeCoordinates:");
            for(int i = 0; i < solver.nodeRestrain.length;i++){
            	for(int j = 0; j <solver.nodeRestrain[i].length; j++){
            		fileWriter.print(solver.nodeRestrain[i][j].char_data + ",");
            	}
            	fileWriter.println();
            }
            
            //propertyTypes
          	fileWriter.println("propertyTypes:");
            for(int i = 0; i < solver.propertyTypes.length;i++){
            	for(int j = 0; j <solver.propertyTypes[i].length; j++){
            		fileWriter.print(solver.propertyTypes[i][j] + ",");
            	}
            	fileWriter.println();
            }
            
            //distloads
          	fileWriter.println("distLoads:");
            for(int i = 0; i < solver.distLoads.length;i++){
            	for(int j = 0; j <solver.distLoads[i].length; j++){
            		fileWriter.print(solver.distLoads[i][j] + ",");
            	}
            	fileWriter.println();
            }

            //pointLoads
          	fileWriter.println("pointLoads:");
            for(int i = 0; i < solver.pointLoads.length;i++){
            	for(int j = 0; j <solver.pointLoads[i].length; j++){
            		fileWriter.print(solver.pointLoads[i][j] + ",");
            	}
            	fileWriter.println();
            }
            //moments
          	fileWriter.println("moments:");
            for(int i = 0; i < solver.moments.length;i++){
            	for(int j = 0; j <solver.moments[i].length; j++){
            		fileWriter.print(solver.moments[i][j] + ",");
            	}
            	fileWriter.println();
            }
            
            //nodeResult
          	fileWriter.println("nodeResult:");
            for(int i = 0; i < solver.nodeResult.length;i++){
            	for(int j = 0; j <solver.nodeResult[i].length; j++){
            		fileWriter.print(solver.nodeResult[i][j] + ",");
            	}
            	fileWriter.println();
            }

            //beamEndResult
          	fileWriter.println("beamEndResult:");
            for(int i = 0; i < solver.beamEndResult.length;i++){
            	for(int j = 0; j <solver.beamEndResult[i].length; j++){
            		fileWriter.print(solver.beamEndResult[i][j] + ",");
            	}
            	fileWriter.println();
            }
            
            //beamResult
          	fileWriter.println("beamResult:");
            for(int i = 0; i < solver.beamResult.length;i++){
            	for(int j = 0; j <solver.beamResult[i].length; j++){
            		fileWriter.print(solver.beamResult[i][j] + ",");
            	}
            	fileWriter.println();
            }
            fileWriter.close();
        }
        else{
        }
    }

    @FXML
    private void handleLoad() throws IOException{

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Results");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Data files", "*.csv"),
                new ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(null);
        //--------for stupid support combobox input-----------------
        int SupportComboBoxLoopIdentifier = 0;
        //----------------------------
        if (file != null) {
//--------------when there is file loaded, clear pre-existing data---------------------------//
        	mainApp.getSpanModelData().clear();
        	mainApp.getLoadModelData().clear();
        	SupportComboBoxData.clear(); 
        	LoadComboBoxData.clear();

        	if(SupportComboBoxData.size()>0){
        		SupportComboBoxData.get(SupportComboBoxData.size()-1).clearXCoordinate_pt();
        	}    	
        	if(mainApp.getSpanModelData().size()>0){
        		mainApp.getSpanModelData().get(mainApp.getSpanModelData().size()-1).ClearSpanNumber_pt();
        	}

//---------------------------------------------------------------------------------------------//
        	BufferedReader  fileReader =new BufferedReader(new FileReader(file));
        	String line = null;
        	//load file
        	while((line = fileReader.readLine())!=null){
        		//GeometryTable:
        		if(line.equals("GeometryTable:")){
        			while(!(line = fileReader.readLine()).equals("Young Modulus:")){
        				String temp[] = line.split(",");
        		        SpanModel tempSpanModel = new SpanModel();
        		        tempSpanModel.setlength(Double.parseDouble(temp[0]));
        		        tempSpanModel.setInertia(Double.parseDouble(temp[1]));
    		            mainApp.getSpanModelData().add(tempSpanModel);
    		          //Add all existing spanmodels to comboboxdata
    		            if(tempSpanModel.getSpanNumber().intValue()==1){
    		            	SupportComboBoxData.add(new NodeModel());
    		            }
    		        	LoadComboBoxData.add(tempSpanModel);
    		            GeometryTable.getItems().get(GeometryTable.getItems().size()-1).getNodeModel().incXCoordinate(GeometryTable.getItems().get(GeometryTable.getItems().size()-1).getlength());
    		            SupportComboBoxData.add(GeometryTable.getItems().get(GeometryTable.getItems().size()-1).getNodeModel());
    		            //-------display----
    		            System.out.println(line);
    		            //------------
        			}
		        	SpanButtonEdit.setDisable(false);
		    		SpanButtonDelete.setDisable(false);
        		}
        		if(line.equals("Young Modulus:")){
        			line = fileReader.readLine();
        			YoungModulus.setText(line);
        			 //-------display----
        			System.out.println(line);
        			 //------------------
        		}
        		if(line.equals("Support combobox:")){
        			while(!(line = fileReader.readLine()).equals("LoadTable:")){
        				String temp[] = line.split(",");
//-------------------------------------stupid but working---------------------------------------------------------------------
        				NodeModel tmpNode = SupportComboBoxData.get(SupportComboBoxLoopIdentifier);

    					if(temp[0].equals("true")){
    						tmpNode.setSupportX(true);
    					}else{
    						tmpNode.setSupportX(false);
    					}
    					
    					if(temp[1].equals("true")){
    						tmpNode.setSupportY(true);
    					}else{
    						tmpNode.setSupportY(false);
    					}
    					
    					if(temp[2].equals("true")){
    						tmpNode.setSupportM(true);
    					}else{
    						tmpNode.setSupportM(false);
    					}
    					
    					TB_SupportX.setSelected(tmpNode.getSupportX().booleanValue());
    					TB_SupportY.setSelected(tmpNode.getSupportY().booleanValue());
    					TB_SupportM.setSelected(tmpNode.getSupportM().booleanValue());
    					setToggleButtonTexts(TB_SupportX, tmpNode.getSupportX().booleanValue());
    					setToggleButtonTexts(TB_SupportY, tmpNode.getSupportY().booleanValue());
    					setToggleButtonTexts(TB_SupportM, tmpNode.getSupportM().booleanValue());	
//----------------------------------------------------------------------------------------------------------    					
        				//-------display----
        				System.out.println(line);
        				//-----------------------
        				SupportComboBoxLoopIdentifier++;
        			}
        		}
        		if(line.equals("LoadTable:")){
        			while(!(line = fileReader.readLine()).equals("BeamCon:")){
        				String temp[] = line.split(",");
        				
        		    	SpanModel tempSpanModel = LoadComboBox.getItems().get(Integer.parseInt(temp[0])-1);
        		    	LoadModel tempLoadModel = new LoadModel(tempSpanModel.getSpanNumber()); 
        		    	
        	        	if(temp[1].equals("PLoad")){
        	        		tempLoadModel.setPLoadPosition(Double.parseDouble(temp[2])); 
        	        		tempLoadModel.setPLoadValue(Double.parseDouble(temp[3]));
        	        		tempLoadModel.SetType("PLoad");
        	        	}else if(temp[1].equals("DLoad") && temp[2].equals(temp[3])){
        	        		tempLoadModel.setDLoadStart(Double.parseDouble(temp[2])); 
        	        		tempLoadModel.setDLoadEnd(Double.parseDouble(temp[3]));
        	        		tempLoadModel.SetType("DLoad");
        	        	}else if(temp[1].equals("DLoad") && ! temp[2].equals(temp[3])){
        	        		tempLoadModel.setDLoadStart(Double.parseDouble(temp[2])); 
        	        		tempLoadModel.setDLoadEnd(Double.parseDouble(temp[3]));
        	        		tempLoadModel.SetType("DLoad");
        	        	}else if(temp[1].equals("Mom")){
        	        		tempLoadModel.setMomPosition(Double.parseDouble(temp[2])); 
        	        		tempLoadModel.setMomValue(Double.parseDouble(temp[3]));
        	        		tempLoadModel.SetType("Mom");
        	        	}   
        	        	
    		            mainApp.getLoadModelData().add(tempLoadModel);   
    		            //-------display----
        				System.out.println(line);
        				//-----------------------
        			}
		        	LoadButtonEdit.setDisable(false);
		    		LoadButtonDelete.setDisable(false);
        		}
        	}
        	
            draw();
        }
    }
    
    
    //as the function name
    void SetCellValueForLoadTable(){
    	
    	SpanNumberForLoad.setCellValueFactory(cellData -> cellData.getValue().SpanNumberProperty());
    	LoadType.setCellValueFactory(cellData -> cellData.getValue().TypeProperty());
		LoadValue1.setCellValueFactory(cellData -> cellData.getValue().Value1ForDisplayProperty());
		LoadValue2.setCellValueFactory(cellData -> cellData.getValue().Value2ForDisplayProperty());
    }
    //disable geometry table buttons when there is null
    void disbaleGeometryTableButtons(){
    	if(GeometryTable.getItems().size() == 0){
    		SpanButtonEdit.setDisable(true);
    		SpanButtonDelete.setDisable(true);    		
    	}
    }
    
	 //disable Load table buttons when there is null
    void disableLoadTableButtons(){
    	if(LoadTable.getItems().size() == 0){
    		LoadButtonEdit.setDisable(true);
    		LoadButtonDelete.setDisable(true);   		
    	}
    }
    
    //for setting toggle button caption
    void setToggleButtonTexts(ToggleButton tb, Boolean bool){
    	if(bool == true){tb.setText("F");}else{tb.setText("B");}
    }
    
    void setToggleButtonImageX(ToggleButton tbX){
    	BufferedImage unselectedXB = null;
    	BufferedImage selectedXB = null;
    	//read X 
    	try {
			unselectedXB = ImageIO.read(new File("button_icon/MToggleB.JPG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			selectedXB = ImageIO.read(new File("button_icon//MToggleF.JPG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
   	
    	Image unselectedX = SwingFXUtils.toFXImage(unselectedXB, null);
    	Image selectedX = SwingFXUtils.toFXImage(selectedXB, null);
    	
	    final ImageView    toggleImageX = new ImageView();
	    
	    tbX.setGraphic(toggleImageX);
	    
	    toggleImageX.imageProperty().bind(Bindings
	      .when(tbX.selectedProperty())
	        .then(selectedX)
	        .otherwise(unselectedX)
	    );
    }

    void setToggleButtonImageY(ToggleButton tbY){
    	BufferedImage unselectedYB = null;
    	BufferedImage selectedYB = null;
    	//read X 
    	try {
			unselectedYB = ImageIO.read(new File("button_icon/MToggleB.JPG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			selectedYB = ImageIO.read(new File("button_icon//MToggleF.JPG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
   	
    	Image unselectedY = SwingFXUtils.toFXImage(unselectedYB, null);
    	Image selectedY = SwingFXUtils.toFXImage(selectedYB, null);
    	
	    final ImageView    toggleImageY = new ImageView();
	    
	    tbY.setGraphic(toggleImageY);
	    
	    toggleImageY.imageProperty().bind(Bindings
	      .when(tbY.selectedProperty())
	        .then(selectedY)
	        .otherwise(unselectedY)
	    );
    }
    
    void setToggleButtonImageM(ToggleButton tbM){
    	BufferedImage unselectedMB = null;
    	BufferedImage selectedMB = null;
    	//read M
    	try {
			unselectedMB = ImageIO.read(new File("button_icon/MToggleB.JPG"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			selectedMB = ImageIO.read(new File("button_icon//MToggleF.JPG"));
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    	Image unselectedM = SwingFXUtils.toFXImage(unselectedMB, null);
    	Image selectedM = SwingFXUtils.toFXImage(selectedMB, null);

	    final ImageView    toggleImageM = new ImageView();

	    tbM.setGraphic(toggleImageM);

	    toggleImageM.imageProperty().bind(Bindings
		  	      .when(tbM.selectedProperty())
		  	        .then(selectedM)
		  	        .otherwise(unselectedM)
		  	    );
    }

    /**
     * Show resulttable
     */
    @FXML
    private void handleResult(){
    	//pass data to result view
    	boolean okClicked = mainApp.showResultUI();
    }
    
    /**
     * Called when the user clicks the new button above geometry table. Opens a dialog to edit
     * details for a new span.
     */
    @FXML
    private void handleNewSpanModel() {
        SpanModel tempSpanModel = new SpanModel();
        boolean okClicked = mainApp.showSpanEditUI(tempSpanModel);
        if (okClicked) {
            mainApp.getSpanModelData().add(tempSpanModel);
          //Add all existing spanmodels to comboboxdata
            if(tempSpanModel.getSpanNumber().intValue()==1){
            	SupportComboBoxData.add(new NodeModel());
            }
        	LoadComboBoxData.add(tempSpanModel);
            GeometryTable.getItems().get(GeometryTable.getItems().size()-1).getNodeModel().incXCoordinate(GeometryTable.getItems().get(GeometryTable.getItems().size()-1).getlength());
            SupportComboBoxData.add(GeometryTable.getItems().get(GeometryTable.getItems().size()-1).getNodeModel());
        	SpanButtonEdit.setDisable(false);
    		SpanButtonDelete.setDisable(false);

            draw();
        }
    }
    
    /**
     * Called when the user clicks the new button above load table. Opens a dialog to edit
     * details for a new load.
     */
    @FXML
    private void handleNewLoadModel() {
    	SpanModel tempSpanModel = LoadComboBox.getSelectionModel().getSelectedItem();
    	LoadModel tempLoadModel = new LoadModel(tempSpanModel.getSpanNumber()); 
        boolean okClicked = mainApp.showLoadEditUI(tempSpanModel,tempLoadModel);
        if (okClicked) {
//        	LoadButtonAdd.setDisable(true); // can add multiple
            mainApp.getLoadModelData().add(tempLoadModel);
        	LoadButtonEdit.setDisable(false);
    		LoadButtonDelete.setDisable(false);
    		
    		//draw the picture
    		draw();

        }
    }
    
	
	/**
	 * Called when the user clicks the edit button above geometry table. Opens a dialog to edit
	 * details for the selected Span.
	 */
	@FXML
	private void handleEditSpanModel() {
	    SpanModel selectedSpanModel = GeometryTable.getSelectionModel().getSelectedItem();
	    int selectedIndex = GeometryTable.getSelectionModel().getSelectedIndex();
	    if (selectedSpanModel != null) {
	    	//minus the previous length for all spans after the selected span
	    	for(int i=selectedIndex; i<GeometryTable.getItems().size();i++){
	    		SpanModel SpanModelInstance = GeometryTable.getItems().get(i);
	    		SpanModelInstance.getNodeModel().editXCoordinate(-selectedSpanModel.getlength());
	    	}	    	
	        boolean okClicked = mainApp.showSpanEditUI(selectedSpanModel);	  
	        //pls the editted length for all spans after the selected span
	        for(int i=selectedIndex; i<GeometryTable.getItems().size();i++){
	    		SpanModel SpanModelInstance = GeometryTable.getItems().get(i);
	    		SpanModelInstance.getNodeModel().editXCoordinate(selectedSpanModel.getlength());
	    	}	
            draw();
	    } else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No SpanModel Selected");
			alert.setContentText("Please select a SpanModel in the table.");
			
			alert.showAndWait();
	    }
	}
	

	
	/**
	 * Called when the user clicks the edit button above load button. Opens a dialog to edit
	 * details for the selected load.
	 */
	@FXML
	private void handleEditLoadModel() {
	    LoadModel selectedLoadModel = LoadTable.getSelectionModel().getSelectedItem();
	    if (selectedLoadModel != null) {
	    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ may cause problems
	        boolean okClicked = mainApp.showLoadEditUI(GeometryTable.getItems().get(selectedLoadModel.getSpanNumberInt()-1) , selectedLoadModel);
	        //get the corresponding SpanModel 
	        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	        
	        //draw the canvas
	        draw();
	    } else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No SpanModel Selected");
			alert.setContentText("Please select a SpanModel in the table.");
			
			alert.showAndWait();
	    }
	}
	
	/**
	 * Called when the user clicks on the delete button above geometry table.
	 */
	@FXML
	private void handleDeleteSpanModel() {
	    SpanModel selectedSpanModel = GeometryTable.getSelectionModel().getSelectedItem();

	    if (selectedSpanModel != null) {
		    int selectedIndex = GeometryTable.getSelectionModel().getSelectedIndex();	
		    int selectedSpanNum = GeometryTable.getItems().get(selectedIndex).getSpanNumberInt();// for removing related loadmodels
		    int length = GeometryTable.getItems().size();
		    for(int i = selectedIndex; i < length; i++){
		    	SpanModel tmpSpanModel = GeometryTable.getItems().get(i);
		    	NodeModel tmpNodeModel = tmpSpanModel.getNodeModel();
		    	//minus 1 for SpanNumber as Index
		    	tmpSpanModel.setSpanNumber(tmpSpanModel.getSpanNumber()-1);
		    	//minus 1 for SpanNumberInt as load combo box output
		    	tmpSpanModel.setSpanNumberInt(tmpSpanModel.getSpanNumberInt()-1);
		    	//minus 1 for NodeNumber as Index
		    	tmpNodeModel.setNodeNumber(tmpNodeModel.getNodeNumber()-1);
		    	//minus 1 for NodeNumberInt as support combo box output
		    	tmpNodeModel.setNodeNumberInt(tmpNodeModel.getNodeNumberInt()-1);
		    	//minus length for related Nodes
		    	tmpNodeModel.setXCoordinate(tmpNodeModel.getXCoordinate()-selectedSpanModel.getlength());
		    	//update on Geometry Table
		    	//GeometryTable.getItems().set(i, tmpSpanModel);
		    	if(i == length-1){
		    		//minus 1 for SpanNumberPointer
		    		tmpSpanModel.decSpanNumber_pt();
		    		//minus XCoordinate_py
		    		tmpNodeModel.decXCoordinate_pt(selectedSpanModel.getlength());
		    	}
		    }
		    
		    GeometryTable.getItems().remove(selectedIndex);//remove in geometry table
		    SupportComboBoxData.remove(selectedIndex+1); //remove option on combo box 
		    LoadComboBoxData.remove(selectedIndex); //remove option on combo box 
		    
		    if( GeometryTable.getItems().size() == 0){ // if after the deletion, size is 0
		    	SupportComboBoxData.remove(0);
		    }
		    	    
		    disbaleGeometryTableButtons();
		    
		    //iteration through loadmodel list and remove all related loadmodels
		    int LoadLength = LoadTable.getItems().size();
		    for(int i = 0; i < LoadLength; i++){
		    	LoadModel tmpLoadModel = LoadTable.getItems().get(i);
		    	 
		    	if(tmpLoadModel.getSpanNumberInt() == selectedSpanNum){ //mark to be deleted
		    		tmpLoadModel.SetType("");
		    		tmpLoadModel.setSpanNumberInt(0);
		    		tmpLoadModel.setSpanNumber(0);
//			    	 System.out.println("I`m here first"); 
//			    	 System.out.println("loadModel: " + tmpLoadModel.getSpanNumberInt());
//			    	 System.out.println("SpanModel: " + selectedSpanNum);
		    	}else if(tmpLoadModel.getSpanNumberInt() > selectedSpanNum){ //for loadmodels associated with span of bigger number		    		
		    		tmpLoadModel.setSpanNumber(tmpLoadModel.getSpanNumber()-1);
		    		tmpLoadModel.setSpanNumberInt(tmpLoadModel.getSpanNumberInt()-1);
//		    		System.out.println("I`m here again"); 
		    	}
		    }	

            draw();
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * how to do the real deletion ??????????????? there is concurrent modification error when one spanmodel associated with multiple load models
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
	    } else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No SpanModel Selected");
			alert.setContentText("Please select a SpanModel in the table.");
			
			alert.showAndWait();
	    }

	}

	/**
	 * Called when the user clicks on the delete button above load table.
	 */
	@FXML
	private void handleDeleteLoadModel() {
	    LoadModel selectedLoadModel = LoadTable.getSelectionModel().getSelectedItem();

	    if (selectedLoadModel != null) {
		    int selectedIndex = LoadTable.getSelectionModel().getSelectedIndex();
		    LoadTable.getItems().remove(selectedIndex);//remove in geometry table
		    //clear corresponding data
		    if(selectedLoadModel.getType().toString() == "DLoad"){
		    	selectedLoadModel.setDLoadStart(new Double(0.0));
		    	selectedLoadModel.setDLoadEnd(new Double(0.0));
		    }else if (selectedLoadModel.getType().toString() == "PLoad"){
		    	selectedLoadModel.setPLoadPosition(new Double(0.0));
		    	selectedLoadModel.setPLoadValue(new Double(0.0));
		    }else if (selectedLoadModel.getType().toString() == "Mom"){
		    	selectedLoadModel.setMomPosition(new Double(0.0));
		    	selectedLoadModel.setMomValue(new Double(0.0));
		    }
		    //clear Type and Value1 and Value2
		    selectedLoadModel.SetType("");
		    selectedLoadModel.SetValue1ForDisplay(new Double(0.0));
		    selectedLoadModel.SetValue2ForDisplay(new Double(0.0));
		    // disable when nothing		    
		    disableLoadTableButtons();
		    //conditionally enable add button 
		    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~should be no more useful~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		    //if(selectedLoadModel.getSpanNumber() == LoadComboBox.getSelectionModel().getSelectedItem().getSpanNumber())
		    //	LoadButtonAdd.setDisable(false);	
		    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		    
		    //draw the canvas
		    draw();
		    
	    } else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No SpanModel Selected");
			alert.setContentText("Please select a SpanModel in the table.");
			
			alert.showAndWait();
	    }
	}
	
	/**
	 * Called when the user clicks the TB_SupportX toggle button.
	 */
	@FXML
	private void handleSupportXToggle() {
		NodeModel selectedNodeModel = SupportComboBox.getSelectionModel().getSelectedItem();
		boolean tmpBool;
	    if (selectedNodeModel != null) {
	    	tmpBool = !selectedNodeModel.getSupportX();
	    	selectedNodeModel.setSupportX(tmpBool);
	    	setToggleButtonTexts(TB_SupportX,tmpBool);

            draw();
	    }else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No Span Selected");
			alert.setContentText("Please select a SpanModel in the drop down box.");
			
			alert.showAndWait();
	    }
	    
	}
	
	/**
	 * Called when the user clicks the TB_SupportY toggle button.
	 */
	@FXML
	private void handleSupportYToggle() {
		NodeModel selectedNodeModel = SupportComboBox.getSelectionModel().getSelectedItem();
		boolean tmpBool;
	    if (selectedNodeModel != null) {
	    	tmpBool = !selectedNodeModel.getSupportY();
	    	selectedNodeModel.setSupportY(tmpBool);
	    	setToggleButtonTexts(TB_SupportY,tmpBool);
	    	

            draw();
	    }else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No Span Selected");
			alert.setContentText("Please select a SpanModel in the drop down box.");
			
			alert.showAndWait();
	    }
	    
	}

	/**
	 * Called when the user clicks the TB_SupportM toggle button.
	 */
	@FXML
	private void handleSupportMToggle() {
		NodeModel selectedNodeModel = SupportComboBox.getSelectionModel().getSelectedItem();
		boolean tmpBool;
	    if (selectedNodeModel != null) {
	    	tmpBool = !selectedNodeModel.getSupportM();
	    	selectedNodeModel.setSupportM(tmpBool);
	    	setToggleButtonTexts(TB_SupportM,tmpBool);
	    	

            draw();
	    }else {
	        // Nothing selected.
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("No Selection");
			alert.setHeaderText("No Span Selected");
			alert.setContentText("Please select a SpanModel in the drop down box.");
			
			alert.showAndWait();
	    }
	    
	}
	
	@FXML
	private void handleCalculate(){
		if(YoungModulusIsInputValid()){
			solver = new ModelSolver();
			solver.initialise(mainApp.getSpanModelData(), mainApp.getLoadModelData(),Double.parseDouble(YoungModulus.getText()), SupportComboBox.getItems().get(0));
			solver.run();

	        //clear the data first
	        ResultTable2.getData().clear();
	        ResultTable3.getData().clear();
	        ResultTable4.getData().clear();
	        mainApp.ResultModel1data.clear();
	        mainApp.ResultModel2data.clear();
	        mainApp.ResultModel3data.clear();
	        mainApp.ResultModel4data.clear();
			
		   for(int i =0; i < solver.beamResult.length;i++){
			   for(int j =0; j < 8;j++){
					   System.out.print(solver.beamResult[i][j] + "\t");					   		   
			   }
			   System.out.println();
			}

	        //connect the data to the table 
//	        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	        XYChart.Series series2 = new XYChart.Series<>();
	        XYChart.Series series3 = new XYChart.Series<>();
	        XYChart.Series series4 = new XYChart.Series<>();
	        
	         
	        //set significant digits no perfect solution in current situation
	        

	        for(int i = 0; i < solver.distanceFromLeft.length; i++){
	        	double temp = solver.distanceFromLeft[i];
//	            series1.getData().add(new XYChart.Data<>(temp ,solver.beamResult[i][0]));
	            series2.getData().add(new XYChart.Data<>(temp ,solver.beamResult[i][2]));
	            series3.getData().add(new XYChart.Data<>(temp ,solver.beamResult[i][3]));
	            series4.getData().add(new XYChart.Data<>(temp ,solver.beamResult[i][6]));
	        }
//	        ResultTable1.getData().add(series1);
//	        ResultTable1.setCreateSymbols(false);


	        ResultTable2.getData().add(series2);
	        ResultTable2.setCreateSymbols(false);

	        ResultTable3.getData().add(series3);
	        ResultTable3.setCreateSymbols(false);
	        ResultTable4.getData().add(series4);
	        ResultTable4.setCreateSymbols(false);
	        
	        // add data to the result
	        // first page
	        String tmpString = "";
	        int tmpmask = 0;
	        for(int i = 0; i < solver.nodeResult.length;i++){
	        	if(tmpmask == 0){
	        		tmpString = "DX";
	        		tmpmask++;	        		
	        	}else if(tmpmask == 1){
	        		tmpString = "DY";
	        		tmpmask++;
	        	}
	        	else{
	        		tmpString = "RZ";
	        		tmpmask=0;
	        	}
	        	int tmpnumber = i / 3 +1;
	        	tmpString = tmpString + tmpnumber;
	        	mainApp.ResultModel1data.add(new ResultModel1(tmpString,solver.nodeResult[i][0],solver.nodeResult[i][1]));
	        }
	        
	        // second page
	        for(int i = 0; i < solver.beamEndResult.length;i++){
	        	int tmpx = i / 2 + 1;
	        	int tmpy = i % 2 + 1;
	        	String tmp = tmpx + "-" + tmpy;
	        	
	        	mainApp.ResultModel2data.add(new ResultModel2(tmp,solver.beamEndResult[i][0],solver.beamEndResult[i][2]));
	        }
	        
	        // third page
	        for(int i = 0; i < solver.beamResult.length; i++){	        	
	        	mainApp.ResultModel3data.add(new ResultModel3(solver.beamResult[i][1],solver.beamResult[i][6],solver.beamResult[i][2],solver.beamResult[i][3]));
	        }
	        
	        // fourth page 
	        int tmpNumber = solver.beamResult.length / solver.NumSegs;
	        double[] deflection = new double[tmpNumber];
	        String[] shearrange = new String[tmpNumber];
	        String[] momentrange = new String[tmpNumber];
	        for(int i = 0; i < tmpNumber;i++){
	        	double maxflection = 0.0;
	        	double lowshearange = 0.0;
	        	double highshearange = 0.0;
	        	double lowmomentrange = 0.0;
	        	double highmomentrange = 0.0;
	        	for(int j = i * solver.NumSegs; j < (i+1)*solver.NumSegs;j++){
	        		if(Math.abs(solver.beamResult[j][6]) > maxflection){
	        			maxflection = Math.abs(solver.beamResult[j][6]);
	        		}
	        		if(solver.beamResult[j][2]>highshearange){
	        			highshearange = solver.beamResult[j][2];
	        		}
	        		if(solver.beamResult[j][2]<lowshearange){
	        			lowshearange = solver.beamResult[j][2];
	        		}
	        		if(solver.beamResult[j][3]>highmomentrange){
	        			highmomentrange = solver.beamResult[j][3];
	        		}
	        		if(solver.beamResult[j][3]<lowmomentrange){
	        			lowmomentrange = solver.beamResult[j][3];
	        		}
	        	}
	        	deflection[i] = maxflection;
	        	shearrange[i] = "(" + lowshearange + "," + highshearange + ")";
	        	momentrange[i] = "(" + lowmomentrange + "," + highmomentrange + ")";
	        }
	        for(int i = 0; i < tmpNumber;i++){
	        	int tmpno = i + 1;
	        	mainApp.ResultModel4data.add(new ResultModel4(tmpno, deflection[i],shearrange[i],momentrange[i]));
	        }
		}
	}
	
    /**
     * Validates the user input in the YoungModulus text field
     * @return true if the input is valid
     */
    private boolean YoungModulusIsInputValid() {
        String errorMessage = "";
        // error message for YoungModulus
        if (YoungModulus.getText().matches("-\\d+.\\d+") || YoungModulus.getText().matches("-\\d+")){
        	errorMessage += "Negative figures not valid for YoungModulus Text Filed!\n"; 
        	//though it would return true for numbers larger than an int
        }
        else if (! YoungModulus.getText().matches("\\d+.\\d+") && ! YoungModulus.getText().matches("\\d+")){
        	errorMessage += "Characters are not valid for YoungModulus Text Filed!\n"; 
        	//though it would return true for numbers larger than an int
        }
        else if(Double.parseDouble(YoungModulus.getText()) <= 0){
        	errorMessage += "YoungModulus must be larger than 0!\n"; 
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
    
    
	/**
	 * Handle distance Input changed
	 */
	@FXML
	private void handleDistanceInput() {
		
//		System.out.println("This is distance display");
//		System.out.println("DistanceFromZero");
//		System.out.println(DistanceFromZero);
//		double DistanceRest = 0;
//		int PointedSpanNumberBase = 0;
//		double PointedSpanLength = 0;
//		int count = 0;
//		for (SpanModel SpanModelInstance : this.mainApp.getSpanModelData()){
//			DistanceFromZero = DistanceFromZero - SpanModelInstance.getlength();			 
//			if(DistanceFromZero < 0){
//				PointedSpanNumberBase = SpanModelInstance.getSpanNumber()-1;	
//				PointedSpanLength = SpanModelInstance.getlength();
//				if(count==0)
//					DistanceRest = DistanceFromZero + PointedSpanLength; // if in the range of the first span
//				break;
//			}								
//			DistanceRest = DistanceFromZero;
//			count++;
//		}
//		
//		beamResultIndex = (int) Math.round(DistanceRest*solver.NumSegs/PointedSpanLength + PointedSpanNumberBase*solver.NumSegs) - 1;
//		
//		System.out.println("DistanceRest");
//		System.out.println(DistanceRest);
//		System.out.println("PointedSpanLength");
//		System.out.println(PointedSpanLength);
//		System.out.println("beamResultIndex");
//		System.out.println(beamResultIndex);
//		
//		DecimalFormat df = new DecimalFormat("#.##");     
//---------------------------------------------------------------for debugging--------		
//		System.out.println("DistanceFromZero");
//		System.out.println(DistanceFromZero);
//		System.out.println();
//		
//	   System.out.println("distanceFromLeft");
//	   for(int k = 0; k < solver.distanceFromLeft.length;k++){
//		   System.out.print(solver.distanceFromLeft[k] + "\t");					   		   
//	   }
//	   System.out.println("distanceFromLeft");
//-------------------------------------------------------------------------
		String DFZ = distance.getText();
		if(DFZ == null ||(!DFZ.matches("[-+]?\\d*\\.?\\d+"))){
			// not digit or empty
            // Show the error message.
        	Alert alert = new Alert(AlertType.WARNING);
			
			alert.setTitle("Input not valid");
			alert.setHeaderText("Input not valid");
			alert.setContentText("Please give a valid number.");
			
			alert.showAndWait();
		}
		else{

			double DistanceFromZero = Double.parseDouble(distance.getText()); 
			if(DistanceFromZero<solver.distanceFromLeft[solver.distanceFromLeft.length-1]&&DistanceFromZero>=0){

				int beamResultIndex = 0;
				for(int i = 0;i<solver.distanceFromLeft.length;i++){
					
					if(DistanceFromZero==solver.distanceFromLeft[i]){
						beamResultIndex = i;
						break;
					}
					
					if(solver.distanceFromLeft[i]>DistanceFromZero){
						if(Math.abs(solver.distanceFromLeft[i]-DistanceFromZero) < Math.abs(DistanceFromZero-solver.distanceFromLeft[i-1])){
							beamResultIndex = i;
						}else{
							beamResultIndex = i-1;
						}
						break;
					}
				}
				
				if(beamResultIndex == 0){
					deflection.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][6])));
					shear.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][2])));
					moment.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][3])));
					
					deflection1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][6])));
					shear1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][2])));
					moment1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][3])));			
				}
				else{
					if(solver.beamResult[beamResultIndex][0] != solver.beamResult[beamResultIndex+1][0] )
					{		
						deflection.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][6])));
						shear.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][2])));
						moment.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][3])));
						
						deflection1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex+1][6])));
						shear1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex+1][2])));
						moment1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex+1][3])));
					}
					else if (solver.beamResult[beamResultIndex][0] != solver.beamResult[beamResultIndex-1][0]){
						
						deflection.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex-1][6])));
						shear.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex-1][2])));
						moment.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex-1][3])));
						
						deflection1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][6])));
						shear1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][2])));
						moment1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][3])));
						
					}
					else{
						deflection.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][6])));
						shear.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][2])));
						moment.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][3])));
						
						deflection1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][6])));
						shear1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][2])));
						moment1.setText(Double.toString(Double.valueOf(solver.beamResult[beamResultIndex][3])));
						
					}
				}
			}
			else{
				// not digit or empty
	            // Show the error message.
	        	Alert alert = new Alert(AlertType.WARNING);
				
				alert.setTitle("Input not valid");
				alert.setHeaderText("Input not valid");
				alert.setContentText("Please give a valid number in range.");
				
				alert.showAndWait();
			}
		}
	
	}
	
	/**
	 * Called when the user clicks About
	 */
	@FXML
	private void handleAbout() {
        boolean okClicked = mainApp.showAboutUI();

	}
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        GeometryTable.setItems(mainApp.getSpanModelData());  
        LoadTable.setItems(mainApp.getLoadModelData());
        
        
    }
}

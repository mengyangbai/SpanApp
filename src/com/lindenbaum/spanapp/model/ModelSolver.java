package com.lindenbaum.spanapp.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

public class ModelSolver {
	
	public class NodeRestrainCell{
		Double double_data;
		public char char_data;
		
		public NodeRestrainCell() {
			double_data = 0.0;
			char_data = '\u0000';
		}
		
		public NodeRestrainCell(Double double_input) {
			double_data = double_input;
			char_data = '\u0000'; // null for char
		}
		
		public NodeRestrainCell(char char_input) {
			if((char_input == 'F') || (char_input == 'B')) {
				char_data = char_input;
				double_data = null;
			} else {
				System.exit(0);
			}		
		}
		
		void setChar (char char_input) {
			char_data = char_input;
		}
		
		void setDouble (double double_input) {
			double_data = double_input;
		}
	}
	
	/*input*/
	public double[][] propertyTypes;
	public double[][] nodeCoordinates;
	public int[][] beamCon;
	double[][] beamEndRelease; //not initialized
	public double[][] distLoads;
	public double[][] pointLoads;
	public double[][] moments;
	double[][] DefacA;
	public NodeRestrainCell[][] nodeRestrain;
	int NumBeams;
	public int NumSegs;
	int NumNodes;
	int NumRest;
	int NumER;
	int NumERBeams;
	int NumRows; // for Range2Array()
	int NumCols; // for Range2Arrau()
	int Num3_global; // for combineArray()
	double[][] KB; // for formKL() 
	
	/*output*/ 
	public double[][] nodeResult; //sheet output 
	public double[][] beamEndResult; //sheet output
	public double[][] beamResult; //sheet output
	public double[] distanceFromLeft;
	
	/*
	 * Check global error estimation
	 */	
	double[][] equilibriumCheck;
	double[] resforce;
	double[] resOut;
	double[] outErr;
	
	
	public void initialise(ObservableList<SpanModel> SpanModelList,ObservableList<LoadModel> LoadModelList, double YoungModulus, NodeModel InitialNodeModel) {
		
		NumSegs = 100;
		NumER = 0;
		NumERBeams = 0;
		
		// For propertyTypes initialization
		List<Double> inertiaArrayList = new ArrayList<Double>();
		int count = 0;
		//*************** declaring space**************//
		nodeCoordinates = new double [SpanModelList.size()+1][2];
		nodeRestrain = new NodeRestrainCell [SpanModelList.size()+1][4];
		pointLoads = new double [LoadModelList.size()][2];
		distLoads = new double [LoadModelList.size()][6];
		moments = new double [LoadModelList.size()][2];
		beamCon = new int [SpanModelList.size()][3];
//*******************************************************************//		
		// initial nodeRestrain
		nodeRestrain[0][0] = new NodeRestrainCell(InitialNodeModel.getNodeNumber().doubleValue());
		
		if(InitialNodeModel.getSupportX().booleanValue()){
			nodeRestrain[0][1] = new NodeRestrainCell('F');
		} else{
			nodeRestrain[0][1] = new NodeRestrainCell('B');
		}
		
		if(InitialNodeModel.getSupportY().booleanValue()){
			nodeRestrain[0][2] = new NodeRestrainCell('F');
		} else{
			nodeRestrain[0][2] = new NodeRestrainCell('B');
		}
		
		if(InitialNodeModel.getSupportM().booleanValue()){
			nodeRestrain[0][3] = new NodeRestrainCell('F');
		} else{
			nodeRestrain[0][3] = new NodeRestrainCell('B');
		}	
		
		//initial nodeCoordinates
		nodeCoordinates[0][0] = InitialNodeModel.getXCoordinate();
		nodeCoordinates[0][1] = 0.0;
		
		for(SpanModel SpanModelInstance : SpanModelList){
			
			//constructing inertiaArrayList
			if(!inertiaArrayList.contains(SpanModelInstance.getInertia())){
				inertiaArrayList.add(SpanModelInstance.getInertia());}	
			
			// Constructing nodeCoordinates
			nodeCoordinates[count+1][0] = SpanModelInstance.getNodeModel().getXCoordinate();
			nodeCoordinates[count+1][1] = 0.0;	
					
			//constructing nodeRestrain			
			nodeRestrain[count+1][0] = new NodeRestrainCell(SpanModelInstance.getNodeModel().getNodeNumber().doubleValue());
			
			if(SpanModelInstance.getNodeModel().getSupportX().booleanValue()){
				nodeRestrain[count+1][1] = new NodeRestrainCell('F');
			} else{
				nodeRestrain[count+1][1] = new NodeRestrainCell('B');
			}
			
			if(SpanModelInstance.getNodeModel().getSupportY().booleanValue()){
				nodeRestrain[count+1][2] = new NodeRestrainCell('F');
			} else{
				nodeRestrain[count+1][2] = new NodeRestrainCell('B');
			}
			
			if(SpanModelInstance.getNodeModel().getSupportM().booleanValue()){
				nodeRestrain[count+1][3] = new NodeRestrainCell('F');
			} else{
				nodeRestrain[count+1][3] = new NodeRestrainCell('B');
			}
			
			count++;	
		}
		
		//reset count
		count = 0;
		for(LoadModel LoadModelInstance : LoadModelList){
			
			//constructing pointLoads, distloads, moments
			/*for debugging purpose, this should be forever added*/
			distLoads[count][0] = LoadModelInstance.getSpanNumber();
			distLoads[count][1] = 1.0; // Y axis in default
			
			if(LoadModelInstance.getType()!=""){
				
				
				//constructing pointLoads
				if(LoadModelInstance.getType()=="PLoad"){
					
					pointLoads[count][0] = LoadModelInstance.getPLoadPosition();
					pointLoads[count][1] = LoadModelInstance.getPLoadValue();
				} 
				//constructing distloads
				else if(LoadModelInstance.getType()=="DLoad"){
					
					distLoads[count][0] = LoadModelInstance.getSpanNumber();
					distLoads[count][2] = 0.0;
					distLoads[count][3] = LoadModelInstance.getDLoadStart();
					distLoads[count][4] = LoadModelInstance.getDLoadEnd();
					distLoads[count][5] = 0.0; 					
				}
				//constructing moments
				else if(LoadModelInstance.getType()=="Mom"){					
					moments[count][0] = LoadModelInstance.getMomPosition();
					moments[count][1] = LoadModelInstance.getMomValue();	
				}
			}
			
			count++;	
		}
		//*************** declaring space**************//
		propertyTypes = new double [inertiaArrayList.size()][5];
//*******************************************************************//			
		// Constructing propertyTypes 
		for(int i=0; i< inertiaArrayList.size(); i++){
			propertyTypes[i][0] = Math.pow(inertiaArrayList.get(i)*12 , 0.25);
			propertyTypes[i][1] = inertiaArrayList.get(i);
			propertyTypes[i][2] = YoungModulus;
			propertyTypes[i][3] = 0;
			propertyTypes[i][4] = YoungModulus/1.5;
		}
		//reset count
		count = 0; 
		//Constructing beam connections
		for(SpanModel SpanModelInstance : SpanModelList){
			for(int i=0; i< inertiaArrayList.size(); i++){
				if(inertiaArrayList.get(i).doubleValue() == SpanModelInstance.getInertia().doubleValue()){
					beamCon[count][0] = i + 1;  // or i
					break;
				}
			}
			beamCon[count][1] = SpanModelInstance.getSpanNumber();	
			beamCon[count][2] = SpanModelInstance.getSpanNumber()+1;		
			count++;		
		}
		
		NumBeams = beamCon.length;
		NumNodes = nodeCoordinates.length;
		NumRest = nodeRestrain.length;

		KB = new double[6][6];
		nodeResult = new double[NumNodes*3][2];
		beamResult = new double[(NumSegs+1)*NumBeams][8];
		distanceFromLeft = new double[(NumSegs+1)*NumBeams];
		
	   System.out.println("beamCon");
	   for(int i =0; i < beamCon.length;i++){
		   for(int j =0; j < 3;j++){
				   System.out.print(beamCon[i][j] + "\t");					   		   
		   }  
		   System.out.println();
	   }
	   
	   System.out.println("nodeCoordinates");
	   for(int i =0; i < nodeCoordinates.length;i++){
		   for(int j =0; j < 2;j++){
				   System.out.print(nodeCoordinates[i][j] + "\t");					   		   
		   }  
		   System.out.println();
	   }
	   
	   System.out.println("NodeRestrain");
	   for(int i =0; i < nodeRestrain.length;i++){
		   for(int j =0; j < 4;j++){
				   System.out.print(nodeRestrain[i][j].char_data + "\t");					   		   
		   }   
		   System.out.println();
	   }
	   
	   System.out.println("propertyTypes");
	   for(int i =0; i < propertyTypes.length;i++){
		   for(int j =0; j < 5;j++){
				   System.out.print(propertyTypes[i][j] + "\t");					   		   
		   }   
		   System.out.println();
	   }
	   
	   System.out.println("distLoads");
	   for(int i =0; i < distLoads.length;i++){
		   for(int j =0; j < 6;j++){
				   System.out.print(distLoads[i][j] + "\t");					   		   
		   }   
		   System.out.println();
	   }
	   
	   System.out.println("pointLoads");
	   for(int i =0; i < pointLoads.length;i++){
		   for(int j =0; j < 2;j++){
				   System.out.print(pointLoads[i][j] + "\t");					   		   
		   }   
		   System.out.println();
	   }
	   
	   System.out.println("moments");
	   for(int i =0; i < moments.length;i++){
		   for(int j =0; j < 2;j++){
				   System.out.print(moments[i][j] + "\t");					   		   
		   }   
		   System.out.println();
	   }
		
	}
	
    public void run(){
    	formKg();
    	
 	   for(int i = 0; i < nodeResult.length;i++){
		   for(int j = 0; j < 2;j++){
			   nodeResult[i][j] = roundToSignificantFigure(nodeResult[i][j],4);		   		   
		   }   
	   }
 	   
 	  for(int i = 0; i < beamResult.length;i++){
		   for(int j = 0; j < 8;j++){
			   beamResult[i][j] = roundToSignificantFigure(beamResult[i][j],4);		   		   
		   }   
	   }
 	  
 	  for(int i = 0; i < beamEndResult.length;i++){
		   for(int j = 0; j < 3;j++){
			   beamEndResult[i][j] = roundToSignificantFigure(beamEndResult[i][j],4);		   		   
		   }   
	   }
 	  
 	  for(int i = 0; i < distanceFromLeft.length;i++){
 		 distanceFromLeft[i] = roundToSignificantFigure(distanceFromLeft[i],4);	
 	  }
    }
	  
	public ModelSolver() { }
	
    protected static int sgn(double x) {
	    if(x > 0) {
		    return 1;
	    } else if(x == 0) {
		    return 0;
	    } else {
		    return -1;
	    }
    }   
    
    static double roundToSignificantFigure(double num, int precision){
        return new BigDecimal(num)
            .round(new MathContext(precision, RoundingMode.HALF_EVEN))
            .doubleValue(); 
    }

      
    public double[][] FEMact(int[][] Beams, double[][] Nodes, double[][] DistLoads, double[][] PointLoads,double[][] Moments) {
	    int i,j,n, Numloads,RowNum,NumBeams,lastk,NumRows,Node1,Node2;
	    double Alpha, Beta,Theta,TLoad1,Tload2,L,DX,DY;
	    double[][] FEMA;
	    double[] MomInc = new double[2]; 
	    double[] LoadInc = new double[2];
	    double LoadAxis;

	    /*
	     * VBA Range Functions.
	     * if TypeName(Beams) = "Range" Then Beams = Beams.Value2
	     * if TypeName(Nodes) = "Range" Then Nodes = Nodes.Value2
	     * if TypeName(DistLoads) = "Range" Then DistLoads = DistLoads.Value2
	     * if TypeName(PointLoads) = "Range" Then PointLoads = PointLoads.Value2
	     * if TypeName(Moments) = "Range" Then Moments = Moments.Value2
	     * */
	    NumBeams = Beams.length;
	    NumRows = DistLoads.length; // not sure about this
	    FEMA = new double[NumBeams][6];	   
 
	    for(RowNum = 0; RowNum < NumRows; RowNum++) { //there is difference between java and VBAscript -> n-1 for in this loop since n is not a iterator
	  	    n = (int) DistLoads[RowNum][0];
		    LoadAxis = DistLoads[RowNum][1]; //x=0.0 y=1.0
		    Node1 = Beams[n-1][1]; //d8
		    Node2 = Beams[n-1][2]; //d8
		    DX = Nodes[Node2-1][0] - Nodes[Node1-1][0]; //d8
		    DY = Nodes[Node2-1][1] - Nodes[Node1-1][1]; //d8
		    L = Math.pow((DX*DX + DY*DY), 0.5);
		    Theta = Math.atan2(DY, DX); //reversed
		    i=RowNum;
		    Alpha = DistLoads[i][2]/L; //d8
            Beta = DistLoads[i][5]/L; //d8
		    TLoad1 = DistLoads[i][3]*L*(1-Alpha-Beta); //d8

		    if((DistLoads[i][3] != 0.0) || (DistLoads[i][4] != 0.0)) { //d8
		         if(DistLoads[i][3] == DistLoads[i][4]) { //d8
		        	 MomInc[0] = TLoad1 * L * (1.0 / 12 + (Math.pow(Alpha, 3)) / 4
		                                      - (Math.pow(Alpha, 2)) * 5 / 12 + Alpha / 12 - (Math.pow(Alpha, 2) * Beta) / 4
		                                      + Alpha * Beta / 6 + (Alpha * Math.pow(Beta, 2)) / 4 + Beta / 12
		                                      + Math.pow(Beta, 2) / 12 - (Math.pow(Beta, 3)) / 4); //d8
		             if(Alpha == Beta) {
		                 MomInc[1] = -MomInc[0];
		             } else {
		                 MomInc[1] = -TLoad1 * L * (1.0/12 + Math.pow(Beta, 3)/ 4
		                                           - Math.pow(Beta, 2) * 5 / 12 + Beta / 12 - (Math.pow(Beta, 2)* Alpha) / 4
		                                           + Beta * Alpha / 6 + (Beta * Math.pow(Alpha, 2)) / 4 + Alpha / 12
		                                           + Math.pow(Alpha, 2) / 12 - Math.pow(Alpha, 3) / 4); //d8
		             }
		         } else {
		             TLoad1 = TLoad1 / 2;
		             Tload2 = L * (1 - Alpha - Beta) * DistLoads[i][4] / 2; //d8
		             MomInc[0] = TLoad1 * L * (1.0/10 + Math.pow(Alpha, 3) * 2 / 5
		                                      - (Math.pow(Alpha, 2)) * 7 / 10 + Alpha / 5 - (Math.pow(Alpha, 2) * Beta) * 3 / 10
		                                      + Alpha * Beta * 4 / 15 + (Alpha * Math.pow(Beta, 2)) / 5
		                                      + Beta / 30 - Math.pow(Beta, 2) / 30 - (Math.pow(Beta, 3)) / 10); //d8		            
		             MomInc[1] = -TLoad1 * L * (1.0/15 - (Math.pow(Alpha, 3)) * 2 / 5
		                                       + (Math.pow(Alpha, 2)) / 5 + Alpha * 2 / 15 + (Math.pow(Alpha, 2) * Beta) * 3 / 10
		                                       + Alpha * Beta * 1 / 15 - (Alpha * Math.pow(Beta, 2)) / 5 - Beta / 30
		                                       - Math.pow(Beta, 2) * 2 / 15 + (Math.pow(Beta, 3)) / 10); //d8		              
		             MomInc[1] = MomInc[1] - Tload2 * L * (1.0/10 + Math.pow(Beta, 3) * 2 / 5
		                                       - Math.pow(Beta, 2) * 7 / 10 + Beta / 5 - (Math.pow(Beta, 2) * Alpha) * 3 / 10
		                                       + Beta * Alpha * 4 / 15 + (Beta * Math.pow(Alpha, 2)) / 5 + Alpha / 30
		                                       - Math.pow(Alpha, 2) / 30 - Math.pow(Alpha, 3) / 10); //d8
		             MomInc[0] = MomInc[0] + Tload2 * L * (1.0/15 - Math.pow(Beta, 3) * 2 / 5
		            							          + Math.pow(Beta, 2) / 5 + Beta * 2 / 15 + (Math.pow(Beta, 2) * Alpha) * 3 / 10
		                                                  + Beta * Alpha * 1 / 15 - (Beta * Math.pow(Alpha, 2)) / 5 - Alpha / 30
		                                                  - Math.pow(Alpha, 2) * 2 / 15 + Math.pow(Alpha, 3) / 10); //d8
		             }
			    if(LoadAxis == 1.0) {
			        if(Theta != 0) {
			            MomInc[0] = MomInc[0] * Math.cos(Theta); //d8
			            MomInc[1] = MomInc[1] * Math.cos(Theta); //d8
			        }
			    } else {
			        MomInc[0] = MomInc[0] * -Math.sin(Theta); //d8
			        MomInc[1] = MomInc[1] * -Math.sin(Theta); //d8
			    }
			    FEMA[n-1][2] = MomInc[0] + FEMA[n-1][2]; //d8
			    FEMA[n-1][5] = MomInc[1] + FEMA[n-1][5]; //d8
		    }

		    /*
		     * Fixed end moments due to point loads
			 *
	         * for i = lastk + 1 To lastk + NumLoads
	         * */
	         if(PointLoads[i][1] != 0) { //d8
	             Alpha = PointLoads[i][0] / L; //d8
	             MomInc[0] = PointLoads[i][1] * Alpha * Math.pow(1-Alpha,2) * L; //d8
	             MomInc[1] = -PointLoads[i][1] * Math.pow(Alpha, 2) * (1 - Alpha) * L; //d8
	             if(LoadAxis == 1.0) {
	                 FEMA[n-1][2] = MomInc[0] * Math.cos(Theta) + FEMA[n-1][2]; //d8
	                 FEMA[n-1][5] = MomInc[1] * Math.cos(Theta) + FEMA[n-1][5]; //d8
	             } else {
	                 FEMA[n-1][2] = -MomInc[0] * Math.sin(Theta) + FEMA[n-1][2]; //d8
	                 FEMA[n-1][5] = -MomInc[1] * Math.sin(Theta) + FEMA[n-1][5]; //d8
	             }
	         }

	         /*
	          * Fixed end moments and end reactions due to point moments
			  * */
	         if(Moments[i][1] != 0) { //d8
	             Alpha = Moments[i][0] / L;//d8
	             MomInc[0] = Moments[i][1] * (3 * Alpha - 1) * (Alpha - 1); //d8
	             MomInc[1] = -Moments[i][1] * (2 - 3 * Alpha) * Alpha; //d8
	             FEMA[n-1][2] = MomInc[0] + FEMA[n-1][2]; //d8
	             FEMA[n-1][5] = MomInc[1] + FEMA[n-1][5]; //d8
	             LoadInc[0] = -Moments[i][1] / L; //d8
	             LoadInc[1] = Moments[i][1] / L; //d8
	             FEMA[n-1][0] = FEMA[n-1][0] - LoadInc[0] * Math.sin(Theta); //d8
	             FEMA[n-1][3] = FEMA[n-1][3] - LoadInc[1] * Math.sin(Theta); //d8
	             FEMA[n-1][1] = FEMA[n-1][1] + LoadInc[0] * Math.cos(Theta); //d8
	             FEMA[n-1][4] = FEMA[n-1][4] + LoadInc[1] * Math.cos(Theta); //d8
	         }	        

	         /*
	          * End reactions due to distributed loads
			  * */
	         if((DistLoads[i][3] != 0) || (DistLoads[i][4] != 0)) { //d8
	             Alpha = DistLoads[i][2]/ L; //d8
	             Beta = DistLoads[i][5] / L; //d8
	             TLoad1 = DistLoads[i][3] * L * (1 - Alpha - Beta); //d8
 
	             if(DistLoads[i][3] == DistLoads[i][4]) { //d8
	                 LoadInc[0] = TLoad1 * (1 - Alpha + Beta) / 2; //d8
	                 LoadInc[1] = TLoad1 - LoadInc[0]; //d8
	             } else {
	                 TLoad1 = TLoad1 / 2;
	                 Tload2 = L * (1 - Alpha - Beta) * DistLoads[i][4] / 2; //d8
	                 LoadInc[0] = TLoad1 * (2 - 2 * Alpha + Beta) / 3; //d8
	                 LoadInc[0] = LoadInc[0] + Tload2 * (1 + 2 * Beta - Alpha) / 3; //d8
	                 LoadInc[1] = (TLoad1 + Tload2) - LoadInc[0]; //d8
	             }

	             if(LoadAxis == 0.0) {
	             	 j = 1;
	             } else { 
	            	 j = 2;
	             }	          

	             FEMA[n-1][j-1] = LoadInc[0] + FEMA[n-1][j-1]; //d8 j-1 because j is not iterator here
	             FEMA[n-1][j+3-1] = LoadInc[1] + FEMA[n-1][j+3-1]; //d8
	         }
	        
	         /*
	          * End reactions due to point loads
	          * */	        
	         if(PointLoads[i][1] != 0) { //d8
	             Alpha = PointLoads[i][0] / L; //d8
	            
	             if(LoadAxis == 0.0) { //d8
	            	 j = 1; 
	             } else {
	            	 j = 2;
	             } //d8

	             LoadInc[0] = PointLoads[i][1] * (1 - Alpha); //d8
	             LoadInc[1] = PointLoads[i][1] - LoadInc[0]; //d8
	             FEMA[n-1][j-1] = LoadInc[0] + FEMA[n-1][j-1]; //d8 j-1 because j is not iterator here
        		 FEMA[n-1][j+3-1] = LoadInc[1] + FEMA[n-1][j+3-1]; //d8 j-1 because j is not iterator here
	         }
	    }

	    /*
	     * Correct end reactions for resultant end moments
	     * */
	    for(n=0; n< NumBeams; n++) {
	    	Node1 = Beams[n][1]; //d8
	        Node2 = Beams[n][2]; //d8
	        DX = Nodes[Node2-1][0] - Nodes[Node1-1][0]; //d8
	        DY = Nodes[Node2-1][1] - Nodes[Node1-1][1]; //d8
	        L = Math.pow((Math.pow(DX, 2) + Math.pow(DY, 2)),0.5);
	        Theta = Math.atan2(DY,DX); //reversed	        

	        if(Math.sin(Theta) != 0) {
	            FEMA[n][0] = FEMA[n][0] - (FEMA[n][2] + FEMA[n][5]) * Math.sin(Theta) / L; //d8
	            FEMA[n][3] = FEMA[n][3] + (FEMA[n][2] + FEMA[n][5]) * Math.sin(Theta) / L; //d8
	        }
	        if(Math.cos(Theta) != 0) {
	            FEMA[n][1] = FEMA[n][1] + (FEMA[n][2] + FEMA[n][5]) * Math.cos(Theta) / L; //d8
	            FEMA[n][4] = FEMA[n][4] - (FEMA[n][2] + FEMA[n][5]) * Math.cos(Theta) / L; //d8
	        }
	    }
	    return FEMA;
    }

    int formKL(double[] k, double[][] KB) { 

	    // Note! ByRef passing the address
	    double Csq, Ssq, CS;
	    int i,j;
	    
	    //ReDim!
	    Csq = Math.pow(k[4], 2); //d8
	    Ssq = Math.pow(k[5], 2); //d8
	    CS = k[4]*k[5]; //d8

	    this.KB[0][0] = Csq * k[0] + Ssq * k[1]; //d8
	    this.KB[0][1] = CS * (k[0] - k[1]); //d8
	    this.KB[0][2] = -k[5] * k[3]; //d8
	    this.KB[0][3] = -this.KB[0][0]; //d8
	    this.KB[0][4] = -this.KB[0][1]; //d8
	    this.KB[0][5] = this.KB[0][2]; //d8
	    this.KB[1][1] = Ssq * k[0] + Csq * k[1];
		this.KB[1][2] = k[4] * k[3];
		this.KB[1][3] = this.KB[0][4];
		this.KB[1][4] = -this.KB[1][1];
		this.KB[1][5] = this.KB[1][2];
		this.KB[2][2] = 4 * k[2];
		this.KB[2][3]  = -this.KB[0][2];
		this.KB[2][4]  = -this.KB[1][5];
		this.KB[2][5]  = 2 * k[6];		
		this.KB[3][3] = this.KB[0][0];
		this.KB[3][4] = this.KB[0][1];
		this.KB[3][5] = -this.KB[0][5];		
		this.KB[4][4] = this.KB[1][1];
		this.KB[4][5] = -this.KB[1][5];		
		this.KB[5][5] = this.KB[2][2];
		
		i = 1;
	    this.KB[i][0] = this.KB[0][i];

	    i = 2;
	    for(j = 0; j < 2; j++) {
	        this.KB[i][j] = this.KB[j][i];
	    }

	    i = 3;
	    for(j = 0; j < 3; j++) {
	        this.KB[i][j] = this.KB[j][i];
	    }

	    i = 4;
	    for(j = 0; j < 4; j++) {
	    	this.KB[i][j] = this.KB[j][i];
	    }

	    i = 5;
	    for(j = 0; j < 5; j++) {
	    	this.KB[i][j] = this.KB[j][i];
	    }
        return 0;
    }

    double[][] LinBeam(int BeamNum, int OutPoints, Double[][] OutCols /*optional*//*, double[][] NCoord, double[][] BeamProps, int[][] DistLoads/**newly added arguments*/) {

	    double[][] BeamCoords, BeamSect, EndDefA, PLoads, tempDLoads;
	    double[] React;
	    double BeamLen, BeamAng, BeamCos, BeamSin;
	    int NumDloads = 0, NumPloads = 0, NumMoms = 0, Node1, Node2, PropNum, i, j = 0, k = 0, M;

	    BeamCoords = new double[2][2];
	    BeamSect = new double[1][3];
	    EndDefA = new double[2][4];

	    /* 
	     * BeamProps = Range("beamprop").Value2
	     * BeamCon = Range("beamcon").Value2
	     * NCoord = Range("coords").Value2
	     * DistLoads = Range("distloads").Value2
	     * PointLoads = Range("pointloads").Value2
	     * Moments = Range("moments").Value2
	     * Nodedef = Range("defc").Value2
	     * React = Range("react").Value2
	     * BeamAct = Range("beam_act").Value2
	     * */
	     PropNum = beamCon[BeamNum][0]; //d8 //BeamNum instead of BeamNum-1 due to BeamNum being alias of iterator 
	     Node1 = beamCon[BeamNum][1]; //d8 //BeamNum instead of BeamNum-1 due to BeamNum being alias of iterator
	     Node2 = beamCon[BeamNum][2]; //d8 //BeamNum instead of BeamNum-1 due to BeamNum being alias of iterator

	     BeamCoords[0][0] = nodeCoordinates[Node1-1][0]; //d8
	     BeamCoords[0][1] = nodeCoordinates[Node1-1][1]; //d8
	     BeamCoords[1][0] = nodeCoordinates[Node2-1][0]; //d8
	     BeamCoords[1][1] = nodeCoordinates[Node2-1][1]; //d8
	     BeamLen = Math.pow((Math.pow((BeamCoords[1][0] - BeamCoords[0][0]),2)+ Math.pow((BeamCoords[1][1] - BeamCoords[0][1]),2)),0.5); //d8
	     BeamAng = Math.atan2((BeamCoords[1][1] - BeamCoords[0][1]),(BeamCoords[1][0] - BeamCoords[0][0])); //d8 //reversed
	     BeamCos = Math.cos(BeamAng);
	     BeamSin = Math.sin(BeamAng);

	     /* EI  GA  EA
	      * Property Number, Area, I, Young's Modulus, Shear Area, Shear Modulus
	      * */
	     BeamSect[0][0] = propertyTypes[PropNum-1][1] * propertyTypes[PropNum-1][2]; //d8
	     BeamSect[0][1] = propertyTypes[PropNum-1][3]  * propertyTypes[PropNum-1][4]; //d8
	     BeamSect[0][2] = propertyTypes[PropNum-1][0]  * propertyTypes[PropNum-1][2]; //d8

	     /*
	      * Count number of Dloads, Ploads and moments
	      * */
	     for (i = 0; i < distLoads.length; i++) {
	    	 if ((BeamNum+1) == distLoads[i][0]) { //d8 // BeamNum +1 instead of BeamNum so that it can reach bound value
	    		 if (distLoads[i][1] != '\u0000') {
	    			 NumDloads = NumDloads + 1;
	    		 } // changed to '\u0000' instead of ''
	             if(pointLoads[i][0] != 0) {
	            	 NumPloads = NumPloads + 1;
	             } // changed to 0 instead of ''
	             if(moments[i][0]!= 0) {
	            	 NumMoms = NumMoms + 1;
	             } // changed to 0 instead of ''
	         }
	     }

	    tempDLoads = new double[distLoads.length][5];
		PLoads = new double[NumPloads + NumMoms + 2][4];	    

	    for(i = 0; i < distLoads.length; i++) { // k & j instead of k+1 and j+1 here 
	        if((BeamNum+1) == distLoads[i][0]) { // BeamNum +1 instead of BeamNum so that it can reach bound value	                
	            tempDLoads[j][4] = distLoads[i][1]; // changed to loadAxis instead of Dloads
	            if(NumDloads >= j) {
	                tempDLoads[j][0]= distLoads[i][2]; //d8
	                tempDLoads[j][1] = BeamLen - distLoads[i][5]; //d8
	                tempDLoads[j][2] = distLoads[i][3]; //d8
	                tempDLoads[j][3] = distLoads[i][4];//d8	               
	            }
		        if (pointLoads[i][0] != 0) { //d8 // changed to 0 instead of ''
		            k = k + 1;
		            PLoads[k][0] = pointLoads[i][0]; //d8 
		            if (tempDLoads[j][4] == 0.0) {
		            	PLoads[k][1] = pointLoads[i][1];
		            } //d8
		            if (tempDLoads[j][4] == 1.0) {
		            	PLoads[k][2] = pointLoads[i][1];
		            } //d8
		        }
		        if(moments[i][0] != 0) { //d8 // changed to 0 instead of ''	 
		            k = k + 1;
		            PLoads[k][0] = moments[i][0];
		            PLoads[k][3] = -1*moments[i][1];
		        }
		        j = j + 1;

	        /* if Moments(i, 1) <> "" Then NumMoms = NumMoms + 1 */
	        }
	    }

        /* add end forces and End Deflections */
	    j = (Node1 - 1) * 3 + 1;
	    M = BeamNum * 2 + 1; // BeamNum*2 instead of (BeamNum-1)*2+1 due to BeamNum being alias of iterator
	    PLoads[0][0] = 0; //d8
	    PLoads[0][1]  = -1 * (beamEndResult[M-1][1] * BeamCos + beamEndResult[M-1][0]  * BeamSin); //d8
	    PLoads[0][2]  = beamEndResult[M-1][0] * BeamCos - beamEndResult[M-1][1]  * BeamSin; //d8
	    PLoads[0][3]  = beamEndResult[M-1][2]; //d8
	    EndDefA[0][0] = 0; //d8
	    EndDefA[0][1]  = nodeResult[j-1][0]; //d8
	    EndDefA[0][2]  = nodeResult[j][0]; //d8
	    EndDefA[0][3]  = nodeResult[j+1][0]; //d8

	    j = (Node2 - 1) * 3 + 1;  /* j+3 */
	    k = k + 2;
	    M = M + 1;
	    
	    PLoads[k-1][0] = BeamLen;
	    PLoads[k-1][1] = beamEndResult[M-1][1]* BeamCos + beamEndResult[M-1][0] * BeamSin; //d8
	    PLoads[k-1][2] = -beamEndResult[M-1][0] * BeamCos + beamEndResult[M-1][1] * BeamSin; //d8
	    PLoads[k-1][3] = beamEndResult[M-1][2]; //d8
	    EndDefA[1][0] = BeamLen; //d8
	    EndDefA[1][1] = nodeResult[j-1][0]; //d8
	    EndDefA[1][2] = nodeResult[j][0]; //d8
	    EndDefA[1][3] = nodeResult[j+1][0];//d8	    

	    return LinBeam2(BeamCoords, BeamSect, OutPoints, tempDLoads, PLoads, EndDefA, null, null, OutCols);
	} 

    double[][] LinBeam2(double[][] BeamCoords, double[][] BeamSect, int NumSegs, double[][] DLoads, /*Optional*/
    				double[][] PLoads, /*Optional*/ double[][] EndDefA,/*Optional*/ Double NumDloads, /*Optional*/
    				Double NumPloads, /*Optional*/ Double[][] OutCols/*Optional*/) {
    	
    	/*
    	 * Find deflected shape for beam under specified loads including resolution of applied loads
    	 * Beam length and slope specified by end coordinates
    	 * Results are output for each listed point in OutPoints
    	 * Out - Output index: 1 (default) - Shear, Moment, Slope and Deflection at each Outpoint; 2 - force and moment imbalance and deflection and rotation difference at ends
    	 * */
    	double[][] DLoads_temp,DLoads_local = null,PLoads_temp = null,PLoads_local,SSRes,SSRes2, SSRes3;

    	// double[][] PLoads2;  
    	// not functioning variable
    	int Numsegments, NumNodes, NumOut, i, j, k, PLoadCols = 0, S1Node, S2Node, EndCols, EndRestraints, NumRows = 0, NumCols = 0, NumBCols,
    			NumSegCols, NumOutCols, NumDloads_local = 0, NumPloads_local = 0;
    	double x, X0, X1, X2, LoadLen, TotLoad, TotMom, EI, Q, Qdash, QNeg, DX, DX1, DX2, W, StartSlope, EndSlope = 0, StartDef, SlopeChange, NewSlope,
    			ActSlope, XS1, XS2, Span, BeamLength, SegLength, S1Def, S2Def, S1Rforce, S2Rforce, DefChange, EndDef = 0, DY, GA, EA, BeamAng, BeamCos, BeamSin,
    			PTrans,PAx,QA,QAdash,QANeg,PA,Xinc,YInc,End2DT,End2DAx,End1DefAng,End1DRtn;
    	double[][] Segments2,SuppLoads;
    	double Tol = 0.00000000000001;
    	double[] OutPoints,Beam,Segments3,End1Defa;

	    /*
	     * if(TypeName(BeamCoords) == "Range") {
	     * BeamCoords = BeamCoords.Value2;
	     * }
	     * if( TypeName(BeamSect) == "Range") {
	     * BeamSect = BeamSect.Value2;
	     * }
	     * */

	    /* for debug process - start */
	    Beam = new double[1];
	    End1Defa = new double[2];
	    /* for debug process - end */	   

	    /* for optional argument OutCols - isMissing() = false*/

		if(OutCols != null) {
		    /* if TypeName(OutCols) = "Range" Then OutCols = OutCols.Value2 */
		    NumOutCols = OutCols[0].length; //d8
		} else {
		    NumOutCols = 8;
		}

	    NumBCols = BeamSect[0].length; //d8
	    DX = BeamCoords[1][0] - BeamCoords[0][0]; //d8
	    DY = BeamCoords[1][1] - BeamCoords[0][1]; //d8

	    BeamLength = Math.pow((Math.pow(DX, 2) + Math.pow(DY, 2)), 0.5);
	    Beam[0] = BeamLength; //d8
	    BeamAng = Math.atan2(DY, DX); //reversed

	    // if BeamAng <> 0 Then
	    BeamCos = Math.cos(BeamAng);
	    BeamSin = Math.sin(BeamAng);
	    // End if

	    /* for optional argument EndDefA - isMissing() = true */

	    if (EndDefA == null) {
	        EndCols = 1;
	    } else {
	        /* if TypeName(EndDefA) = "Range" Then EndDefA = EndDefA.Value2 */
	        EndCols = EndDefA[0].length; //d8
	    }

	    XS1 = 0;
	    XS2 = BeamLength;
	    Span = XS2 - XS1;

	    OutPoints = new double[NumSegs+1];
	    OutPoints[0] = 0; //d8?
	    SegLength = Span / NumSegs;
	    NumOut = NumSegs + 1;

	    for(i = 1; i < NumOut; i++) {
	        OutPoints[i] = OutPoints[i - 1] + SegLength; //d8?
	    }

	    /* for optional argument DLoads - isMissing() = false */
	    if (DLoads != null) {
	        DLoads_temp = Range2Array(DLoads, NumRows, NumCols, null , null , new Boolean(true), new Integer(2)); // 2 neglected input arguments MaxVal & MaxValCol replaced by '0' 

	        /* for optional argument NumDloads - isMissing() = true */
	        NumRows = this.NumRows;
	        NumCols = this.NumCols;
	        
	        if (NumDloads == null) { 
	        	NumDloads_local = NumRows;
	        }

	        DLoads_local = new double[NumRows][9];

	        for(int d1 = 0; d1 < DLoads_temp.length; d1++) { // copy value of DLoads_temp to DLoads_local
	        	for(int d2=0; d2 < DLoads_temp[0].length; d2++) {
	        		DLoads_local[d1][d2]=DLoads_temp[d1][d2];
	        	}
        	}
	        for(i = 0; i < NumRows; i++) {
	            if(DLoads_local[i][4] == 0.0) { 
	            	DLoads_local[i][5] = -DLoads_local[i][2] * BeamSin; // Trans start //d8
	            	DLoads_local[i][6] = -DLoads_local[i][3] * BeamSin; // Trans end //d8
	            	DLoads_local[i][7] = DLoads_local[i][2] * BeamCos; // Long start //d8
	            	DLoads_local[i][8] = DLoads_local[i][3] * BeamCos; // Long end //d8
	            } else {
	            	DLoads_local[i][5] = DLoads_local[i][2] * BeamCos; // Trans start //d8
	            	DLoads_local[i][6] = DLoads_local[i][3] * BeamCos; // Trans end //d8
	            	DLoads_local[i][7] = -DLoads_local[i][2] * BeamSin; // Long start //d8
	            	DLoads_local[i][8] = -DLoads_local[i][3] * BeamSin; // Long end //d8
	            }
	    	}  
	    } else {
	    	NumDloads_local = 0;
   		}

	    /* for optional argument PLoads - isMissing() = false */

	    if (PLoads != null) {
	    	PLoads_temp = Range2Array(PLoads, NumRows, PLoadCols, null , null , new Boolean(false), null); // 3 neglected input arguments MaxVal & MaxValCol & CheckCol replaced by '0'
	    	PLoadCols = this.NumCols; // added here to give PLoadsCols Value
	    	NumRows = this.NumRows;
	    	
	    	/* for optional argument NumPloads - isMissing() = true */		
	    	if (NumPloads == null) {
	    		NumPloads_local = NumRows;
	    	}

	    	for(i = 0; i < NumRows; i++) {
	    		if (BeamAng != 0) {
	    			PAx = -(PLoads_temp[i][1] * BeamCos + PLoads_temp[i][2]  * BeamSin); //d8
                    PTrans = PLoads_temp[i][2]  * BeamCos - PLoads_temp[i][1]  * BeamSin; //d8
	    		} else {
	    			PAx = -PLoads_temp[i][1]; //d8
	    			PTrans = PLoads_temp[i][2]; //d8
	    		}
	    		PLoads_temp[i][1]  = PTrans;    //'PTrans //d8
	    		PLoads_temp[i][2]  = PAx;    //' PAx //d8
	    	}
	    } else {
	    	NumPloads_local = 0;
	    }

	    // PLoads2 = new double[NumPloads_local][5];   
	    // this variable is not functioning 

	    // ***
	    // Insert additional nodes if required at segment ends and supports.

	    // Insert segment ends
	    Segments3 = CombineArray(Beam, OutPoints, Num3_global, null, null, new Boolean(true)); //combine array
	    NumNodes = Num3_global;
	    Numsegments = Num3_global - 1;
	    if(NumBCols < 2) {
	        SSRes = new double[NumNodes][8];
	        SSRes2 = new double[NumNodes][8];
	    } else {

	    	// Include shear slope and deflection and total slope and deflection
	        SSRes= new double[NumNodes][11];
	        SSRes2= new double[NumNodes][11];
	    }

	    S1Node = 1;
	    S2Node = NumNodes;
	    Segments2 = new double[NumNodes][NumBCols + 1];
	    
	    k = 1;
	    for(i = 0; i < NumNodes; i++) {
	        Segments2[i][0] = Segments3[i]; //d8
	        if(Segments2[i][0] != 0) {
	            if ((Segments2[i][0] - Beam[k-1]) / Segments2[i][0] > Tol) //d8
	            	k = k + 1;
	        }
	        Segments2[i][1] = BeamSect[k-1][0]; //d8
	        if (NumBCols > 1) {
	        	Segments2[i][2] = BeamSect[k-1][1];//d8
	        }
	        if (NumBCols > 2) {
	        	Segments2[i][3] = BeamSect[k-1][2];//d8	    	
	        }
	    }

	    /*
         * Solve assuming zero rotation at node 1
         * SSRes columns:                    SSRes2 columns:
         * 1: X                              1: Shear force
         * 2: Shear force                    2: Bending moment
         * 3: Bending moment                 3: Axial force
         * 4: Slope                          4: DX
         * 5: Deflection due to bending      5: DY
         * 6: Axial force                    6: RZ
         * 7: Axial deflection               7: D Trans
         * 8: Shear slope                    8: D Ax
         * 9: Shear deflection
	     * */

	    SSRes[0][3] = EndDefA[0][3];
	    SSRes[0][4] = 0;
	    SSRes[0][6] = 0;
	    if(PLoads_temp[0][0] == 0) { //D8
	        SSRes[0][1] = PLoads_temp[0][1]; //d8
	        SSRes[0][5] = PLoads_temp[0][2]; //d8
	    }
	    if(PLoadCols > 3) {
		    SSRes[0][2] = PLoads_temp[0][3]; //d8
	    }	   
        for(i=1; i < NumNodes; i++) {
	        X0 = Segments2[i - 1][0];
	        x = Segments2[i][0];
	        DX = x - X0;
		    for(j = 0; j < NumDloads_local; j++) {
	            X1 = DLoads_local[j][0];
	            X2 = DLoads_local[j][1];
	            Q = DLoads_local[j][5];
	            QNeg = -DLoads_local[j][6];
	            QA = DLoads_local[j][7];
	            QANeg = -DLoads_local[j][8];
	            if(X2 != X1) {
	                Qdash = (DLoads_local[j][6] -DLoads_local[j][5])/ (X2 - X1); //d8
	                QAdash = (DLoads_local[j][8] - DLoads_local[j][7]) / (X2 - X1); //d8	            	
	            } else {
	                Qdash = 0;
	                QAdash = 0;
	            }

	            DX1 = x - X1;
	            if(DX1 < 0) {
	            	DX1 = 0;
	            }
	            
	            DX2 = x - X2;
	            if(DX2 < 0) {
	            	DX2 = 0;
	            }
	            
	            SSRes[i][1] = SSRes[i][1] + Q * DX1 + Qdash * Math.pow(DX1, 2)/ 2 + QNeg * DX2 - Qdash * Math.pow(DX2, 2) / 2; //d8
	            SSRes[i][2] = SSRes[i][2] + Q * Math.pow(DX1, 2)/ 2 + Qdash * Math.pow(DX1, 3) / 6 + QNeg *Math.pow(DX2, 2) / 2 - Qdash * Math.pow(DX2, 3) / 6; //d8
	            SSRes[i][3] = SSRes[i][3] + (Q * Math.pow(DX1, 3) / 6 + Qdash * Math.pow(DX1, 4) / 24 + QNeg * Math.pow(DX2, 3) / 6 - Qdash * Math.pow(DX2, 4) / 24); //d8
	            SSRes[i][4] = SSRes[i][4] + (Q * Math.pow(DX1, 4) / 24 + Qdash * Math.pow(DX1, 5)  / 120 + QNeg * Math.pow(DX2, 4) / 24 - Qdash * Math.pow(DX2, 5) / 120); //d8
	            SSRes[i][5] = SSRes[i][5] + QA * DX1 + QAdash * Math.pow(DX1, 2) / 2 + QANeg * DX2 - QAdash * Math.pow(DX2, 2) / 2;//d8
	            SSRes[i][6] = SSRes[i][6] + QA * Math.pow(DX2, 2)/ 2 + QAdash * Math.pow(DX1, 3) / 6 + QANeg * Math.pow(DX2, 2) / 2 - QAdash * Math.pow(DX2, 3) / 6; //d8
		    }		   

		    for(j = 0; j < NumPloads_local; j++) {
	            X1 = PLoads_temp[j][0];//d8
	            DX1 = x - X1; // 8.88 * 10(-15) > 0 ??

	            /* for debugging purpose */
	            if(DX1 < 0.000001){
	        	    DX1 = 0;	            	
	            }
	            /*for debugging purpose*/

	            W = PLoads_temp[j][1]; //d8
	            PA = PLoads_temp[j][2]; //d8
	            
	            // if DX1 < 0 Then DX1 = 0
	            if(DX1 > 0) {
	                SSRes[i][1] = SSRes[i][1] + W; //d8
	                SSRes[i][2] = SSRes[i][2] + W * DX1; //d8
	                SSRes[i][3] = SSRes[i][3] + (W * DX1 * DX1 / 2); //d8
	                SSRes[i][4] = SSRes[i][4] + (W * DX1 * DX1* DX1 / 6); //d8
	                SSRes[i][5] = SSRes[i][5] + PA; //d8
	                SSRes[i][6] = SSRes[i][6] + PA * DX1; //d8
	            }
		    }

		    if(NumBCols > 1) {

			    //'Adjust shear slope and deflection for GA
	            GA = Segments2[i][2]; //d8
			    if(GA > 0) {
	               SSRes[i][7] = SSRes[i][1] / GA; //d8
	               SSRes[i][8] = SSRes[i - 1][8] - (SSRes[i][2] - SSRes[i - 1][2]) / GA; //d8
			    }
			    if(NumBCols > 2) {
				    //'Adjust axial deflection for EA
	                EA = Segments2[i][3]; //d8
	                SSRes[i][6] = SSRes[i][6] / EA; //d8				   
			    }
		    }

		    if(PLoadCols > 3) {
			   for(j = 0; j < NumPloads_local; j++) {
	               X1 = PLoads_temp[j][0]; //d8
	               DX1 = x - X1; // 8.88 * 10(-15) > 0 ??
	               /*for debugging purpose*/
		           if(DX1< 0.000001) {
		        	   DX1 = 0;	            	
		           }            
		           /* for debugging purpose */
		           
	               W = PLoads_temp[j][3]; //d8
				   if(DX1 > 0) {
	                   SSRes[i][2]= SSRes[i][2]+ W; //d8
	                   SSRes[i][3]= SSRes[i][3]+ W * DX1; //d8
	                   SSRes[i][4]= SSRes[i][4]+ (W * Math.pow(DX1, 2) / 2); //d8					   
				   }
		       }
           }
	    }

        for(i = 0; i < NumSegs + 1; i++) { //d8 // < instead of <=
	        x = OutPoints[i];
	        SSRes[i][0]= x;
	    }

	    // Adjust slope and deflection for EI
        for(i = 1; i < NumNodes; i++) { //d8
	        EI = Segments2[i][1]; //d8
	        x = Segments2[i][0]; //d8
	        DX = x - Segments2[i - 1][0]; //d8

	        if(i == 1) { //d8
	        	StartSlope = SSRes[i - 1][3]; //d8
	        } else {
	        	StartSlope = EndSlope; //d8
	        }

	        if(i == 1) {
	        	StartDef = SSRes[i - 1][4]; //d8
	        } else {
	        	StartDef = EndDef; //d8
	        }

	        EndSlope = StartSlope + (SSRes[i][3] - SSRes[i - 1][3]) / EI; //d8
	        EndDef = StartDef + StartSlope * DX + (SSRes[i][4] - SSRes[i - 1][4] - (SSRes[i - 1][3] * DX)) / EI; //d8
	        SSRes[i - 1][3] = StartSlope;
	        SSRes[i - 1][4] = StartDef;
	    }

	    i = i - 1;
	    SSRes[i][3] = EndSlope; //d8
	    SSRes[i][4] = EndDef; //d8
	    
	    // LinBeam2 = SSRes
	    // Exit Function
	    if(NumBCols > 0) {
	    	for(i = 1; i < NumNodes; i++) {
	            SSRes[i][4] = SSRes[i][4] + SSRes[i][8]; //d8
	    	}
	    }

	    // Adjust deflection
	    End2DT = -(EndDefA[1][1] - EndDefA[0][1]) * BeamSin + (EndDefA[1][2] - EndDefA[0][2]) * BeamCos; //d8
	    End2DAx = (EndDefA[1][1] - EndDefA[0][1]) * BeamCos + (EndDefA[1][2] - EndDefA[0][2]) * BeamSin; //d8
	    DefChange = (SSRes[S2Node-1][4] - End2DT); //d8

	    if(DefChange != 0) {
	        SlopeChange = DefChange / Span;
	        for(i = 1; i < NumNodes; i++) {
	            SSRes[i][3] = SSRes[i][3] - SlopeChange; //d8
	            SSRes[i][4] = SSRes[i][4] - SlopeChange * SSRes[i][0];//d8       	
	        }
	    }

	    k = 0; //d8
	    SSRes2 = new double[NumNodes][9];
	    for(i=0;i<NumOut;i++) { //d8 // i ranges from 0 ~ 100
	    	while(OutPoints[i] > SSRes[k][0]) { //d8
	            k = k + 1;	    		
	    	}
	    	for(j = 0; j < 3; j++) { //d8
	            SSRes2[i][j] = SSRes[k][j];   	
	    	}

	        SSRes2[i][3] = SSRes[k][5]; //d8
	        SSRes2[i][4] = -SSRes[i][4] * BeamSin + SSRes[i][6] * BeamCos ; // DX
	        SSRes2[i][5] = (SSRes[i][4] * BeamCos + SSRes[i][6] * BeamSin); // DY
	        SSRes2[i][6] = SSRes[k][3]; //d8
	        SSRes2[i][7] = SSRes[k][4]; //d8
	        SSRes2[i][8] = SSRes[k][6]; //d8
	    }

	    // Add initial deflection of start node
	    if(EndDefA[0][1] != 0 || EndDefA[0][2] != 0) { //d8
	        Xinc = (EndDefA[1][1] * 0 + EndDefA[0][1]) / Span; //d8
	        YInc = (EndDefA[1][2] * 0 + EndDefA[0][2]) / Span; //d8
	        End1Defa[0] = EndDefA[0][1]; //d8
	        End1Defa[1] = EndDefA[0][2]; //d8
	        End1DefAng = Math.atan2(EndDefA[0][2],EndDefA[0][1]) + Math.PI / 2; //d8 //reversed
	        End1DRtn = Math.pow(EndDefA[0][1], 2) 
	        			+ Math.pow(Math.pow(EndDefA[0][2], 2),0.5) 
	        			* Math.cos(BeamAng - End1DefAng) * sgn(End1DefAng- Math.PI/2); //d8
	        for(i = 0;  i < NumNodes; i++) { //d8
	        	SSRes2[i][7] = SSRes2[i][7] + End1DRtn; //d8
	            SSRes2[i][8] = SSRes2[i][8] + End1DRtn * Math.tan(BeamAng - End1DefAng); //d8
	            SSRes2[i][4] = SSRes2[i][4] + EndDefA[0][1]; //d8
	            SSRes2[i][5] = SSRes2[i][5] + EndDefA[0][2]; //d8
	        }
	    }

	    if(NumOutCols == 8) {
	        return SSRes2;
	    } else {
	    	SSRes3 = new double[NumNodes][NumOutCols];
	    	for(i = 0; i < NumNodes; i++) {
	    		for(j = 0; j < NumOutCols; j++) {
	                k = (int) OutCols[0][j].doubleValue(); //d8
	                SSRes3[i][j] = SSRes2[i][k-1];//d8
	    		}
	    	}
	    	return SSRes3;
	    }
    }

   

    double[] CombineArray(double[] Array1, double[] Array2, int Num3, Integer Num1, Integer Num2, Boolean AddZero) {
        double[] Array3;
	    int i=0; //iterator starting from 0
	    int j=0; //iterator starting from 0
	    int k=0; //iterator starting from 0
	    int Num1_local, Num2_local;
	    boolean AddZero_local;

	    /*
	     * if TypeName(Array1) = "Range" Then Array1 = Array1.Value2
	     * if TypeName(Array2) = "Range" Then Array2 = Array2.Value2
	     * */
	    if(AddZero == null) {
		    AddZero_local = false;
	    }  else {
	    	AddZero_local = true;
	    }	   

 	    Num1_local = Array1.length;
	    Num2_local = Array2.length;
	    Num3_global = Num1_local + Num2_local;
	    Array3 = new double[Num3_global];

	    if(AddZero_local == true) {
	    	Num3_global = Num3_global + 1;
	    	//ReDim Array3(1 To Num3_local, 1 To 1)
	        Array3[0] = 0;
	        k = 1; //d8
	        if(Array1[0] == 0) { 
	        	i = 1;
	        } //d8	            
	        if(Array2[0] == 0) { 
	        	j = 1;
	        } //d8

	    }

	    do {
	    	if(Array1[i] <= Array2[j]){ //d8
	    		Array3[k]= Array1[i]; //d8
	    	}
	    	if (Array1[i] == Array2[j]) { //d8
	    		j = j + 1;
	    		i = i + 1;
	    	} else {
	    		Array3[k] = Array2[j]; //d8
	    		j = j + 1;
	    	}
	    	k = k + 1;
	    	} while((i < Num1_local) && (j < Num2_local)); // < instead of <= 
	    
	    if(i < Num1_local) {//d8 // < instead of <= 
	    	for(j = i; j< Num1_local; j++) {
	    		Array3[k]= Array1[j];//d8
	    		k = k + 1;
	    	}
	    } else if (j < Num2_local) { //d8 // < instead of <=
	    	for(i=j; i< Num2_local; i++) {
	    		Array3[k] = Array2[i]; //d8
	    		k = k + 1;
	    	}
	    }
	    
	    Num3_global = k ;  // k instead of k-1
	    return Array3;
	}
    
    double[][] Range2Array(double[][] InRange, int NumRows, int NumCols, Double MaxVal, Integer MaxValCol, Boolean RemoveRows, Integer CheckCol) {
    	int MaxValCol_local;
    	int CheckCol_local;
    	boolean RemoveRows_local;
    	double MaxVal_local;

	    /* for optional argument MaxValCol */
    	if(MaxValCol == null){
    		MaxValCol_local = 1;
    	} else {
    		MaxValCol_local=MaxValCol.intValue();
    	}

	    /* for optional argument CheckCol */
    	if(CheckCol == null) {
    		CheckCol_local = 1;
    	} else {
    		CheckCol_local=CheckCol.intValue();
    	}

	    /* for optional argument RemoveRows */
    	if(RemoveRows == null) {
    		RemoveRows_local = true;
    	} else {
    		RemoveRows_local = RemoveRows.booleanValue();
    	}

	    /* for optional argument MaxVal */
	    if(MaxVal == null) {
		    MaxVal_local = 0.0;
	    } else {
		    MaxVal_local = MaxVal.doubleValue();
	    }	   

	    int i, j;
	    double[][] InRange2;
	      	   
	    /*
	     * if TypeName(InRange) = "Range" Then InRange = InRange.Value2
	     * */

	    if(InRange.getClass().isArray() == false) { // redundant if branch
		    this.NumRows = 1;
		    this.NumCols = 1;		   
		    return InRange;
	    }

	    this.NumCols = InRange[0].length; //d8
	    this.NumRows = InRange.length;
 
	    for(i = 1; i < this.NumRows; i++) { //d8
	 	    if(InRange[i][CheckCol_local] == 0) { //d8 //understood as whether this cell is zero
			    break;
		    }
		    if(MaxVal_local > 0) { 
			    if(InRange[i][MaxValCol_local-1] - MaxVal_local > 0.000000000001) { //d8
				    break;
			    }
		    }
	    }

	    if(RemoveRows_local == true) {
		    if(i < this.NumRows) { //d8
			    this.NumRows = i; // i instead of i-1 due to iterator starting from 0
			    InRange2 = new double[this.NumRows][this.NumCols];
			    for(i = 0; i < this.NumRows; i++) {
				    for(j = 0; j < this.NumCols; j++) {
					    InRange2[i][j] = InRange[i][j];
				    }
			    }
            InRange = InRange2;
		    }
	    }
	    return InRange;
    }

    void formKg() {
	    double[] KParamA, KGSparse, BAA, InforceA, ResforceA = null, forceErrA = null, React, FA, F_aA, DefacA, DefaA;
	    double[][] KGSparsei, KGA, KGCA, GFreeA, FEMA, KBCA = null, NFG;
	    int iErr, TNumFree, L = 0, M = 0, i, j, k, gj, gk, GNumFree, DNode, TNumNodes, NumFix = 0, ERBeam, EREnd, RNode, LastFree = 0, Node1,
	    		Node2, NumSuppDef = 0, SolverType, PropNum, RowNum;
	    int[] NumFixA, NFreeA, FreeA;
	    int[][] GBFreea;
	    double BA, BE, BI, BL, BSA, BG, DX, DY, Theta, TimeNow, CalTime, ER = 0, PHI;
	    double[][] DefA,KBAA,BeamA;
	    final double StifFact = 1000000.0;
	    String[] NLabels,BLabels;

	    /*
	     * UseAL = CheckALref(); //another method from nowhere
	     * if(UseAL == false){
	     *    SolverType = SetSparse("VBA solver")    ' SetSparse(False);
	     * }
	     * */

	    /* debugging process - start */
	    NumFixA = new int[] {0, 0, 0};
	    FreeA = new int[] {0, 0, 0, 0, 0, 0};
	    InforceA = new double[] {0.0, 0.0};
	    ResforceA = new double[] {0.0, 0.0};
	    forceErrA = new double[] {0.0, 0.0};

	    /* debugging process - end */
	    BeamA = new double[NumBeams][2];
	    KBAA = new double[6*NumBeams][7];
	    for(i = 0; i < NumBeams; i++){ //d8
	        DX = nodeCoordinates[beamCon[i][2]-1][0] - nodeCoordinates[beamCon[i][1]-1][0]; //d8
	        DY = nodeCoordinates[beamCon[i][2]-1][1] - nodeCoordinates[beamCon[i][1]-1][1]; //d8
	        BeamA[i][0] = Math.pow((DX * DX + DY *DY), 0.5) ;
	        BeamA[i][1] = Math.atan2(DY, DX); //reversed
	    }

	    GNumFree = (NumNodes) * 3 + NumER;
	    for(i = 0; i < NumRest; i++) { //d8
		    for(j = 1; j < 4; j++) { //d8 // starting from 1 and ending with 3 due to inclusion of node restraint number
			    if(Character.toUpperCase(nodeRestrain[i][j].char_data) == 'F') {
				    nodeRestrain[i][j].setChar('F'); // simplified the logic here
	                NumFix = NumFix + 1;
	                NumFixA[j-1] = NumFixA[j-1] + 1;			   
			    }			   
		    }
	    }

	    if(NumFixA[0] == 0 || NumFixA[1] == 0) { //d8
		    System.out.println("There must be at least one node with restraint in X and Y directions"); //not sure whether this should be kept. Since this can be prevented initially
		    return;		   
	    }
	    TNumFree = GNumFree - NumFix;
	  
	    /*ReDim block*/
	    KGCA = new double[GNumFree][GNumFree];
	    KGA = new double[TNumFree][TNumFree];
	    React = new double[GNumFree];
	    GFreeA = new double[GNumFree][2];
	    FA = new double[GNumFree];
	    F_aA = new double[TNumFree];
	    DefacA = new double[GNumFree];
	    NFreeA = new int[GNumFree];
	    GBFreea = new int[NumBeams][6];
	    KParamA = new double[7];

	    // Create dummy nodes for each released beam end	   
	    if(NumERBeams > 0 ) {
		    for(i = 0; i < NumERBeams; i++) {
			    DNode = NumNodes + i;
			    ERBeam = (int) beamEndRelease[i][0]; //d8
			    for(j = 1;j < 4; j++) {
				    ER = ER + beamEndRelease[i][j];
			    }
			    if(ER > 0) {
				    EREnd = 1;
			    } else {
				    EREnd = 2;
			    }

	            nodeCoordinates[DNode][0] = DNode; //d8
                RNode = beamCon[ERBeam - 1][1 + EREnd]; //d8
                nodeCoordinates[DNode][1] = nodeCoordinates[RNode][1]; //d8
                nodeCoordinates[DNode][2] = nodeCoordinates[RNode][2]; //d8			   
		     }
	    }

	    FEMA = FEMact(beamCon, nodeCoordinates, distLoads, pointLoads, moments);

	    // Create array of freedoms for each beam, GBFreeA
        for(i = 0; i < NumBeams; i++) {
		    Node1 = beamCon[i][1]; //d8
		    Node2 = beamCon[i][2]; //d8
		    for(j = 0; j < 3; j++){ //d8
			    GBFreea[i][j] = (Node1 - 1) * 3 + j + 1; // +1 due to iterator starting from 0
		    }
		    if(GBFreea[i][2] > LastFree){ //d8
			    LastFree = GBFreea[i][2]; //d8
		    }
		    for(j = 3; j < 6; j++) {
			    GBFreea[i][j] = (Node2 - 1) * 3 + (j - 3) + 1; // +1 due to iterator starting from 0
		    }
		    if(GBFreea[i][5] > LastFree){ //d8
			    LastFree = GBFreea[i][5]; //d8
		    }
	    }
	   
        for(i = 0; i < NumERBeams; i++){ //d8
		    ERBeam = (int)beamEndRelease[i][0]; //d8
		   
		    for(j = 1; j < 7; j++){ //d8
			    ER = beamEndRelease[i][j];
			    if(ER > 0) {
			 	    LastFree = LastFree + 1;
			 	    GBFreea[ERBeam-1][j - 1] = LastFree;	//d8 
			    } 
		    }
	    }

        // Create global array of active freedoms, GFreeA,
	    // and array of number of freedoms at each node, NFreeA
        k = 0;
	    for(i = 0; i < NumNodes; i++){ //d8
		    if((int) nodeRestrain[k][0].double_data.doubleValue() == i+1){ // i+1 because node number starts from 0
			    for(j = 1; j < 4; j++){ //d8 //starting from 1 and ending with 3 due to inclusion of node number
				    if(Character.toUpperCase(nodeRestrain[k][j].char_data) == 'B' || Character.toUpperCase(nodeRestrain[k][j].char_data) != 'F') { // according the data structure no need j+1
					    L = L + 1;
					    M = M + 1;
					    GFreeA[L-1][0] = j + (i) * 3;//d8    	
					    if(nodeRestrain[k][j].double_data != null) {
						    GFreeA[L-1][1] = nodeRestrain[k][j].double_data.doubleValue(); //d8
						    NumSuppDef = NumSuppDef + 1;
					    }
				    }
			    }
			    if(k < NumRest-1) { //d8
				    k = k + 1;
			    }
		    } else {
			    for(j = 1; j < 4; j++){ //d8 //d8 //starting from 1 and ending with 3 due to inclusion of node number
				    L = L + 1;
				    M = M + 1;
				    GFreeA[L-1][0] = j + (i) * 3;//d8 	    			
			    }
		    }
		    NFreeA[i] = M;
		    M = 0;
        }
	    
	    k = NumNodes * 3;
	    for(i = 0; i < NumERBeams; i++) { //d8
	    	for(j = 0; j < 6;j++) { //d8
	    		if(beamEndRelease[i][j + 1] == 1) {
	                k = k + 1;
                    L = L + 1;
                    M = M + 1;
                    GFreeA[L-1][0] = k; //d8
                }
	    	}
	    	NFreeA[i + NumNodes] = M;
	    	M = 0;
	    }
	     
	    // Create complete global stiffness matrix
        // ReDim block
	    KGSparsei = new double[NumBeams * 6 - 1][1];
	    KGSparse  = new double[NumBeams * 6 - 1];
	    
	    for(i = 0; i < NumBeams; i++){ //d8
	    	for(j = 0; j < 6; j++){ //d8
	    		FreeA[j] = GBFreea[i][j]; //d8
	    	}
	    	PropNum = beamCon[i][0]; //d8
	        BA = propertyTypes[PropNum-1][0]; //d8
	        BI = propertyTypes[PropNum-1][1]; //d8
	        BE = propertyTypes[PropNum-1][2]; //d8
	        BSA = propertyTypes[PropNum-1][3];//d8
	        BG = propertyTypes[PropNum-1][4]; //d8
	        BL = BeamA[i][0]; //d8
	        Theta = BeamA[i][1]; //d8
	        KParamA[0] = BE * BA / BL; //d8
	        KParamA[1] = 12 * BE * BI / (Math.pow(BL, 3)); //d8
	        KParamA[2] = BE * BI / BL; //d8
	        KParamA[3] = 6 * BE * BI / (Math.pow(BL, 2)); //d8
	        KParamA[4] = Math.cos(Theta); //d8
	        KParamA[5] = Math.sin(Theta); //d8
	        KParamA[6] = KParamA[2]; //d8

	    	if(BSA != 0 && BG != 0 ) {
	            PHI = 12 * BE * BI / (BG * BSA * BL * BL);
                KParamA[1] = KParamA[1] / (1 + PHI); //d8
                KParamA[2] = KParamA[2] * (1 + PHI / 4) / (1 + PHI); //d8
                KParamA[3] = KParamA[3] / (1 + PHI); //d8
                KParamA[6] = KParamA[6] * (1 - PHI / 2) / (1 + PHI); //d8
	    	}

	        iErr = formKL(KParamA, KBCA);
	        KBCA = this.KB; // for debugging purpose
	    	for(j = 0; j < 6; j++){ //d8
	            gj = FreeA[j]; //d8
	    		for(k = 0; k < 6; k++) { //d8
	                gk = FreeA[k]; //d8
                    KGCA[gj-1][gk-1] = KGCA[gj-1][gk-1] + KBCA[j][k]; //d8	    			
	    		}
	    	}

	    	for(j = 0; j < 6; j++) { //d8
	            RowNum = i * 6 + j + 1 ; // i instead of i-1  // due to iterator starting from 0
	    		for(k = 0; k < 6; k++) { //d8
	    			KBAA[RowNum-1][k] = KBCA[j][k]; //d8
	    		}
	    		KBAA[RowNum-1][6] = KParamA[j]; //d8
	    	}
	    }
	    
	    // Extract active global stiffness matrix, KGA, from KGCA
	    for(j = 0; j < TNumFree; j++) { //d8
	        gj = (int) GFreeA[j][0]; //d8
	    	for(k = 0;k < TNumFree; k++) { //d8
	            gk = (int) GFreeA[k][0]; //d8
	            KGA[j][k] = KGCA[gj-1][gk-1]; //d8	    		
	    	}
	    	if(GFreeA[j][1] != 0) { //d8
	    		KGA[j][j] = KGA[j][j] * StifFact;
	    	}
	    }

	    // Create array of applied end actions, FA

	    for(i = 0; i < NumBeams; i++) { //d8
	    	for(j = 0; j < 6; j++) { //d8
	            FreeA[j] = GBFreea[i][j]; //d8	    		
	    	}
	    	for(j = 0; j < 6; j++) {
	            FA[FreeA[j]-1] = FA[FreeA[j]-1] + FEMA[i][j]; //d8
	    		if(j < 2) { //d8
	                InforceA[j] = InforceA[j] + FEMA[i][j];	    			
	    		} else if (j > 2 && j < 5) { //d8
	                InforceA[j - 3] = InforceA[j - 3] + FEMA[i][j];	    			
	    		}
	    	}
	    }

	    // Range("resforce") = InforceA
	    resforce = InforceA;// pass InforceA to result resforce 

	    // Extract array of actions applied to active freedoms, F_aA, from FA
	    for(i = 0; i< TNumFree; i++) { //d8
	    	F_aA[i] = FA[(int) GFreeA[i][0]-1]; //d8
	    	if(GFreeA[i][1] != 0) { //d8
	    		F_aA[i] = F_aA[i] + GFreeA[i][1] * KGA[i][i]; //d8
	    	}
	    }

	    /*
	     * Transfer F_aA to range f_a
	     * With Range("f_a")
	     *     .ClearContents
	     *     .Resize(TNumFree, 1).Name = "f_a"
	     * End With
	     * Range("f_a").Value2 = F_aA
	     * */  

		/*    
	     * Resize range defa, solve system for active freedoms
	     * With Range("defa")
	     *     .ClearContents
	     *     .Resize(TNumFree, 1).Name = "defa"
	     * End With
	     * TimeNow = Timer
	     * */	   

	    DefaA = GESolve(KGA,F_aA);

	    for(i = 0; i < TNumFree; i++) {
	        DefacA[(int)GFreeA[i][0]-1]= DefaA[i]; //d8
	    }
	    
	    // Transfer DefacA to range defc 

		for(int ii = 0; ii < nodeResult.length; ii++) {
			nodeResult[ii][0] = DefacA[ii]; // pass defacA to result nodeResult[0]   
		}

		// ' Create arrays of node and beam labels and transfer to range "nlabels" and "blabels"
		NLabels = new String[GNumFree];
	    for(i = 0; i < NumNodes; i++) { //d8
	        j = i * 3; // originall was (i-1)*3 for i starting from 1
            NLabels[j + 0]= "DX" + Integer.toString(i); //d8
            NLabels[j + 1]= "DY" + Integer.toString(i); //d8
            NLabels[j + 2]= "RZ" + Integer.toString(i); //d8	    	
	    }

	    L = 0;
	    j = NumNodes * 3;
	    for(i = 0; i < NumERBeams; i++) { //d8
	    	for(k = 0; k < 6; k++) { //d8 // supposed to be 0~7 since beam number is not included,now changed to 0~6
	    		if(beamEndRelease[i][k] == 1) {
	    		    j = j + 1;
	                L = L + 1;
	                NLabels[j-1] = "Rel" + new Double(beamEndRelease[i][0]).toString() + "-" + Integer.toString(k); //d8
	    		}
	    	}
	    }

	    BLabels = new String[NumBeams*2];
	    for(i = 0; i < NumBeams; i++) {
	        j = i * 2;
	        BLabels[j] = Integer.toString(i) + "-" + "1";
	        BLabels[j +1] = Integer.toString(i) + "-" + "2";
	    }
	    
	    NFG = MMultv(KGCA, DefacA);

	    for(i = 0; i < GNumFree; i++) { //d8
	        React[i] = NFG[i][0] - FA[i]; //d8
	    }

	    /* Range("react").Value = React */
	    for(int ii = 0; ii < nodeResult.length; ii++) {
	    	nodeResult[ii][1] = React[ii]; // pass defacA to result nodeResult[1]   
	    }

	    for(i = 0; i < NumNodes; i++) { //d8
	    	for(j = 0; j < 2; j++) { //d8
	            L = i * 3 + j; 
	            ResforceA[j] = ResforceA[j] + React[L]; //d8 L should be L & j should be j
	    	}	    	
	    }

	    // Range("resout").Value = ResforceA
	    resOut = ResforceA; // pass ResforceA to result resOut 

	    for(i = 0; i < 2; i++) {
	        forceErrA[i] = InforceA[i] + ResforceA[i];	    	
	    }

	    // Range("outerr").Value = forceErrA
	    outErr = forceErrA; // pass ResforceA to result outErr  

	    //  Find beam actions
	    iErr = BeamActions(KBAA, DefacA, FEMA, GBFreea);

	    AllBeams();
    }   



    double[][] MMultv(double[][] Mat1, double[] Mat2) {
	    double[][] MatRes;
		int NumRows, NumCols, i, j, k;

	    NumRows = Mat1.length;
	    NumCols = Mat2.length;
	    MatRes = new double[NumRows][NumCols];

	    for(i = 0; i < NumCols; i++) {
	        for(j = 0; j < NumRows; j++) {
	            for(k = 0; k < NumRows; k++){
	                MatRes[j][i] = MatRes[j][i] + Mat1[j][k] * Mat2[k]; //Mat2[k] instead of Mat2[k][i]
	            }
	        }
	    }

	    return MatRes;
	} 

    int BeamActions(double[][] KBAA, double[] DefacA, double[][] FEMA, int[][] GBFreea) {
	    int NumBeams, i, j, k, FreeNum, BeamNum;
	    double KBA[][];
	    double[] DefA;
	    double[][] BeamAct;
	    double CT, ST, FX, FY;	   

	    KBA = new double[6][6];
	    DefA = new double[6];

	    NumBeams = GBFreea.length;// count the number of beams Difference

	    beamEndResult = new double[NumBeams * 2][3];
	    for(i = 0; i < NumBeams; i++) {
		    BeamNum = i * 6; //d8
		    for(j = 0; j < 6; j++) {
			    for(k = 0; k < 6; k++) {
				    KBA[j][k] = KBAA[BeamNum + j][k];
			    }
			    FreeNum = GBFreea[i][j];
			    DefA[j] = DefacA[FreeNum-1]; //d8 between VBS and JAVA regarding index of array.
		    }
		    BeamAct = MMultv(KBA, DefA);
		    CT = KBAA[BeamNum + 4][6]; //d8
		    ST = KBAA[BeamNum + 5][6]; //d8
		    FX = BeamAct[0][0]; //d8
		    FY = KBAA[1][0]; //d8
		    
		    beamEndResult[i* 2][0] = -1 * ((BeamAct[0][0] - FEMA[i][0])* ST - (BeamAct[1][0] - FEMA[i][1])*CT); //d8
		    beamEndResult[i* 2][1] = -1 * ((BeamAct[1][0] - FEMA[i][1])* ST + (BeamAct[0][0] - FEMA[i][0])*CT); //d8
		    beamEndResult[i* 2][2] = -1 * (BeamAct[2][0] - FEMA[i][2]); //d8
		    beamEndResult[i* 2+1][0] =  ((BeamAct[3][0] - FEMA[i][3])* ST - (BeamAct[4][0] - FEMA[i][4])*CT); //d8
		    beamEndResult[i* 2+1][1] = ((BeamAct[4][0] - FEMA[i][4])* ST + (BeamAct[3][0] - FEMA[i][3])*CT); //d8
		    beamEndResult[i* 2+1][2] =  (BeamAct[5][0] - FEMA[i][5]); //d8
        }
	    return 0;
	}

    /* here is the start of mmacauley */ 
    void AllBeams() {
	    double[][] BeamResA, ResA1; 
	    int NumRows, i, j, k, M;
	    
	    //double[][] OutPoints;
	    NumRows = NumBeams * (NumSegs + 1);
	    BeamResA = new double[NumRows][8];
	    
	    M = 0;

	    for(i = 0; i < NumBeams; i++) {
		    ResA1 = LinBeam(i, NumSegs,null);
		    for(j = 0; j<NumSegs + 1; j++) {
			    M = M + 1;
			    BeamResA[M-1][0] = i+1; //d8 // i+1 instead of i to show the beam number 
			    for(k = 1; k < 8; k++) { //d8
				    BeamResA[M-1][k] = ResA1[j][k-1]; //d8 
			    }
		    }
	    }   
	    
	    beamResult = BeamResA;
    	double totalDistance = 0 ;
	    
	    for(i=0; i <distanceFromLeft.length;i++){
	    	if(BeamResA[i][0] <= 1){
	    		distanceFromLeft[i] = BeamResA[i][1];
	    	}else{
	    		if( BeamResA[i][0] > BeamResA[i-1][0]){
	    			totalDistance = totalDistance +  BeamResA[i-1][1];
	    		}	    		
	    		distanceFromLeft[i] = totalDistance + BeamResA[i][1];
	    	}	    	
	    }	    
//		   System.out.println("distanceFromLeft");
//		   for(k =0; k < distanceFromLeft.length;k++){
//			   System.out.print(distanceFromLeft[k] + "\t");					   		   
//		   }
    }
    
    double[] GESolve(double[][] Mat, double[] Vect) {
	    int MBound, i, j, k, col;
	    double Df = 0, SSum,swap_use;
	    double[] ResultA;
	    
	    MBound = Mat.length;
	    
	    if(MBound == 0) {
		    return null;
	    }
	    
	    ResultA = new double[MBound];
	    for(k = 0; k < MBound - 1; k++) { //d8
	    	for(i = k + 1; i < MBound; i++){ //d8
	    		if(Math.abs(Mat[k][k]) < Math.abs(Mat[i][k])) {
	    			//swapRowMat(i, k, MBound, Mat)
	    			for(col = 0; col < MBound; col++) { //d8
	    				swap_use = Mat[i][col];
	    				Mat[i][col] = Mat[k][col];
	    				Mat[k][col] = swap_use;
	    			}
	    			
	    			// swapRowMat(i, k, 1, Vect)
	    			swap_use = Vect[i];
	    			Vect[i] = Vect[k];
	    			Vect[k]= swap_use;
	    		}
	    		
	    		if(Mat[k][k] != 0) {
	    			Df = Mat[i][k] / Mat[k][k];
	    		}
	    		for(j = k; j < MBound; j++){ //d8
	    			Mat[i][j] = Mat[i][j] - Mat[k][j] * Df;
	    		}
	    		Vect[i] = Vect[i] - Vect[k] * Df;
	    	}
	    }
	    
	    ResultA[MBound-1] = Vect[MBound-1] / Mat[MBound-1][MBound-1]; //d8
	    for(i = MBound - 2; i >= 0; i--){ //d8
	    	SSum = 0;
	    	for(j = i + 1; j < MBound; j++){ //d8
	    		SSum = SSum + Mat[i][j] * ResultA[j];
	    	}
	    	if (Mat[i][i] != 0){ ResultA[i] = (Vect[i] - SSum) / Mat[i][i];
	    	}
	    }
	    return ResultA;
	}
    
    public void compute() {
    	formKg();
    }

}


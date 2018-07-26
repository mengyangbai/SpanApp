package com.lindenbaum.spanapp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class NodeModel {
	//add final to this
	private static Double XCoordinate_pt = new Double(0.0);
	
	private IntegerProperty NodeNumber;
	
	private int NodeNumberInt;
	
	private DoubleProperty XCoordinate;
	private Double XCoordinateDouble;	
	private BooleanProperty SupportX;
	private BooleanProperty SupportY;
	private BooleanProperty SupportM;
	
	public NodeModel(){
		this.NodeNumber = new SimpleIntegerProperty(new Integer(1));
		this.NodeNumberInt = NodeNumber.intValue();
		this.SupportX = new SimpleBooleanProperty(false);
		this.SupportY = new SimpleBooleanProperty(false);
		this.SupportM = new SimpleBooleanProperty(false);
		this.XCoordinate = new SimpleDoubleProperty(new Double(0.0));
	}
	
	public NodeModel(Integer NodeNumber){
		this.NodeNumber = new SimpleIntegerProperty(NodeNumber);
		this.NodeNumberInt = NodeNumber.intValue();
		this.SupportX = new SimpleBooleanProperty(false);
		this.SupportY = new SimpleBooleanProperty(false);
		this.SupportM = new SimpleBooleanProperty(false);
		this.XCoordinate = new SimpleDoubleProperty(new Double(0.0));	
		this.XCoordinateDouble = new Double(0.0);
	}
	
	public void setNodeNumber(Integer NodeNumber){
		this.NodeNumber.set(NodeNumber);
	}

	public Integer getNodeNumber(){
		return this.NodeNumber.get();
	}

	public IntegerProperty NodeNumberProperty(){
		return NodeNumber;
	}
	
	public void setXCoordinate(Double XCoordinate){
		this.XCoordinate.set(XCoordinate);
	}
	
	public Double getXCoordinate(){
		return this.XCoordinate.get();
	} 
	
	public DoubleProperty XCoordinateProperty(){
		return XCoordinate;
	}
	
	public void incXCoordinate(Double length){
		this.XCoordinateDouble = XCoordinate_pt + length;
		XCoordinate_pt += length;
		setXCoordinate(XCoordinateDouble);
	}
	
	public void editXCoordinate(Double length){
		setXCoordinate(this.getXCoordinate()+length);
	}
	
	
	public void decXCoordinate_pt(Double length){
		if(XCoordinate_pt > 0)
			XCoordinate_pt = XCoordinate_pt - length;
	}
	
	public void clearXCoordinate_pt(){
		XCoordinate_pt = new Double(0.0);
	}
	
	public String getNodeNumberinString(){
		return String.valueOf(this.NodeNumberInt);
	}
	
	public int getNodeNumberInt(){
		return this.NodeNumberInt;		
	}
	
	public void setNodeNumberInt(int NodeNumberInt){
		this.NodeNumberInt = NodeNumberInt;		
	}
	
	public void setSupportX(Boolean SupportX){
		this.SupportX.set(SupportX);
	}
	public Boolean getSupportX(){
		return this.SupportX.get();
	}
	public BooleanProperty SupportXProperty(){
		return SupportX;
	}
	
	public void setSupportY(Boolean SupportY){
		this.SupportY.set(SupportY);
	}
	public Boolean getSupportY(){
		return this.SupportY.get();
	}
	public BooleanProperty SupportYProperty(){
		return SupportY;
	}
	
	public void setSupportM(Boolean SupportM){
		this.SupportM.set(SupportM);
	}
	public Boolean getSupportM(){
		return this.SupportM.get();
	}
	public BooleanProperty SupportMProperty(){
		return SupportM;
	}
	

}

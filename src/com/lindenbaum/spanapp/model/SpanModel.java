package com.lindenbaum.spanapp.model;

import javafx.beans.property.*;

public class SpanModel {
	//add final to this
	
	private static Integer SpanNumber_pt = new Integer(1);
	
	private IntegerProperty SpanNumber;
	
	private int SpanNumberInt;
	
	private DoubleProperty length;

	private DoubleProperty Inertia;
	
	private NodeModel Node;
	
	private static Double lengthOldVal = new Double(0.0);
	
	private static Double inertiaOldVal = new Double(0.0);
	
	public SpanModel(){
		this(SpanNumber_pt,new Double(lengthOldVal), new Double(inertiaOldVal));
		SpanNumber_pt++;
	}	

	public SpanModel(Integer SpanNumber, Double length, Double Inertia){
		this.SpanNumber = new SimpleIntegerProperty(SpanNumber);
		this.SpanNumberInt = SpanNumber.intValue();
		this.length = new SimpleDoubleProperty(length);
		this.Inertia = new SimpleDoubleProperty(Inertia);
		this.Node = new NodeModel(SpanNumber+1);	
	}
	
	public void setSpanNumber(Integer SpanNumber){
		this.SpanNumber.set(SpanNumber);
	}

	public Integer getSpanNumber(){
		return this.SpanNumber.get();
	}
	
	public String getSpanNumberinString(){
		return String.valueOf(this.SpanNumberInt);
	}
	
	public int getSpanNumberInt(){
		return this.SpanNumberInt;		
	}
	
	public void setSpanNumberInt(int SpanNumberInt){
		this.SpanNumberInt = SpanNumberInt;		
	}
	
	public void decSpanNumber_pt(){
		if(SpanNumber_pt >1){
			this.SpanNumber_pt--;
		}
	}
	
	public IntegerProperty SpanNumberProperty(){
		return SpanNumber;
	}
	
	public void setlength(Double length){
		lengthOldVal = length;
		this.length.set(length);
	}
	
	public Double getlength(){
		return this.length.get();
	}

	public DoubleProperty lengthProperty(){
		return length;
	}
	
	public void setInertia(Double Inertia){
		inertiaOldVal = Inertia;
		this.Inertia.set(Inertia);
	}
	
	public Double getInertia(){
		return this.Inertia.get();
	}
	
	public DoubleProperty InertiaProperty(){
		return Inertia;
	}
		
	public NodeModel getNodeModel(){
		return this.Node;
	}
	
	
	public void SetInertiaOldVal( Double LastInertiaVal){
		inertiaOldVal = LastInertiaVal;
	}
	
	public void SetLengthOldVal( Double LastLengthVal){
		lengthOldVal = LastLengthVal;
	}
	
	public void ClearSpanNumber_pt(){
		SpanNumber_pt = new Integer(1);
	}
}


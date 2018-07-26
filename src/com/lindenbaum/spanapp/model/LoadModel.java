package com.lindenbaum.spanapp.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoadModel {

	private IntegerProperty SpanNumber;
	
	private int SpanNumberInt;
	
	private StringProperty Type;
	
	private DoubleProperty PLoadPosition;
	private DoubleProperty PLoadValue;
	private DoubleProperty DLoadStart;
	private DoubleProperty DLoadEnd;
	private DoubleProperty MomPosition;
	private DoubleProperty MomValue;
	private DoubleProperty Value1ForDisplay;
	private DoubleProperty Value2ForDisplay;
		
	public LoadModel(Integer SpanNumber){
		this.SpanNumber = new SimpleIntegerProperty(SpanNumber);
		this.SpanNumberInt = SpanNumber.intValue();
		this.PLoadPosition = new SimpleDoubleProperty(0.0);
		this.PLoadValue = new SimpleDoubleProperty(0.0);
		this.DLoadStart = new SimpleDoubleProperty(0.0);
		this.DLoadEnd = new SimpleDoubleProperty(0.0);
		this.MomPosition = new SimpleDoubleProperty(0.0);
		this.MomValue = new SimpleDoubleProperty(0.0);
		this.Type = new SimpleStringProperty("");
		this.Value1ForDisplay = new SimpleDoubleProperty(0.0);
		this.Value2ForDisplay = new SimpleDoubleProperty(0.0);
	}
	
	//SpanNumber
	public void setSpanNumber(Integer SpanNumber){
		this.SpanNumber.set(SpanNumber);
	}

	public Integer getSpanNumber(){
		return this.SpanNumber.get();
	}

	public IntegerProperty SpanNumberProperty(){
		return SpanNumber;
	}
	
	//SpanNumberInt
	public String getSpanNumberinString(){
		return String.valueOf(this.SpanNumberInt);
	}
	
	public int getSpanNumberInt(){
		return this.SpanNumberInt;		
	}
	
	public void setSpanNumberInt(int SpanNumberInt){
		this.SpanNumberInt = SpanNumberInt;		
	}
	
	
	//PLoadPosition
	public void setPLoadPosition(Double PLoadPosition){
		this.PLoadPosition.set(PLoadPosition);
		this.SetValue1ForDisplay(PLoadPosition);// for display purpose
	}
	
	public Double getPLoadPosition(){
		return this.PLoadPosition.get();
	} 
	
	public DoubleProperty PLoadPositionProperty(){
		return PLoadPosition;
	}
	//PLoadValue
	public void setPLoadValue(Double PLoadValue){
		this.PLoadValue.set(PLoadValue);
		this.SetValue2ForDisplay(PLoadValue);// for display purpose
	}
	
	public Double getPLoadValue(){
		return this.PLoadValue.get();
	} 
	
	public DoubleProperty PLoadValueProperty(){
		return PLoadValue;
	}

	//MomPosition
	public void setMomPosition(Double MomPosition){
		this.MomPosition.set(MomPosition);
		this.SetValue1ForDisplay(MomPosition);// for display purpose
	}
	
	public Double getMomPosition(){
		return this.MomPosition.get();
	} 
	
	public DoubleProperty MomPositionProperty(){
		return MomPosition;
	}
	//MomValue
	public void setMomValue(Double MomValue){
		this.MomValue.set(MomValue);
		this.SetValue2ForDisplay(MomValue);// for display purpose
	}
	
	public Double getMomValue(){
		return this.MomValue.get();
	} 
	
	public DoubleProperty MomValueProperty(){
		return MomValue;
	}	

	//DLoadStart
	public void setDLoadStart(Double DLoadStart){
		this.DLoadStart.set(DLoadStart);
		this.SetValue1ForDisplay(DLoadStart);// for display purpose
	}
	
	public Double getDLoadStart(){
		return this.DLoadStart.get();
	} 
	
	public DoubleProperty DLoadStartProperty(){
		return DLoadStart;
	}
	//DLoadEnd
	public void setDLoadEnd(Double DLoadEnd){
		this.DLoadEnd.set(DLoadEnd);
		this.SetValue2ForDisplay(DLoadEnd);// for display purpose
	}
	
	public Double getDLoadEnd(){
		return this.DLoadEnd.get();
	} 
	
	public DoubleProperty DLoadEndProperty(){
		return DLoadEnd;
	}
	
	//Type
	public void SetType(String Type){
		this.Type.set(Type);
	}
	
	public String getType(){
		return this.Type.get();
	}
	
	public StringProperty TypeProperty(){
		return Type;
	}
	
	//Value1ForDisplay
	public void SetValue1ForDisplay(Double Value1ForDisplay){
		this.Value1ForDisplay.set(Value1ForDisplay);
	}
	
	public Double getValue1ForDisplay(){
		return this.Value1ForDisplay.get();
	}
	
	public DoubleProperty Value1ForDisplayProperty(){
		return Value1ForDisplay;
	}
	
	//Value2ForDisplay
	public void SetValue2ForDisplay(Double Value2ForDisplay){
		this.Value2ForDisplay.set(Value2ForDisplay);
	}
	
	public Double getValue2ForDisplay(){
		return this.Value2ForDisplay.get();
	}
	
	public DoubleProperty Value2ForDisplayProperty(){
		return Value2ForDisplay;
	}
}

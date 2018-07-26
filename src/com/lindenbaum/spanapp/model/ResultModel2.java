package com.lindenbaum.spanapp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultModel2 {

	//
	private final StringProperty SpanNo;
	private final DoubleProperty Shear;
	private final DoubleProperty Moments;

	public ResultModel2(){
		this.SpanNo = new SimpleStringProperty("");
		this.Shear = new SimpleDoubleProperty(0.0);
		this.Moments = new SimpleDoubleProperty(0.0);
	}

	public ResultModel2(String SpanNo, double Shear, double Moments){
		this.SpanNo = new SimpleStringProperty(SpanNo);
		this.Shear = new SimpleDoubleProperty(Shear);
		this.Moments = new SimpleDoubleProperty(Moments);
	}
	//SpanNo
	public void setSpanNo(String SpanNo){
		this.SpanNo.set(SpanNo);
	}

	public String getSpanNo(){
		return this.SpanNo.get();
	}

	public StringProperty SpanNoProperty(){
		return SpanNo;
	}
	//Shear
	public void setShear(Double Shear){
		this.Shear.set(Shear);
	}

	public Double getShear(){
		return this.Shear.get();
	}

	public DoubleProperty ShearProperty(){
		return Shear;
	}
	
	//Moments
	public void setMoments(Double Moments){
		this.Moments.set(Moments);
	}

	public Double getMoments(){
		return this.Moments.get();
	}

	public DoubleProperty MomentsProperty(){
		return Moments;
	}
}

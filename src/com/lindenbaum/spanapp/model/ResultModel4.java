package com.lindenbaum.spanapp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultModel4 {


	//
	private final DoubleProperty SpanNo4;
	private final DoubleProperty Deflection4;//max def;
	private final StringProperty Shearrange;
	private final StringProperty Momentsrange;

	public ResultModel4(){
		this.SpanNo4 = new SimpleDoubleProperty(0.0);
		this.Deflection4 = new SimpleDoubleProperty(0.0);
		this.Shearrange = new SimpleStringProperty("");
		this.Momentsrange = new SimpleStringProperty("");
	}

	public ResultModel4(double SpanNo4, double Deflection4, String Shearrange,String Momentsrange){
		this.SpanNo4 = new SimpleDoubleProperty(SpanNo4);
		this.Deflection4 = new SimpleDoubleProperty(Deflection4);
		this.Shearrange = new SimpleStringProperty(Shearrange);
		this.Momentsrange = new SimpleStringProperty(Momentsrange);
	}
	
	//SpanNo4
	public void setDistance(Double SpanNo4){
		this.SpanNo4.set(SpanNo4);
	}

	public Double getSpanNo4(){
		return this.SpanNo4.get();
	}

	public DoubleProperty SpanNo4Property(){
		return SpanNo4;
	}
	
	//Deflection4
	public void setDeflection4(Double Deflection4){
		this.Deflection4.set(Deflection4);
	}

	public Double getDeflection4(){
		return this.Deflection4.get();
	}

	public DoubleProperty Deflection4Property(){
		return Deflection4;
	}
	
	//Shear3
	public void setShearrange(String Shearrange){
		this.Shearrange.set(Shearrange);
	}

	public String getShearrange(){
		return this.Shearrange.get();
	}

	public StringProperty ShearrangeProperty(){
		return Shearrange;
	}

	//Momentsrange
	public void setMomentsrange(String Momentsrange){
		this.Shearrange.set(Momentsrange);
	}

	public String getMomentsrange(){
		return this.Momentsrange.get();
	}

	public StringProperty MomentsrangeProperty(){
		return Momentsrange;
	}
}

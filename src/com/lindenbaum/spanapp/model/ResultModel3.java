package com.lindenbaum.spanapp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ResultModel3 {

	//
	private final DoubleProperty Distance;
	private final DoubleProperty Deflection3;
	private final DoubleProperty Shear3;
	private final DoubleProperty Moments3;

	public ResultModel3(){
		this.Distance = new SimpleDoubleProperty(0.0);
		this.Deflection3 = new SimpleDoubleProperty(0.0);
		this.Shear3 = new SimpleDoubleProperty(0.0);
		this.Moments3 = new SimpleDoubleProperty(0.0);
	}

	public ResultModel3(double Distance, double Deflection3, double Shear3, double Moments3){
		this.Distance = new SimpleDoubleProperty(Distance);
		this.Deflection3 = new SimpleDoubleProperty(Deflection3);
		this.Shear3 = new SimpleDoubleProperty(Shear3);
		this.Moments3 = new SimpleDoubleProperty(Moments3);
	}
	
	//Deflection3
	public void setDistance(Double Distance){
		this.Distance.set(Distance);
	}

	public Double getDistance(){
		return this.Distance.get();
	}

	public DoubleProperty DistanceFromLeftProperty(){
		return Distance;
	}
	
	//Deflection3
	public void setDeflection3(Double Deflection3){
		this.Deflection3.set(Deflection3);
	}

	public Double getDeflection3(){
		return this.Deflection3.get();
	}

	public DoubleProperty Deflection3Property(){
		return Deflection3;
	}
	
	//Shear3
	public void setShear3(Double Shear3){
		this.Shear3.set(Shear3);
	}

	public Double getShear3(){
		return this.Shear3.get();
	}

	public DoubleProperty Shear3Property(){
		return Shear3;
	}
	
	//Moments3
	public void setMoments3(Double Moments3){
		this.Moments3.set(Moments3);
	}

	public Double getMoments3(){
		return this.Moments3.get();
	}

	public DoubleProperty Moments3Property(){
		return Moments3;
	}
	
}

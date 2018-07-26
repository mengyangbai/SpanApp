package com.lindenbaum.spanapp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class YongModulusModel {
	private DoubleProperty YoungModulus;
	
	public YongModulusModel(){
		this(0.0);
	}

	public YongModulusModel(Double YoungModulus){
		this.YoungModulus = new SimpleDoubleProperty(YoungModulus);
	}
	
	public Double getYoungModulus(){
		return this.YoungModulus.get();
	}
	
	public void setYoungModulus(Double YoungModulus){
		this.YoungModulus.set(YoungModulus);
	}
	
	public DoubleProperty YoungModulusProperty(){
		return YoungModulus;
	}
	
}

package com.lindenbaum.spanapp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultModel1 {

	
	//Result page 1
	private final StringProperty NodeNo;
	private final DoubleProperty Deflection;
	private final DoubleProperty Reaction;
	
	public ResultModel1(){
		this.NodeNo = new SimpleStringProperty("");
		this.Deflection = new SimpleDoubleProperty(0.0);
		this.Reaction = new SimpleDoubleProperty(0.0);
	}

	public ResultModel1(String NodeNo, double Deflection, double Reaction){
		this.NodeNo = new SimpleStringProperty(NodeNo);
		this.Deflection = new SimpleDoubleProperty(Deflection);
		this.Reaction = new SimpleDoubleProperty(Reaction);
	}
	//NodeNo
	public void setNodeNo(String Nodeno){
		this.NodeNo.set(Nodeno);
	}

	public String getNodeNo(){
		return this.NodeNo.get();
	}

	public StringProperty NodeNoProperty(){
		return NodeNo;
	}
	//Deflection
	public void setDeflection(Double Deflection){
		this.Deflection.set(Deflection);
	}

	public Double getDeflection(){
		return this.Deflection.get();
	}

	public DoubleProperty DeflectionProperty(){
		return Deflection;
	}
	
	//NodeNo
	public void setReaction(Double Reaction){
		this.Reaction.set(Reaction);
	}

	public Double getReaction(){
		return this.Reaction.get();
	}

	public DoubleProperty ReactionProperty(){
		return Reaction;
	}
}

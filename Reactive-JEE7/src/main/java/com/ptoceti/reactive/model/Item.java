package com.ptoceti.reactive.model;

public class Item {

	private String name;
	
	private String value;
	
	public Item(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

	public void setValue(String value){
		this.value = value;
	}
	
	public String getName(){
		return name;
	}
	
	public Item clone(){
		return new Item(this.name, this.value);
	}
}

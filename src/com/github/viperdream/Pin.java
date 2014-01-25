package com.github.viperdream;

public class Pin {

	private Integer pinID;
	private String pinTitle;
	private String pinMessage;
	private Integer pinDuration;
	private Float pinX;
	private Float pinY;
	private String pinColor;
	private String author;
	private Integer pinSQLId;
	
	public Pin(){}
	
	public Pin(int pinID, String pinTitle, String pinMessage, Integer pinDuration, Float pinX, Float pinY, String pinColor, String author, Integer pinSQLId){
		this.pinID = pinID;
		this.pinTitle = pinTitle;
		this.pinMessage = pinMessage;
		this.pinDuration = pinDuration;
		this.pinX = pinX;
		this.pinY = pinY;
		this.pinColor = pinColor;
		this.author = author;
		this.pinSQLId = pinSQLId;
	}
	
	public Pin(String pinTitle, String pinMessage, Integer pinDuration, Float pinX, Float pinY, String pinColor, String author, Integer pinSQLId){
		this.pinTitle = pinTitle;
		this.pinMessage = pinMessage;
		this.pinDuration = pinDuration;
		this.pinX = pinX;
		this.pinY = pinY;
		this.pinColor = pinColor;
		this.author = author;
		this.pinSQLId = pinSQLId;
	}
	
	//get ------------------------------------------------------
	public int getID(){
		return this.pinID;
	}
	
	public String getPinTitle(){
		return this.pinTitle;
	}
	
	public String getPinMessage(){
		return this.pinMessage;
	}
	
	public Integer getPinDuration(){
		return this.pinDuration;
	}
	public Float getPinX(){
		return this.pinX;
	}
	
	public Float getPinY(){
		return this.pinY;
	}
	public String getPinColor(){
		return this.pinColor;
	}
	public String getPinAuthor(){
		return this.author;
	}
	public Integer getPinSQLId(){
		return this.pinSQLId;
	}
	
	//set ------------------------------------------------------
	public void setPinID(int pinID){
		this.pinID = pinID;
	}
	
	public void setPinTitle(String pinTitle){
		this.pinTitle = pinTitle;
	}
	
	public void setPinMessage(String pinMessage){
		this.pinMessage = pinMessage;
	}
	
	public void setPinDuration(int pinDuration){
		this.pinDuration = pinDuration;
	}
	public void setPinX(Float pinX){
		this.pinX = pinX;
	}
	
	public void setPinY(Float pinY){
		this.pinY = pinY;
	}
	public void setPinColor(String pinColor){
		this.pinColor = pinColor;
	}
	public void setPinAuthor(String author){
		this.author = author;
	}
	public void setPinSQLId(Integer pinSQLId){
		this.pinSQLId = pinSQLId;
	}
}

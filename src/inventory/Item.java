/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

/**
 Name: <Ernst Thelifort>
 Course: CNT 4714 –
 Spring 2021 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 31, 2021
 */
public class Item {
	private double price;
	private String itemID;
	private String title;
	private boolean inStock;
	
        public boolean isInStock() {
		return inStock;
	}
        public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getItemID() {
		return itemID;
	}
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
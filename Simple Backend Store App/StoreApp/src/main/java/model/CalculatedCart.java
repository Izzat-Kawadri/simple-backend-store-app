package model;

import java.util.ArrayList;
import java.util.List;

import entity.Product;

public class CalculatedCart {
	private int id;

	private List<Product> products = new ArrayList<>();

	private float amount;
	private float totalAmount;

	public CalculatedCart() {
	}

	public CalculatedCart(int id, List<Product> products, float amount, float totalAmount) {
		super();
		this.id = id;
		this.products = products;
		this.amount = amount;
		this.totalAmount = totalAmount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
}

package model;

import java.util.ArrayList;
import java.util.List;

import entity.Product;

public class Cart {
	private int id;

	private List<Product> products = new ArrayList<>();

	private int quantity;

	public Cart(int id, List<Product> products, int quantity) {
		super();
		this.id = id;
		this.products = products;
		this.quantity = quantity;
	}

	public Cart() {

	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public float getAmountById(int id) {
		
		return products.get(id).getAmount();
	}
}

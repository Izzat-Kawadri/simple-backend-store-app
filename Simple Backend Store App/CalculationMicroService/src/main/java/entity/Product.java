package entity;

public class Product {

	private int id;

	private float amount;

	public Product(int id, float amount) {
		super();
		this.id = id;
		this.amount = amount;
	}

	public Product() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}

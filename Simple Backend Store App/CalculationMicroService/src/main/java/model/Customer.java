package model;

public class Customer {
	private String name;
	private String address;
	private String paymentType;
	
	public Customer(String name, String address, String paymentType) {
		super();
		this.name = name;
		this.address = address;
		this.paymentType = paymentType;
	}
	public Customer() {}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
}

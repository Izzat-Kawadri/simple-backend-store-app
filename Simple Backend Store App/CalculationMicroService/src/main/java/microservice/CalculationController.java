package microservice;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.CalculatedCart;
import model.Cart;
import model.Customer;
import entity.Account;
import entity.Order;

@RestController
public class CalculationController {
	float calcAmount = 0;
	int cartQuantity = 0;
	int orderId = 0;
	protected Logger logger = Logger.getLogger(CalculationController.class.getName());

	@RequestMapping("/calc")
	public CalculatedCart doCalc(@RequestParam Cart cart, @RequestParam Customer customer) {

		Account newCustomer = new Account(customer.getName(), customer.getAddress());

		cartQuantity = cart.getQuantity();
		while (cartQuantity > 0) {
			calcAmount = calcAmount + cart.getAmountById(cart.getId());
			cartQuantity--;
		}
		orderId++;
		Order newOrder = new Order(orderId, customer.getName(), customer.getAddress(), customer.getPaymentType(),
				calcAmount);
		return new CalculatedCart(cart.getId(), cart.getProducts(), cart.getAmountById(cart.getId()), calcAmount);

	}
}
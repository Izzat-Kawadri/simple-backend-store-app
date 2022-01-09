package web;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import model.CalculatedCart;
import model.Cart;

@Controller
public class WebCalculationController {
	  @Autowired
	    protected WebCalculationService calculationService;
        
	  protected Logger logger = Logger.getLogger(WebCalculationController.class
	            .getName());

	public WebCalculationController(WebCalculationService calculationService) {
		
		this.calculationService = calculationService;
	}

	@Cacheable("calculatedcart")
	@RequestMapping("/calc")
    public CalculatedCart doCalc(Cart cart) {
 
        CalculatedCart calcCart = calculationService.calc(cart);
 
        logger.info("CalculatedCart: " + calcCart);
        
 
        return calcCart;
    }  
}

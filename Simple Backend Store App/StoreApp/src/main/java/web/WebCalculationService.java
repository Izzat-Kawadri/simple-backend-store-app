package web;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import model.CalculatedCart;
import model.Cart;
 
@Service
public class WebCalculationService {
	 @Autowired
	    @LoadBalanced
	    protected RestTemplate restTemplate;
	 
	    protected String serviceUrl;
	 
	    protected Logger logger = Logger.getLogger(WebCalculationService.class
	            .getName());
	 
	    public WebCalculationService(String serviceUrl) {
	        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
	                : "http://" + serviceUrl;
	    }
	 
	    public CalculatedCart calc(Cart cart) {
	        return restTemplate.getForObject(serviceUrl + "/calc?cart={cart}", CalculatedCart.class, cart);
	    }
	}


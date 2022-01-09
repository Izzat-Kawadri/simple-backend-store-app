# Simple-Backend-Store-App
a simple backend store app using spring boot
## Introduction :
The backend system uses the REST API and processes requests from the site / application that sells the company's products.
1. As part of the task, we add a new method for calculating the cost of the user's cart.
 The endpoint receives the "Cart" object as input
  - Array of goods (Products)
  - product id (int)
  - the number of goods (int).
  - payment type (String)
  - address id from the user's address book (String)
   
  At the exit, the "Calculated basket" object :
   - array of goods (Products)
   - product id (int)
   - number of goods (int)
   - amount by position (float)
   - total amount (float)

2. The API method for calculating prices uses an external microservice (via http)

3. The system stores information about the prices of goods in the cache, if there is no information in the cache, then it takes information
 from an external microservice and puts them in the cache
---
## Implementation :
To achieve the goal of this Project, we'll implement three microservices:

1. a service registry (Eureka Server).
2. a web Store application, which is consuming the REST service as a registry-aware client (Spring Cloud Netflix Feign Client).
3. a Calculation REST service which registers itself at the registry (Eureka Client)

---
## 1. Service Registry (Eureka Server) :
Firstly we'll create a new project and put the dependencies into it. 
Next, we're creating the main application class:

### main :
```java
 @EnableEurekaServer
 @SpringBootApplication
 public class DiscoveryServerApplication {

 public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}
 }
 ```

Finally, we're configuring the properties file:
### properties :
```
server.port=1111
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.instance.hostname= localhost


eureka.client.instance.leaseRenewalIntervalInSeconds=5

eureka.client.serviceUrl.defaultZone= http://localhost:1111/eureka/

logging.level.com.netflix.eureka=OFF
logging.level.com.netflix.discovery=OFF
```
---
## 2. Web Store Application :
 first, we'll add the dependencies. Again,
 then we will declare the entity class of our project.
 ### 1. Product :
 ```java
 
public class Product {

	private int id;

	private float amount;

	public Product(int id, float amount) {
		super();
		this.id = id;
		this.amount = amount;
	}
  }
  //getters and setters
  ```
  ### 2.  Account :
  ```java 
  public class Account {
	private String userName;
	private String address;

	
	public Account(String userName, String address) {
		super();
		this.userName = userName;
		this.address = address;
	}
	public Account() {}
  }
//getters and setters
```
### 3. Order :
```java
public class Order {
	private int id;

	private String customerName;
	private String customerAddress;
	private String paymentType;
	private float amount;

	public Order() {

	}

	public Order(int id, String customerName, String customerAddress, String paymentType, float amount) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.paymentType = paymentType;
		this.amount = amount;
	}
  }
  //getters and setters 
  ```
  ### 4. Customer :
  ```java 
  
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
  }
  //getters and setters
  ```
  then we will implemnt the cart model using our entity classes :
  ### 1. Cart :
  ```java
  
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

	public Cart() {}
  public float getAmountById(int id) {
		
		return products.get(id).getAmount();
	}
  }
  //getters and setters
  ```
  ### 2. CalculatedCart :
  ```java
  
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
  }
  //getters and setters 
  ```
  then we will declare our service class :
  ###  WebCalculationService :
  ```java 
  
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
```
then we will declare our REST Controller class :
###  WebCalculationController :
```java 

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
```
after that we will set the application.properties:
###  application.properties:
```
spring.application.name=StoreApp
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

server.port=2222

eureka.client.serviceUrl.defaultZone = http://localhost:1111/eureka/
```

finally we will implement the main application class:
###  main :
 ```java 
@EnableAutoConfiguration
@EnableEurekaClient
@ComponentScan(useDefaultFilters = false)
@EnableCaching
@SpringBootApplication
public class StoreAppApplication {

	public static final String CALCULATION_SERVICE_URL = "http://calculation-service";

	public static void main(String[] args) {
		SpringApplication.run(StoreAppApplication.class, args);
	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public WebCalculationService calculationService() {
		return new WebCalculationService(CALCULATION_SERVICE_URL);
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}
	
}
```
---

## 3. Calculation REST Microservice :
 first, we'll add the dependencies. Again,and we will declare our entity classes to be able use them.
 after that we will declare the REST Controller that will calculate the full amount 
 ### CalculationController :
 ```java 
 
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
```
then we will set the application.properties:
###  application.properties:
```
spring.application.name=Calculation-service
# HTTP Server
server.port: 4444

eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.serviceUrl.defaultZone= http://localhost:1111/eureka/
```
 finally we will implement the main application class:
 ### main :
 ```java
@EnableAutoConfiguration
@EnableEurekaClient
@ComponentScan
@SpringBootApplication
public class CalculationMicroServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CalculationMicroServiceApplication.class, args);
	}
}
```
---

## Running the Project :
to run this project we need to run the 3 applications by open each app then do :
```
mvnw clean package 
```
then :
```
java -jar target\appxx.jar
```
![alt text](https://github.com/Izzat-Kawadri/simple-backend-store-app/blob/e7be45ea334e5ab55c97c07d6706c52aae2860cd/Screenshot.png "Screen")
## Project Notes :
this Project is still under development, and i am working to add html templates files using spring thymeleaf.
if you have any question or you found a bug please contact me :) .

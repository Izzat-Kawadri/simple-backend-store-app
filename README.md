# Simple-Backend-Store-App
a simple backend store app using spring boot
## Introduction :
The backend system uses the REST API and processes requests from the site / application that sells the company's products.
As part of the task, we add a new method for calculating the cost of the user's cart.
The endpoint receives the "Cart" object as input
1. Array of goods
  - product id (int)
  - the number of goods (int).
  - payment type
  - address id from the user's address book 
   
At the exit, the "Calculated basket" object
  1. array of goods
    - product id (int)
    - number of goods (int)
    - amount by position (float)
    - total amount (float)

2. The API method for calculating prices uses an external microservice (via http)

3. The system stores information about the prices of goods in the cache, if there is no information in the cache, then it takes information
 from an external microservice and puts them in the cache

## Implementation :
To achieve the goal of this write-up, we'll implement three microservices:

1. a service registry (Eureka Server).
2. a web Store application, which is consuming the REST service as a registry-aware client (Spring Cloud Netflix Feign Client).
3. a Calculation REST service which registers itself at the registry (Eureka Client)

## 1. service registry (Eureka Server) :
Firstly we'll create a new project and put the dependencies into it. 
Next, we're creating the main application class:

```java
 @EnableEurekaServer
 @SpringBootApplication
 public class DiscoveryServerApplication {

 public static void main(String[] args) {
		SpringApplication.run(DiscoveryServerApplication.class, args);
	}
 }
 ```

Finally, we're configuring the properties in YAML format; so an application.yml will be our configuration file:

```
server.port=1111
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.instance.hostname= localhost


eureka.client.instance.leaseRenewalIntervalInSeconds=5

eureka.client.registerWithEureka=true
eureka.client.fetchRegistry=true
eureka.client.serviceUrl.defaultZone= http://localhost:1111/eureka/

logging.level.com.netflix.eureka=OFF
logging.level.com.netflix.discovery=OFF
```
## 2. web Store application :
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
  ### - WebCalculationService :
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



 finally we will implement the main application class:
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
## 3. Calculation REST Microservice :
 first, we'll add the dependencies. Again,
 then we will implement the main application class:
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


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
  1.array of goods
    - product id (int)
    - number of goods (int)
    - amount by position (float)
   2.total amount (float)

2. The API method for calculating prices uses an external microservice (via http)

3. The system stores information about the prices of goods in the cache, if there is no information in the cache, then it takes information
 from an external microservice and puts them in the cache

## Implementation :
To achieve the goal of this write-up, we'll implement three microservices:

1. a service registry (Eureka Server).
2. a REST service which registers itself at the registry (Eureka Client)
3. a web application, which is consuming the REST service as a registry-aware client (Spring Cloud Netflix Feign Client).

## 1.service registry (Eureka Server)

# CPSC 4620 Pizza Database

A Java and MySQL database application developed for CPSC 4620 at Clemson University.

## Project Overview

This project is a pizza ordering and management system backed by a relational MySQL database. The application supports customer management, pizza and topping selection, order processing, discounts, inventory tracking, and business reporting.

The Java application communicates with the MySQL database through JDBC and uses SQL queries, relational tables, and database views to manage and analyze the application's data.

## Features

* Customer management
* Pizza ordering
* Pickup, delivery, and dine-in orders
* Pizza toppings and pricing
* Discounts
* Inventory tracking
* Order completion tracking
* Business and profit reports
* SQL database views for reporting

## Technologies

* **Java**
* **MySQL**
* **JDBC / MySQL Connector**
* **SQL**
* **Git & GitHub**

## Project Structure

```text
cpsc4620-pizza-database/
├── database/
│   ├── PizzaDB.sql
│   └── PizzaDB_portfolio.sql
├── lib/
│   └── mysql-connector-j-9.6.0.jar
├── src/
│   └── cpsc4620/
│       ├── Customer.java
│       ├── DBConnector.java
│       ├── DBNinja.java
│       ├── DeliveryOrder.java
│       ├── DineinOrder.java
│       ├── Discount.java
│       ├── Menu.java
│       ├── Order.java
│       ├── PickupOrder.java
│       ├── Pizza.java
│       └── Topping.java
├── .gitignore
└── README.md
```

## Database

The database is organized around customers, orders, pizzas, toppings, discounts, pricing, and inventory.

The project also includes SQL views used for reporting:

* `profitbyordertype`
* `profitbypizza`
* `toppingpopularity`

The `PizzaDB_portfolio.sql` file is provided as the portfolio-ready database script, while `PizzaDB.sql` preserves the original project database dump.

## What This Project Demonstrates

This project gave me hands-on experience working across both application and database layers.

### SQL & Database

* Relational database design
* Primary and foreign keys
* SQL queries
* Database views
* Joins and aggregations
* Inventory and order data management
* Connecting Java applications to MySQL

### Java

* Object-oriented programming
* Classes and inheritance
* ArrayLists and object collections
* JDBC database connectivity
* Console-based application development
* Input validation and application logic

### QA & Testing

My current professional experience is in QA testing, so this project also provides a foundation for demonstrating how I approach software quality:

* Identifying expected application behavior
* Testing database-backed functionality
* Reproducing issues
* Documenting expected versus actual results
* Thinking through different order and customer scenarios

## Academic Context

Developed as part of Clemson University's CPSC 4620 coursework.

The original project requirements and academic materials are not included in this repository. The code has been organized here as a portfolio demonstration of my Java, SQL, database, and testing experience.

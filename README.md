# Pizza Ordering Database

A Java and MySQL application for managing pizza orders, customers, discounts, and inventory, with SQL views and queries for business reporting. Built for CPSC 4620 (Database Systems) at Clemson University.

## Overview

A console-based ordering system backed by a relational MySQL database. Java connects to MySQL through JDBC, and reporting logic lives in SQL views instead of application code.

## Features

- Customer management
- Pizza ordering with sizes, crusts, toppings, and calculated pricing
- Dine-in, pickup, and delivery orders
- Order-level and pizza-level discounts
- Topping inventory tracking
- Order completion tracking
- Profit and topping-popularity reports through SQL views
- Business analytics queries (`analytics/business_questions.sql`)

## Tech Stack

Java, MySQL 8, JDBC (MySQL Connector/J 9.6.0), SQL, HTML/CSS/JavaScript (dashboard prototype)

## Getting Started

### Prerequisites
- JDK 
- MySQL 8.0 or newer

### 1. Create and load the database
```bash
mysql -u root -p -e "CREATE DATABASE PizzaDB;"
mysql -u root -p PizzaDB < database/PizzaDB_portfolio.sql
```

### 2. Configure the connection (optional)
The defaults are user `root`, an empty password, and database `PizzaDB`. To override:
```bash
export PIZZA_DB_USER="your_user"
export PIZZA_DB_PASSWORD="your_password"
export PIZZA_DB_NAME="PizzaDB"
```

### 3. Compile and run
```bash
javac -cp "lib/mysql-connector-j-9.6.0.jar" -d out src/cpsc4620/*.java
java -cp "out:lib/mysql-connector-j-9.6.0.jar" cpsc4620.Menu
```
On Windows, use `;` instead of `:` in the classpath.

## Project Structure

```
├── analytics/
│   └── business_questions.sql    Queries answering five business questions
├── database/
│   ├── PizzaDB.sql               Original database dump
│   └── PizzaDB_portfolio.sql     Same schema and data, with view definers removed so it imports on any MySQL account
├── lib/                          MySQL Connector/J
├── src/cpsc4620/                 Java source (Menu.java is the course-provided console front end)
├── ui/                           Dashboard prototype (HTML, CSS, Chart.js)
└── README.md
```

## Database Design

12 tables: `customer`, `ordertable`, `pickup`, `delivery`, `dinein`, `pizza`, `topping`, `pizza_topping`, `discount`, `order_discount`, `pizza_discount`, `baseprice`. The three order types (pickup, delivery, dine-in) are modeled as subtype tables of `ordertable`, and `pizza_topping` resolves the many-to-many relationship between pizzas and toppings.

ER Diagram coming soon 

### Reporting Views

| View | Reports |
|------|---------|
| `profitbyordertype` | Revenue, cost, and profit by order type and month, with a grand total row |
| `profitbypizza` | Profit by pizza size and crust type per month |
| `toppingpopularity` | How many times each topping has been used |

`analytics/business_questions.sql` builds on these views to answer five questions: profit by order type, profit by size and crust, topping popularity, inventory alerts (current vs. minimum stock), and overall KPIs (revenue, cost, profit, average order value).

## Screenshots

coming soon 

## What I Learned

- Designing a relational schema with primary keys, foreign keys, and many-to-many relationships
- Writing joins, aggregations, and views for reporting
- Connecting a Java application to MySQL with JDBC
- Testing database-backed behavior from a QA perspective: checking expected versus actual results across order types, discounts, and inventory updates

## Academic Note

Developed for CPSC 4620 at Clemson University. Assignment instructions and grading materials are not included. `Menu.java` was provided by the course.

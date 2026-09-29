# Pizza Ordering Database & Analytics

A MySQL database for a pizza ordering business, with SQL reporting views that answer business questions about profit, order types, and topping popularity. Includes a Java/JDBC application layer.

Built for CPSC 4620 (Database Systems) at Clemson University.

## Business Questions This Database Answers

- Which order types (dine-in, pickup, delivery) are the most profitable?
- Which pizzas generate the most profit?
- Which toppings are the most popular?

## Key Findings

*Based on the sample data in this project.*

- **Profit by order type:** Delivery orders earned the most total profit ($99.39 across two months), followed by pickup ($90.34) and dine-in ($44.34). Across all order types, the business earned $234.07 in profit on $301.18 in order revenue.
- **Profit by pizza:** Original-crust pizzas were the most profitable, led by the Large Original ($69.48, Jan 2025) and the XLarge Original ($68.80, Feb 2025). The Small Original was the least profitable at $5.53.
- **Topping popularity:** Pepperoni and Regular Cheese tied as the most-used toppings (10 each), followed by Four Cheese Blend (7). Jalapenos were never ordered (0), which could flag them as a candidate to drop from the menu or promote.

## Database Design

The database is organized around customers, orders, pizzas, toppings, discounts, pricing, and inventory. It uses primary and foreign keys to keep data consistent across tables, and supports pickup, delivery, and dine-in orders.

## SQL Highlights

- Relational design with primary and foreign keys
- Joins and aggregations across multiple tables
- Reporting views: `profitbyordertype`, `profitbypizza`, `toppingpopularity`
- Inventory and order data management

Run the views yourself:

```sql
SELECT * FROM profitbyordertype;
SELECT * FROM profitbypizza;
SELECT * FROM toppingpopularity;
```

## Repository Structure

```
cpsc4620-pizza-database/
├── analytics/     # SQL analytics and reporting queries
├── database/
│   ├── PizzaDB.sql             # Original project database dump
│   └── PizzaDB_portfolio.sql   # Cleaned, portfolio-ready database script
├── lib/                        # MySQL JDBC connector
├── src/cpsc4620/               # Java application (JDBC)
├── ui/                         # Demo user interface (AI-assisted)
├── .gitignore
└── README.md
```

## How to Run It

1. Install MySQL and create a database: `CREATE DATABASE pizzadb;`
2. Load the database: `mysql -u YOUR_USERNAME -p pizzadb < database/PizzaDB_portfolio.sql`
3. Run the queries in the `analytics/` folder, or query the views listed above.

## Technologies

MySQL, SQL, Java, JDBC, Git & GitHub

## What I Did and Didn't Build

- **Written by me:** database schema, SQL queries, reporting views, analytics
- **AI-assisted:** the UI in the `ui/` folder was generated with ChatGPT to demo the data
- **Academic context:** built for Clemson's CPSC 4620. Original course materials are not included.

## Testing Approach

With a QA background, I validated the database by checking expected vs. actual results across different order and customer scenarios, including edge cases like discounts and inventory changes.

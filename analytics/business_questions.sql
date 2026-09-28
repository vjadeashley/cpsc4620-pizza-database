-- CPSC 4620 Pizza Database
-- Portfolio Analytics
--
-- Business questions explored in this project:
--
-- 1. Which order types generate the most revenue and profit?
-- 2. Which pizza sizes and crust combinations generate the most profit?
-- 3. Which toppings are most popular?
-- 4. Which toppings may need inventory attention?
-- 5. How does pizza/order profitability change over time?


-- ============================================================
-- 1. PROFIT BY ORDER TYPE
-- ============================================================

SELECT
    CustomerType AS order_type,
    OrderMonth AS order_month,
    TotalOrderPrice AS revenue,
    TotalOrderCost AS cost,
    Profit AS profit
FROM profitbyordertype
WHERE CustomerType <> ''
ORDER BY Profit DESC;


-- ============================================================
-- 2. PROFIT BY PIZZA SIZE AND CRUST
-- ============================================================

SELECT
    Size AS pizza_size,
    Crust AS crust_type,
    OrderMonth AS order_month,
    Profit AS profit
FROM profitbypizza
ORDER BY Profit DESC;


-- ============================================================
-- 3. TOPPING POPULARITY
-- ============================================================

SELECT
    Topping AS topping,
    ToppingCount AS times_used
FROM toppingpopularity
ORDER BY ToppingCount DESC, Topping ASC;


-- ============================================================
-- 4. INVENTORY ALERTS
-- ============================================================

SELECT
    topping_TopName AS topping,
    topping_CurINVT AS current_inventory,
    topping_MinINVT AS minimum_inventory,
    topping_CurINVT - topping_MinINVT AS inventory_buffer
FROM topping
ORDER BY inventory_buffer ASC;


-- ============================================================
-- 5. OVERALL BUSINESS KPIs
-- ============================================================

SELECT
    COUNT(*) AS total_orders,
    SUM(ordertable_CustPrice) AS total_revenue,
    SUM(ordertable_BusPrice) AS total_cost,
    SUM(ordertable_CustPrice - ordertable_BusPrice) AS total_profit,
    AVG(ordertable_CustPrice) AS average_order_value
FROM ordertable;
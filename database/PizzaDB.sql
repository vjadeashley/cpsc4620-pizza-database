-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (arm64)
--
-- Host: localhost    Database: PizzaDB
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `baseprice`
--

DROP TABLE IF EXISTS `baseprice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `baseprice` (
  `baseprice_Size` varchar(30) NOT NULL,
  `baseprice_CrustType` varchar(30) NOT NULL,
  `baseprice_CustPrice` decimal(5,2) NOT NULL,
  `baseprice_BusPrice` decimal(5,2) NOT NULL,
  PRIMARY KEY (`baseprice_CrustType`,`baseprice_Size`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `baseprice`
--

LOCK TABLES `baseprice` WRITE;
/*!40000 ALTER TABLE `baseprice` DISABLE KEYS */;
INSERT INTO `baseprice` VALUES ('Large','Gluten-Free',9.50,4.00),('medium','Gluten-Free',6.25,3.00),('small','Gluten-Free',4.00,2.00),('XLarge','Gluten-Free',12.50,6.00),('Large','Original',8.00,2.00),('medium','Original',5.00,1.50),('small','Original',3.00,0.75),('XLarge','Original',10.00,3.00),('Large','Pan',9.00,3.00),('medium','Pan',6.00,2.25),('small','Pan',3.50,1.00),('XLarge','Pan',11.50,4.50),('Large','Thin',8.00,1.25),('medium','Thin',5.00,1.00),('small','Thin',3.00,0.50),('XLarge','Thin',10.00,2.00);
/*!40000 ALTER TABLE `baseprice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_CustID` int NOT NULL AUTO_INCREMENT,
  `customer_FName` varchar(30) NOT NULL,
  `customer_LName` varchar(30) NOT NULL,
  `customer_PhoneNum` varchar(30) NOT NULL,
  PRIMARY KEY (`customer_CustID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'Andrew','Wilkes-Krier','8642545861'),(2,'Matt','Engers','8644749953'),(3,'Frank','Turner','8642328944'),(4,'Milo','Auckerman','8648785679');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery` (
  `ordertable_OrderID` int NOT NULL,
  `delivery_HouseNum` int NOT NULL,
  `delivery_Street` varchar(30) NOT NULL,
  `delivery_City` varchar(30) NOT NULL,
  `delivery_State` varchar(2) NOT NULL,
  `delivery_Zip` int NOT NULL,
  `delivery_IsDelivered` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ordertable_OrderID`),
  CONSTRAINT `deliveryFK` FOREIGN KEY (`ordertable_OrderID`) REFERENCES `ordertable` (`ordertable_OrderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (4,115,'Party Blvd','Anderson','SC',29621,1),(6,6745,'Wessex St','Anderson','SC',29621,1),(7,8879,'Suburban Lane','Anderson','SC',29621,1);
/*!40000 ALTER TABLE `delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dinein`
--

DROP TABLE IF EXISTS `dinein`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinein` (
  `ordertable_OrderID` int NOT NULL,
  `dinein_TableNum` int NOT NULL,
  PRIMARY KEY (`ordertable_OrderID`),
  CONSTRAINT `dineinFK` FOREIGN KEY (`ordertable_OrderID`) REFERENCES `ordertable` (`ordertable_OrderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dinein`
--

LOCK TABLES `dinein` WRITE;
/*!40000 ALTER TABLE `dinein` DISABLE KEYS */;
INSERT INTO `dinein` VALUES (1,21),(2,4);
/*!40000 ALTER TABLE `dinein` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `discount_DiscountID` int NOT NULL AUTO_INCREMENT,
  `discount_DiscountName` varchar(30) NOT NULL,
  `discount_Amount` decimal(5,2) NOT NULL,
  `discount_IsPercent` tinyint(1) NOT NULL,
  PRIMARY KEY (`discount_DiscountID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
INSERT INTO `discount` VALUES (1,'Employee',15.00,1),(2,'Lunch Special Medium',1.00,0),(3,'Lunch Special Large',2.00,0),(4,'Specialty Pizza',1.50,0),(5,'Happy Hour',10.00,1),(6,'Gameday Special',20.00,1);
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_discount`
--

DROP TABLE IF EXISTS `order_discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_discount` (
  `ordertable_OrderID` int NOT NULL,
  `discount_DiscountID` int NOT NULL,
  PRIMARY KEY (`ordertable_OrderID`,`discount_DiscountID`),
  KEY `orderdiscountFK2` (`discount_DiscountID`),
  CONSTRAINT `orderdiscountFK1` FOREIGN KEY (`ordertable_OrderID`) REFERENCES `ordertable` (`ordertable_OrderID`),
  CONSTRAINT `orderdiscountFK2` FOREIGN KEY (`discount_DiscountID`) REFERENCES `discount` (`discount_DiscountID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_discount`
--

LOCK TABLES `order_discount` WRITE;
/*!40000 ALTER TABLE `order_discount` DISABLE KEYS */;
INSERT INTO `order_discount` VALUES (7,1),(2,2),(4,6);
/*!40000 ALTER TABLE `order_discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordertable`
--

DROP TABLE IF EXISTS `ordertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordertable` (
  `ordertable_OrderID` int NOT NULL AUTO_INCREMENT,
  `customer_CustID` int DEFAULT NULL,
  `ordertable_OrderType` varchar(30) NOT NULL,
  `ordertable_OrderDateTime` datetime NOT NULL,
  `ordertable_CustPrice` decimal(5,2) NOT NULL,
  `ordertable_BusPrice` decimal(5,2) NOT NULL,
  `ordertable_IsComplete` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ordertable_OrderID`),
  KEY `ordertableFK` (`customer_CustID`),
  CONSTRAINT `ordertableFK` FOREIGN KEY (`customer_CustID`) REFERENCES `customer` (`customer_CustID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordertable`
--

LOCK TABLES `ordertable` WRITE;
/*!40000 ALTER TABLE `ordertable` DISABLE KEYS */;
INSERT INTO `ordertable` VALUES (1,NULL,'dinein','2025-01-05 12:03:00',19.75,3.68,1),(2,NULL,'dinein','2025-02-03 12:05:00',19.78,4.63,1),(3,1,'pickup','2025-01-03 21:30:00',89.28,19.80,1),(4,1,'delivery','2025-02-20 19:11:00',68.95,17.39,1),(5,2,'pickup','2025-01-02 17:30:00',28.70,7.84,1),(6,3,'delivery','2025-01-02 18:17:00',25.81,3.64,1),(7,4,'delivery','2025-02-13 20:32:00',31.66,6.00,1);
/*!40000 ALTER TABLE `ordertable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pickup`
--

DROP TABLE IF EXISTS `pickup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pickup` (
  `ordertable_OrderID` int NOT NULL,
  `pickup_IsPickedUp` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ordertable_OrderID`),
  CONSTRAINT `pickupFK` FOREIGN KEY (`ordertable_OrderID`) REFERENCES `ordertable` (`ordertable_OrderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pickup`
--

LOCK TABLES `pickup` WRITE;
/*!40000 ALTER TABLE `pickup` DISABLE KEYS */;
INSERT INTO `pickup` VALUES (3,1),(5,1);
/*!40000 ALTER TABLE `pickup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pizza`
--

DROP TABLE IF EXISTS `pizza`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pizza` (
  `pizza_PizzaID` int NOT NULL AUTO_INCREMENT,
  `pizza_Size` varchar(30) NOT NULL,
  `pizza_CrustType` varchar(30) NOT NULL,
  `ordertable_OrderID` int NOT NULL,
  `pizza_PizzaState` varchar(30) NOT NULL,
  `pizza_PizzaDate` datetime NOT NULL,
  `pizza_CustPrice` decimal(5,2) NOT NULL,
  `pizza_BusPrice` decimal(5,2) NOT NULL,
  PRIMARY KEY (`pizza_PizzaID`),
  KEY `pizzaFK1` (`pizza_CrustType`,`pizza_Size`),
  KEY `pizzaFK3` (`ordertable_OrderID`),
  CONSTRAINT `pizzaFK1` FOREIGN KEY (`pizza_CrustType`, `pizza_Size`) REFERENCES `baseprice` (`baseprice_CrustType`, `baseprice_Size`),
  CONSTRAINT `pizzaFK3` FOREIGN KEY (`ordertable_OrderID`) REFERENCES `ordertable` (`ordertable_OrderID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pizza`
--

LOCK TABLES `pizza` WRITE;
/*!40000 ALTER TABLE `pizza` DISABLE KEYS */;
INSERT INTO `pizza` VALUES (1,'Large','Thin',1,'completed','2025-01-05 12:03:00',19.75,3.68),(2,'Medium','Pan',2,'completed','2025-02-03 12:05:00',13.85,3.23),(3,'Small','Original',2,'completed','2025-02-03 12:05:00',6.93,1.40),(4,'Large','Original',3,'completed','2025-01-03 21:30:00',14.88,3.30),(5,'Large','Original',3,'completed','2025-01-03 21:30:00',14.88,3.30),(6,'Large','Original',3,'completed','2025-01-03 21:30:00',14.88,3.30),(7,'Large','Original',3,'completed','2025-01-03 21:30:00',14.88,3.30),(8,'Large','Original',3,'completed','2025-01-03 21:30:00',14.88,3.30),(9,'Large','Original',3,'completed','2025-01-03 21:30:00',14.88,3.30),(10,'XLarge','Original',4,'completed','2025-02-20 19:11:00',27.94,5.59),(11,'XLarge','Original',4,'completed','2025-02-20 19:11:00',31.50,6.25),(12,'XLarge','Original',4,'completed','2025-02-20 19:11:00',26.75,5.55),(13,'XLarge','Gluten-Free',5,'completed','2025-01-02 17:30:00',28.70,7.84),(14,'Large','Thin',6,'completed','2025-01-02 18:17:00',25.81,3.64),(15,'Large','Thin',7,'completed','2025-02-13 20:32:00',18.00,2.75),(16,'Large','Thin',7,'completed','2025-02-13 20:32:00',19.25,3.25);
/*!40000 ALTER TABLE `pizza` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pizza_discount`
--

DROP TABLE IF EXISTS `pizza_discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pizza_discount` (
  `pizza_PizzaID` int NOT NULL,
  `discount_DiscountID` int NOT NULL,
  PRIMARY KEY (`pizza_PizzaID`,`discount_DiscountID`),
  KEY `pizzadiscountFK2` (`discount_DiscountID`),
  CONSTRAINT `pizzadiscountFK1` FOREIGN KEY (`pizza_PizzaID`) REFERENCES `pizza` (`pizza_PizzaID`),
  CONSTRAINT `pizzadiscountFK2` FOREIGN KEY (`discount_DiscountID`) REFERENCES `discount` (`discount_DiscountID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pizza_discount`
--

LOCK TABLES `pizza_discount` WRITE;
/*!40000 ALTER TABLE `pizza_discount` DISABLE KEYS */;
INSERT INTO `pizza_discount` VALUES (1,3),(2,4),(11,4),(13,4);
/*!40000 ALTER TABLE `pizza_discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pizza_topping`
--

DROP TABLE IF EXISTS `pizza_topping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pizza_topping` (
  `pizza_PizzaID` int NOT NULL,
  `topping_TopID` int NOT NULL,
  `pizza_topping_IsDouble` int NOT NULL,
  PRIMARY KEY (`pizza_PizzaID`,`topping_TopID`),
  KEY `pizzatopsFK2` (`topping_TopID`),
  CONSTRAINT `pizzatopsFK2` FOREIGN KEY (`topping_TopID`) REFERENCES `topping` (`topping_TopID`),
  CONSTRAINT `pizzatopsgFK1` FOREIGN KEY (`pizza_PizzaID`) REFERENCES `pizza` (`pizza_PizzaID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pizza_topping`
--

LOCK TABLES `pizza_topping` WRITE;
/*!40000 ALTER TABLE `pizza_topping` DISABLE KEYS */;
INSERT INTO `pizza_topping` VALUES (1,1,0),(1,2,0),(1,13,1),(2,7,0),(2,8,0),(2,9,0),(2,12,0),(2,15,0),(3,4,0),(3,12,0),(3,13,0),(4,1,0),(4,13,0),(5,1,0),(5,13,0),(6,1,0),(6,13,0),(7,1,0),(7,13,0),(8,1,0),(8,13,0),(9,1,0),(9,13,0),(10,1,0),(10,2,0),(10,14,0),(11,3,1),(11,10,1),(11,14,0),(12,4,0),(12,14,0),(12,17,0),(13,5,0),(13,6,0),(13,7,0),(13,8,0),(13,9,0),(13,16,0),(14,4,0),(14,5,0),(14,6,0),(14,8,0),(14,14,1),(15,14,1),(16,1,1),(16,13,0);
/*!40000 ALTER TABLE `pizza_topping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `profitbyordertype`
--

DROP TABLE IF EXISTS `profitbyordertype`;
/*!50001 DROP VIEW IF EXISTS `profitbyordertype`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `profitbyordertype` AS SELECT 
 1 AS `CustomerType`,
 1 AS `OrderMonth`,
 1 AS `TotalOrderPrice`,
 1 AS `TotalOrderCost`,
 1 AS `Profit`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `profitbypizza`
--

DROP TABLE IF EXISTS `profitbypizza`;
/*!50001 DROP VIEW IF EXISTS `profitbypizza`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `profitbypizza` AS SELECT 
 1 AS `Size`,
 1 AS `Crust`,
 1 AS `Profit`,
 1 AS `OrderMonth`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `topping`
--

DROP TABLE IF EXISTS `topping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topping` (
  `topping_TopID` int NOT NULL AUTO_INCREMENT,
  `topping_TopName` varchar(30) NOT NULL,
  `topping_SmallAMT` decimal(5,2) NOT NULL,
  `topping_MedAMT` decimal(5,2) NOT NULL,
  `topping_LgAMT` decimal(5,2) NOT NULL,
  `topping_XLAMT` decimal(5,2) NOT NULL,
  `topping_CustPrice` decimal(5,2) NOT NULL,
  `topping_BusPrice` decimal(5,2) NOT NULL,
  `topping_MinINVT` int NOT NULL,
  `topping_CurINVT` int NOT NULL,
  PRIMARY KEY (`topping_TopID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topping`
--

LOCK TABLES `topping` WRITE;
/*!40000 ALTER TABLE `topping` DISABLE KEYS */;
INSERT INTO `topping` VALUES (1,'Pepperoni',2.00,2.75,3.50,4.50,1.25,0.20,50,100),(2,'Sausage',2.50,3.00,3.50,4.25,1.25,0.15,50,100),(3,'Ham',2.00,2.50,3.25,4.00,1.50,0.15,25,78),(4,'Chicken',1.50,2.00,2.25,3.00,1.75,0.25,25,56),(5,'Green Pepper',1.00,1.50,2.00,2.50,0.50,0.02,25,79),(6,'Onion',1.00,1.50,2.00,2.75,0.50,0.02,25,85),(7,'Roma Tomato',2.00,3.00,3.50,4.50,0.75,0.03,10,86),(8,'Mushrooms',1.50,2.00,2.50,3.00,0.75,0.10,50,52),(9,'Black Olives',0.75,1.00,1.50,2.00,0.60,0.10,25,39),(10,'Pineapple',1.00,1.25,1.75,2.00,1.00,0.25,0,15),(11,'Jalapenos',0.50,0.75,1.25,1.75,0.50,0.05,0,64),(12,'Banana Peppers',0.60,1.00,1.30,1.75,0.50,0.05,0,36),(13,'Regular Cheese',2.00,3.50,5.00,7.00,0.50,0.12,50,250),(14,'Four Cheese Blend',2.00,3.50,5.00,7.00,1.00,0.15,25,150),(15,'Feta Cheese',1.75,3.00,4.00,5.50,1.50,0.18,0,75),(16,'Goat Cheese',1.60,2.75,4.00,5.50,1.50,0.20,0,54),(17,'Bacon',1.00,1.50,2.00,3.00,1.50,0.25,0,89);
/*!40000 ALTER TABLE `topping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `toppingpopularity`
--

DROP TABLE IF EXISTS `toppingpopularity`;
/*!50001 DROP VIEW IF EXISTS `toppingpopularity`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `toppingpopularity` AS SELECT 
 1 AS `Topping`,
 1 AS `ToppingCount`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'PizzaDB'
--

--
-- Final view structure for view `profitbyordertype`
--

/*!50001 DROP VIEW IF EXISTS `profitbyordertype`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `profitbyordertype` AS select `ordertable`.`ordertable_OrderType` AS `CustomerType`,date_format(`ordertable`.`ordertable_OrderDateTime`,'%c/%Y') AS `OrderMonth`,sum(`ordertable`.`ordertable_CustPrice`) AS `TotalOrderPrice`,sum(`ordertable`.`ordertable_BusPrice`) AS `TotalOrderCost`,sum((`ordertable`.`ordertable_CustPrice` - `ordertable`.`ordertable_BusPrice`)) AS `Profit` from `ordertable` group by `CustomerType`,`OrderMonth` union select ' ' AS `customerType`,'Grand Total' AS `OrderMonth`,sum(`ordertable`.`ordertable_CustPrice`) AS `TotalOrderPrice`,sum(`ordertable`.`ordertable_BusPrice`) AS `TotalOrderCost`,sum((`ordertable`.`ordertable_CustPrice` - `ordertable`.`ordertable_BusPrice`)) AS `Profit` from `ordertable` order by `Profit` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `profitbypizza`
--

/*!50001 DROP VIEW IF EXISTS `profitbypizza`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `profitbypizza` AS select `pizza`.`pizza_Size` AS `Size`,`pizza`.`pizza_CrustType` AS `Crust`,sum((`pizza`.`pizza_CustPrice` - `pizza`.`pizza_BusPrice`)) AS `Profit`,date_format(`pizza`.`pizza_PizzaDate`,'%c/%Y') AS `OrderMonth` from `pizza` group by `pizza`.`pizza_Size`,`pizza`.`pizza_CrustType`,`OrderMonth` order by `Profit` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `toppingpopularity`
--

/*!50001 DROP VIEW IF EXISTS `toppingpopularity`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `toppingpopularity` AS select `T`.`topping_TopName` AS `Topping`,(case when ((count(`P`.`topping_TopID`) + sum(`P`.`pizza_topping_IsDouble`)) is null) then 0 else (count(`P`.`topping_TopID`) + sum(`P`.`pizza_topping_IsDouble`)) end) AS `ToppingCount` from (`topping` `T` left join `pizza_topping` `P` on((`P`.`topping_TopID` = `T`.`topping_TopID`))) group by `T`.`topping_TopName` order by `ToppingCount` desc,`T`.`topping_TopName` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-09 13:30:12

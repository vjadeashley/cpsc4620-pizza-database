package cpsc4620;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/*
 * This file is where you will implement the methods needed to support this application.
 * You will write the code to retrieve and save information to the database and use that
 * information to build the various objects required by the applicaiton.
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. 
 * DO NOT change these constant values.
 * 
 * You can add any helper methods you need, but you must implement all the methods
 * in this class and use them to complete the project.  The autograder will rely on
 * these methods being implemented, so do not delete them or alter their method
 * signatures.
 * 
 * Make sure you properly open and close your DB connections in any method that
 * requires access to the DB.
 * Use the connect_to_db below to open your connection in DBConnector.
 * What is opened must be closed!
 */

/*
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// DO NOT change these variables!
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "Small";
	public final static String size_m = "Medium";
	public final static String size_l = "Large";
	public final static String size_xl = "XLarge";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";

	public enum order_state {
		PREPARED,
		DELIVERED,
		PICKEDUP
	}


	private static boolean connect_to_db() throws SQLException, IOException 
	{

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * 
	 * 
	 * todo 
		printToppingReport()
		printProfitByPizzaReport()
		printProfitByOrderTypeReport()
	 */

	public static void addOrder(Order o) throws SQLException, IOException 
	{
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, pickup, pizzas, toppings
		 * on pizzas, order discounts and pizza discounts.
		 * 
		 * This is a KEY method as it must store all the data in the Order object
		 * in the database and make sure all the tables are correctly linked.
		 * 
		 * Remember, if the order is for Dine In, there is no customer...
		 * so the cusomter id coming from the Order object will be -1.
		 * 
		 */
		connect_to_db();

		try {
			java.util.Date now = new java.util.Date();

			String query;
			PreparedStatement os;

			if(o.getCustID() == -1) //dine in 
			{
				query = "INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_IsComplete) VALUES (NULL, ?, ?, ?, ?, ?);";
				os = conn.prepareStatement(query);
				os.setString(1, o.getOrderType());
				os.setTimestamp(2, new java.sql.Timestamp(now.getTime()));
				os.setDouble(3, o.getCustPrice());
				os.setDouble(4, o.getBusPrice());
				os.setBoolean(5, o.getIsComplete());
			}
			else
			{
				query = "INSERT INTO ordertable (customer_CustID, ordertable_OrderType, ordertable_OrderDateTime, ordertable_CustPrice, ordertable_BusPrice, ordertable_IsComplete) VALUES (?, ?, ?, ?, ?, ?);";
				os = conn.prepareStatement(query);
				os.setInt(1, o.getCustID());
				os.setString(2, o.getOrderType());
				os.setTimestamp(3, new java.sql.Timestamp(now.getTime()));
				os.setDouble(4, o.getCustPrice());
				os.setDouble(5, o.getBusPrice());
				os.setBoolean(6, o.getIsComplete());
			}
			os.executeUpdate();

			int newOrderID = -1;
			PreparedStatement getID = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			ResultSet idResult = getID.executeQuery();
			if(idResult.next())
			{
				newOrderID = idResult.getInt(1);
			}

			if(o instanceof DineinOrder)
			{
				DineinOrder d = (DineinOrder) o;
				PreparedStatement os2 = conn.prepareStatement("INSERT INTO dinein (ordertable_OrderID, dinein_TableNum) VALUES (?, ?);");
				os2.setInt(1, newOrderID);
				os2.setInt(2, d.getTableNum());
				os2.executeUpdate();
			}
			else if(o instanceof DeliveryOrder)
			{
				DeliveryOrder d = (DeliveryOrder) o;
				String[] parts = d.getAddress().split("\t");
				PreparedStatement os2 = conn.prepareStatement("INSERT INTO delivery (ordertable_OrderID, delivery_HouseNum, delivery_Street, delivery_City, delivery_State, delivery_Zip, delivery_IsDelivered) VALUES (?, ?, ?, ?, ?, ?, ?);");
				os2.setInt(1, newOrderID);
				os2.setInt(2, Integer.parseInt(parts[0])); 
				os2.setString(3, parts[1]);
				os2.setString(4, parts[2]);
				os2.setString(5, parts[3]);
				os2.setInt(6, Integer.parseInt(parts[4])); 
				os2.setBoolean(7, false);
				os2.executeUpdate(); 
			}
			else 
			{
				// lol pu 
				PickupOrder pu = (PickupOrder) o;
				PreparedStatement os2 = conn.prepareStatement("INSERT INTO pickup (ordertable_OrderID, pickup_IsPickedUp) VALUES (?, ?);");
				os2.setInt(1, newOrderID);
				os2.setBoolean(2, pu.getIsPickedUp());
				os2.executeUpdate();
			}

			for(Pizza p : o.getPizzaList())
			{
				addPizza(now, newOrderID, p);
			}

			for(Discount disc : o.getDiscountList())
			{
				PreparedStatement os3 = conn.prepareStatement("INSERT INTO order_discount (ordertable_OrderID, discount_DiscountID) VALUES (?, ?);");
				os3.setInt(1, newOrderID);
				os3.setInt(2, disc.getDiscountID());
				os3.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}
	
	public static int addPizza(java.util.Date d, int orderID, Pizza p) throws SQLException, IOException
	{
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind you must also add the pizza discounts and toppings 
		 * associated with the pizza.
		 * 
		 * NOTE: there is a Date object passed into this method so that the Order
		 * and ALL its Pizzas can be assigned the same DTS.
		 * 
		 * This method returns the id of the pizza just added.
		 * 
		 */

		int newPizzaID = -1;

		try {
			String query = "INSERT INTO pizza (pizza_Size, pizza_CrustType, ordertable_OrderID, pizza_PizzaState, pizza_PizzaDate, pizza_CustPrice, pizza_BusPrice) VALUES (?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement os = conn.prepareStatement(query);
			os.setString(1, p.getSize());
			os.setString(2, p.getCrustType());
			os.setInt(3, orderID);
			os.setString(4, p.getPizzaState());
			os.setTimestamp(5, new java.sql.Timestamp(d.getTime()));
			os.setDouble(6, p.getCustPrice());
			os.setDouble(7, p.getBusPrice());
			os.executeUpdate();

			PreparedStatement getID = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			ResultSet idResult = getID.executeQuery();
			if(idResult.next())
			{
				newPizzaID = idResult.getInt(1);
			}

			for(Topping t : p.getToppings())
			{
				PreparedStatement os2 = conn.prepareStatement("INSERT INTO pizza_topping (pizza_PizzaID, topping_TopID, pizza_topping_IsDouble) VALUES (?, ?, ?);");
				os2.setInt(1, newPizzaID);
				os2.setInt(2, t.getTopID());
				os2.setInt(3, t.getDoubled() ? 1 : 0);
				os2.executeUpdate();

				String sizeCol;
				if(p.getSize().equals(DBNinja.size_s)) sizeCol = "topping_SmallAMT";
				else if(p.getSize().equals(DBNinja.size_m)) sizeCol = "topping_MedAMT";
				else if(p.getSize().equals(DBNinja.size_l)) sizeCol = "topping_LgAMT";
				else sizeCol = "topping_XLAMT";

				PreparedStatement getAmt = conn.prepareStatement(
                "SELECT " + sizeCol + " FROM topping WHERE topping_TopID = ?;");
				getAmt.setInt(1, t.getTopID());
				ResultSet amtRset = getAmt.executeQuery();
				if(amtRset.next())
				{
					double amt = amtRset.getDouble(1);
					double multiplier = t.getDoubled() ? 2.0 : 1.0;
					PreparedStatement os3 = conn.prepareStatement(
						"UPDATE topping SET topping_CurINVT = topping_CurINVT - ? WHERE topping_TopID = ?;");
					os3.setDouble(1, amt * multiplier);
					os3.setInt(2, t.getTopID());
					os3.executeUpdate();
				}
			}
			

			for(Discount disc : p.getDiscounts())
			{
				PreparedStatement os4 = conn.prepareStatement("INSERT INTO pizza_discount (pizza_PizzaID, discount_DiscountID) VALUES (?, ?);");
				os4.setInt(1, newPizzaID);
				os4.setInt(2, disc.getDiscountID());
				os4.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newPizzaID;
	}

	
	public static int addCustomer(Customer c) throws SQLException, IOException
	 {
		/*
		 * This method adds a new customer to the database.
		 * 
		 */
		connect_to_db();

		int newID = -1; 

		try {
			String query = "INSERT INTO customer (customer_FName, customer_LName, customer_PhoneNum) VALUES (?, ?, ?);";
			PreparedStatement os = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			os.setString(1, c.getFName()); 
			os.setString(2, c.getLName()); 
			os.setString(3, c.getPhone()); 
			os.executeUpdate();
			ResultSet keys = os.getGeneratedKeys();
			if(keys.next()){
				newID = keys.getInt(1);
			} 
			conn.close();
			return newID;
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close(); 
		return newID; 
	}

	public static void completeOrder(int OrderID, order_state newState ) throws SQLException, IOException
	{
		/*
		 * Mark that order as complete in the database.
		 * Note: if an order is complete, this means all the pizzas are complete as well.
		 * However, it does not mean that the order has been delivered or picked up!
		 *
		 * For newState = PREPARED: mark the order and all associated pizza's as completed
		 * For newState = DELIVERED: mark the delivery status
		 * FOR newState = PICKEDUP: mark the pickup status
		 * 
		 */
		connect_to_db();

		try {
			if(newState == order_state.PREPARED)
			{
				PreparedStatement os = conn.prepareStatement(
					"UPDATE ordertable SET ordertable_IsComplete = 1 WHERE ordertable_OrderID = ?;");
				os.setInt(1, OrderID);
				os.executeUpdate();

				PreparedStatement os2 = conn.prepareStatement(
					"UPDATE pizza SET pizza_PizzaState = 'completed' WHERE ordertable_OrderID = ?;");
				os2.setInt(1, OrderID);
				os2.executeUpdate();
			}
			else if(newState == order_state.DELIVERED)
			{
				PreparedStatement os = conn.prepareStatement("UPDATE delivery SET delivery_IsDelivered = 1 WHERE ordertable_OrderID = ?;");
				os.setInt(1, OrderID);
				os.executeUpdate();
			}
			else if(newState == order_state.PICKEDUP)
			{
				PreparedStatement os = conn.prepareStatement("UPDATE pickup SET pickup_IsPickedUp = 1 WHERE ordertable_OrderID = ?;");
				os.setInt(1, OrderID);
				os.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}

	public static ArrayList<Order> getOrders(int status) throws SQLException, IOException
	 {
	/*
	 * Return an ArrayList of orders.
	 * 	status   == 1 => return a list of open (ie oder is not completed)
	 *           == 2 => return a list of completed orders (ie order is complete)
	 *           == 3 => return a list of all the orders
	 * Remember that in Java, we account for supertypes and subtypes
	 * which means that when we create an arrayList of orders, that really
	 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
	 *
	 * You must fully populate the Order object, this includes order discounts,
	 * and pizzas along with the toppings and discounts associated with them.
	 * 
	 * Don't forget to order the data according to their order sequence, ie, order 1, order 2, etc.
	 *
	 */
		connect_to_db(); 

		ArrayList<Order> orders = new ArrayList<Order>(); 

		try{
			String query = ""; 
			if(status == 1){
				query = "SELECT * FROM ordertable WHERE ordertable_IsComplete = 0 ORDER BY ordertable_OrderID;";
			}else if(status == 2){
				query = "SELECT * FROM ordertable WHERE ordertable_IsComplete = 1 ORDER BY ordertable_OrderID;";
			}else if(status == 3){
				query = "SELECT * FROM ordertable ORDER BY ordertable_OrderID;";
			}else{
				System.out.println("Error: status code error"); 
			}

			PreparedStatement os = conn.prepareStatement(query);
			ResultSet rset = os.executeQuery();

			while(rset.next())
			{
				int orderid = rset.getInt("ordertable_OrderID");
				int custid = rset.getInt("customer_CustID");
				String ordertype = rset.getString("ordertable_OrderType");
				String date = rset.getString("ordertable_OrderDateTime");
				double custprice = rset.getDouble("ordertable_CustPrice");
				double busprice = rset.getDouble("ordertable_BusPrice");
				boolean iscomplete = rset.getBoolean("ordertable_IsComplete");

				Order o = null;

				if(ordertype.equals(DBNinja.dine_in))
				{
					PreparedStatement os2 = conn.prepareStatement("SELECT * FROM dinein WHERE ordertable_OrderID = ?;");
					os2.setInt(1, orderid);
					ResultSet rset2 = os2.executeQuery();
					if(rset2.next())
					{
						int tablenum = rset2.getInt("dinein_TableNum");
						o = new DineinOrder(orderid, custid, date, custprice, busprice, iscomplete, tablenum);
					}
				}
				else if(ordertype.equals(DBNinja.delivery))
				{
					PreparedStatement os2 = conn.prepareStatement("SELECT * FROM delivery WHERE ordertable_OrderID = ?;");
					os2.setInt(1, orderid);
					ResultSet rset2 = os2.executeQuery();
					if(rset2.next())
					{
						// please autograder accept this! 
						String address = rset2.getInt("delivery_HouseNum") + "\t" + 
							rset2.getString("delivery_Street") + "\t" +
							rset2.getString("delivery_City") + "\t" +
							rset2.getString("delivery_State") + "\t" +
							rset2.getInt("delivery_Zip");
						boolean isdelivered = rset2.getBoolean("delivery_IsDelivered");
						o = new DeliveryOrder(orderid, custid, date, custprice, busprice, iscomplete, isdelivered, address);
					}
				}
				else // pickup
				{
					PreparedStatement os2 = conn.prepareStatement("SELECT * FROM pickup WHERE ordertable_OrderID = ?;");
					os2.setInt(1, orderid);
					ResultSet rset2 = os2.executeQuery();
					if(rset2.next())
					{
						boolean ispickedup = rset2.getBoolean("pickup_IsPickedUp");
						o = new PickupOrder(orderid, custid, date, custprice, busprice, ispickedup, iscomplete);
					}
				}

				if(o != null)
				{
					o.setPizzaList(getPizzas(o));
					o.setDiscountList(getDiscounts(o));
					orders.add(o);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
		return orders;
	}
	
	public static Order getLastOrder() throws SQLException, IOException 
	{
		/*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there will ALWAYS be a "last order"!
		 */
		ArrayList<Order> all = getOrders(3); 
		return all.get(all.size() - 1); 
	}

	public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException
	 {
		/*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *  
		 */
		ArrayList<Order> all = getOrders(3); 
		ArrayList<Order> order = new ArrayList<Order>(); 

		for(Order o : all){
			if(o.getDate().substring(0, 10).equals(date)){
				order.add(o); 
			}
		}

		return order; 
	}
		
	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException 
	{
		/* 
		 * Query the database for all the available discounts and 
		 * return them in an arrayList of discounts ordered by discount name.
		 * 
		*/
		connect_to_db(); 

		ArrayList<Discount> discount = new ArrayList<Discount>();  

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT * FROM discount ORDER BY discount_DiscountName;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while(rset.next()){
				int id = rset.getInt("discount_DiscountID"); 
				String discountname = rset.getString("discount_DiscountName"); 
				Double amount = rset.getDouble("discount_Amount"); 
				Boolean ispercent = rset.getBoolean("discount_IsPercent");  

				Discount d = new Discount(id, discountname, amount, ispercent); 
				discount.add(d); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close(); 
		return discount;
	}

	public static Discount findDiscountByName(String name) throws SQLException, IOException 
	{
		/*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *  
		 */
		 connect_to_db(); 

		 Discount d = null; 

		 try{
			String query = "SELECT * FROM discount WHERE discount_DiscountName = ?;"; 
			PreparedStatement os = conn.prepareStatement(query); 
			os.setString(1, name); 
			ResultSet rset = os.executeQuery(); 

			if(rset.next()){
				int id = rset.getInt("discount_DiscountID"); 
				String discountname = rset.getString("discount_DiscountName"); 
				Double amount = rset.getDouble("discount_Amount"); 
				Boolean ispercent = rset.getBoolean("discount_IsPercent");  

				d = new Discount(id, discountname, amount, ispercent); 
			}
		 } catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close(); 
		return d; 
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException 
	{
		/*
		 * Query the data for all the customers and return an arrayList of all the customers. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 * pretty much did this exactly like the example that was given 
		 * 
		*/
		connect_to_db(); 

		ArrayList<Customer> customers = new ArrayList<Customer>(); 

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT * FROM customer ORDER BY customer_LName, customer_FName, customer_PhoneNum;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while(rset.next()){
				int id = rset.getInt("customer_CustID"); 
				String fname = rset.getString("customer_FName"); 
				String lname = rset.getString("customer_LName"); 
				String phone = rset.getString("customer_PhoneNum"); 

				Customer c = new Customer(id, fname, lname, phone); 
				customers.add(c); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close(); 
		return customers; 
	}

	public static Customer findCustomerByPhone(String phoneNumber)  throws SQLException, IOException 
	{
		/*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *  
		 */
		 connect_to_db(); 

         Customer c = null; 

         try{
            String query = "SELECT * FROM customer WHERE customer_PhoneNum = ?;"; 
            PreparedStatement os = conn.prepareStatement(query); 
            os.setString(1, phoneNumber); 
            ResultSet rset = os.executeQuery(); 

            if(rset.next()){
                int id = rset.getInt("customer_CustID"); 
				String fname = rset.getString("customer_FName"); 
				String lname = rset.getString("customer_LName"); 
				String phone = rset.getString("customer_PhoneNum"); 

				c = new Customer(id, fname, lname, phone); 
            }
         } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        conn.close(); 
        return c; 
	}

	public static String getCustomerName(int CustID) throws SQLException, IOException 
	{
		/*
		 * COMPLETED...WORKING Example!
		 * 
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with
		 * your database from Java.  
		 * 
		 * Notice how the connection to the DB made at the start of the 
		 *
		 */

		 connect_to_db();

		/* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
		String cname1 = "";
		String cname2 = "";
		String query = "Select customer_FName, customer_LName From customer WHERE customer_CustID=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);
		
		while(rset.next())
		{
			cname1 = rset.getString(1) + " " + rset.getString(2); 
		}

		/* 
		* an BETTER example of the same query using a prepared statement...
		* with exception handling
		* 
		*/
		try {
			PreparedStatement os;
			ResultSet rset2;
			String query2;
			query2 = "Select customer_FName, customer_LName From customer WHERE customer_CustID=?;";
			os = conn.prepareStatement(query2);
			os.setInt(1, CustID);
			rset2 = os.executeQuery();
			while(rset2.next())
			{
				cname2 = rset2.getString("customer_FName") + " " + rset2.getString("customer_LName"); // note the use of field names in the getSting methods
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close();

		return cname1;
		// OR
		// return cname2;

	}


	public static ArrayList<Topping> getToppingList() throws SQLException, IOException 
	{
		/*
		 * Query the database for the aviable toppings and 
		 * return an arrayList of all the available toppings. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */
		connect_to_db(); 

		ArrayList<Topping> topping = new ArrayList<Topping>();  

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT * FROM topping ORDER BY topping_TopName;";
			os = conn.prepareStatement(query);
			rset = os.executeQuery();
			while(rset.next()){
				int id = rset.getInt("topping_TopID"); 
				String topname = rset.getString("topping_TopName"); 
				Double smallamt = rset.getDouble("topping_SmallAMT");
				Double medamt = rset.getDouble("topping_MedAMT");
				Double lgamt = rset.getDouble("topping_LgAMT"); 
				Double xlamt = rset.getDouble("topping_XLAMT");
				Double custprice = rset.getDouble("topping_CustPrice");
				Double busprice = rset.getDouble("topping_BusPrice");
				int mininvt = rset.getInt("topping_MinINVT"); 
				int curinvt = rset.getInt("topping_CurINVT");

				Topping t = new Topping(id, topname, smallamt, medamt, lgamt, xlamt, custprice, busprice, mininvt, curinvt); 
				topping.add(t); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		conn.close(); 
		return topping;
	}

	public static Topping findToppingByName(String name) throws SQLException, IOException 
	{
		/*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *  
		 */
		 connect_to_db(); 

         Topping t = null; 

         try{
            String query = "SELECT * FROM topping WHERE topping_TopName = ?;"; 
            PreparedStatement os = conn.prepareStatement(query); 
            os.setString(1, name); 
            ResultSet rset = os.executeQuery(); 

            if(rset.next()){
                int id = rset.getInt("topping_TopID"); 
				String topname = rset.getString("topping_TopName"); 
				Double smallamt = rset.getDouble("topping_SmallAMT");
				Double medamt = rset.getDouble("topping_MedAMT");
				Double lgamt = rset.getDouble("topping_LgAMT"); 
				Double xlamt = rset.getDouble("topping_XLAMT");
				Double custprice = rset.getDouble("topping_CustPrice");
				Double busprice = rset.getDouble("topping_BusPrice");
				int mininvt = rset.getInt("topping_MinINVT"); 
				int curinvt = rset.getInt("topping_CurINVT");

				t = new Topping(id, topname, smallamt, medamt, lgamt, xlamt, custprice, busprice, mininvt, curinvt); 
            }
         } catch (SQLException e) {
            e.printStackTrace();
            // process the error or re-raise the exception to a higher level
        }

        conn.close(); 
        return t; 
	}

	public static ArrayList<Topping> getToppingsOnPizza(Pizza p) throws SQLException, IOException 
	{
		/* 
		 * This method builds an ArrayList of the toppings ON a pizza.
		 * The list can then be added to the Pizza object elsewhere in the
		 */

		ArrayList<Topping> topping = new ArrayList<Topping>();  

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT t.*, pt.pizza_topping_IsDouble FROM topping t JOIN pizza_topping pt ON t.topping_TopID = pt.topping_TopID WHERE pt.pizza_PizzaID = ?;";
			os = conn.prepareStatement(query); 
			os.setInt(1, p.getPizzaID()); 
			rset = os.executeQuery();
			while(rset.next()){
				int id = rset.getInt("topping_TopID"); 
				String topname = rset.getString("topping_TopName"); 
				Double smallamt = rset.getDouble("topping_SmallAMT");
				Double medamt = rset.getDouble("topping_MedAMT");
				Double lgamt = rset.getDouble("topping_LgAMT"); 
				Double xlamt = rset.getDouble("topping_XLAMT");
				Double custprice = rset.getDouble("topping_CustPrice");
				Double busprice = rset.getDouble("topping_BusPrice");
				int mininvt = rset.getInt("topping_MinINVT"); 
				int curinvt = rset.getInt("topping_CurINVT");

				Topping t = new Topping(id, topname, smallamt, medamt, lgamt, xlamt, custprice, busprice, mininvt, curinvt);
				t.setDoubled(rset.getInt("pizza_topping_IsDouble") == 1);
				topping.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}
		return topping;
	}	
	

	public static void addToInventory(int toppingID, double quantity) throws SQLException, IOException 
	{
		/*
		 * Updates the quantity of the topping in the database by the amount specified.
		 * 
		 * */
		connect_to_db(); 

		try {
			String query = "UPDATE topping SET topping_CurINVT = topping_CurINVT + ? WHERE topping_TopID = ?;"; 
			PreparedStatement os = conn.prepareStatement(query); 
			os.setDouble(1, quantity); 
			os.setInt(2, toppingID); 
			os.executeUpdate(); 
		} catch(SQLException e){
			e.printStackTrace(); 
		}

		conn.close(); 
	}
	
	
	public static ArrayList<Pizza> getPizzas(Order o) throws SQLException, IOException 
	{
		/*
		 * Build an ArrayList of all the Pizzas associated with the Order.
		 * 
		 */
		
		ArrayList<Pizza> pizzas = new ArrayList<Pizza>(); 

		try{
			String query = "SELECT * FROM pizza WHERE ordertable_OrderID = ?;";
			PreparedStatement os = conn.prepareStatement(query);
			os.setInt(1, o.getOrderID());
			ResultSet rset = os.executeQuery();

			while(rset.next()){
				int id = rset.getInt("pizza_PizzaID");
				String size = rset.getString("pizza_Size");
				String crust = rset.getString("pizza_CrustType");
				int orderID = rset.getInt("ordertable_OrderID");
				String state = rset.getString("pizza_PizzaState");
				String date = rset.getString("pizza_PizzaDate");
				double custprice = rset.getDouble("pizza_CustPrice");
				double busprice = rset.getDouble("pizza_BusPrice");

				Pizza p = new Pizza(id, size, crust, orderID, state, date, custprice, busprice);

				p.setToppings(getToppingsOnPizza(p));
				p.setDiscounts(getDiscounts(p));

				pizzas.add(p);
			}
		} catch (SQLException e){
			e.printStackTrace(); 
		}

		return pizzas; 
	}

	public static ArrayList<Discount> getDiscounts(Order o) throws SQLException, IOException 
	{
		/* 
		 * Build an array list of all the Discounts associted with the Order.
		 * 
		 */

		ArrayList<Discount> discount = new ArrayList<Discount>();  

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT d.* FROM discount d JOIN order_discount od ON d.discount_DiscountID = od.discount_DiscountID WHERE od.ordertable_OrderID = ?;";
			os = conn.prepareStatement(query); 
			os.setInt(1, o.getOrderID()); 
			rset = os.executeQuery();
			while(rset.next()){
				int id = rset.getInt("discount_DiscountID"); 
				String discountname = rset.getString("discount_DiscountName"); 
				Double amount = rset.getDouble("discount_Amount"); 
				Boolean ispercent = rset.getBoolean("discount_IsPercent");  

				Discount d = new Discount(id, discountname, amount, ispercent); 
				discount.add(d); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		return discount;
	}

	public static ArrayList<Discount> getDiscounts(Pizza p) throws SQLException, IOException 
	{
		/* 
		 * Build an array list of all the Discounts associted with the Pizza.
		 * 
		 */

		ArrayList<Discount> discount = new ArrayList<Discount>();  

		try {
			PreparedStatement os;
			ResultSet rset;
			String query;
			query = "SELECT d.* FROM discount d JOIN pizza_discount pd ON d.discount_DiscountID = pd.discount_DiscountID WHERE pd.pizza_PizzaID = ?;";
			os = conn.prepareStatement(query); 
			os.setInt(1, p.getPizzaID()); 
			rset = os.executeQuery();
			while(rset.next()){
				int id = rset.getInt("discount_DiscountID"); 
				String discountname = rset.getString("discount_DiscountName"); 
				Double amount = rset.getDouble("discount_Amount"); 
				Boolean ispercent = rset.getBoolean("discount_IsPercent");  

				Discount d = new Discount(id, discountname, amount, ispercent); 
				discount.add(d); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// process the error or re-raise the exception to a higher level
		}

		return discount;
	}

	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException 
	{
		/* 
		 * Query the database fro the base customer price for that size and crust pizza.
		 * 
		*/
		// baseprice_Size baseprice_CrustType baseprice_CustPrice baseprice_BusPrice 
		connect_to_db(); 

		double price = 0.0; 

		try{
			String query = "SELECT baseprice_CustPrice FROM baseprice WHERE baseprice_Size = ? AND baseprice_CrustType = ?;"; 
			PreparedStatement os = conn.prepareStatement(query); 
			os.setString(1, size); 
			os.setString(2, crust); 
			ResultSet rset = os.executeQuery(); 

			if(rset.next()){
				price = rset.getDouble("baseprice_CustPrice"); 
			}
		} catch (SQLException e){
			e.printStackTrace(); 
		}

		conn.close(); 
		return price; 
	}

	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException 
	{
		/* 
		 * Query the database fro the base business price for that size and crust pizza.
		 * 
		*/
		connect_to_db(); 

		double price = 0.0; 

		try{
			String query = "SELECT baseprice_BusPrice FROM baseprice WHERE baseprice_Size = ? AND baseprice_CrustType = ?;"; 
			PreparedStatement os = conn.prepareStatement(query); 
			os.setString(1, size); 
			os.setString(2, crust); 
			ResultSet rset = os.executeQuery(); 

			if(rset.next()){
				price = rset.getDouble("baseprice_BusPrice"); 
			}
		} catch (SQLException e){
			e.printStackTrace(); 
		}

		conn.close(); 
		return price; 
	}

	
	public static void printToppingReport() throws SQLException, IOException
	{
		/*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 * HINT: You need to match the expected output EXACTLY....I would suggest
		 * you look at the printf method (rather that the simple print of println).
		 * It operates the same in Java as it does in C and will make your code
		 * better.
		 * 
		 */
		connect_to_db();

		try {
			String query = "SELECT * FROM ToppingPopularity ORDER BY ToppingCount DESC;";
			PreparedStatement os = conn.prepareStatement(query);
			ResultSet rset = os.executeQuery();

			System.out.printf("%-20s %-20s%n", "Topping", "Topping Count");
			System.out.printf("%-20s %-20s%n", "-------", "-------------");

			while(rset.next())
			{
				String topping = rset.getString("Topping");
				int count = rset.getInt("ToppingCount");
				System.out.printf("%-20s %-20d%n", topping, count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}
	
	public static void printProfitByPizzaReport() throws SQLException, IOException 
	{
		/*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 * HINT: You need to match the expected output EXACTLY....I would suggest
		 * you look at the printf method (rather that the simple print of println).
		 * It operates the same in Java as it does in C and will make your code
		 * better.
		 * 
		 */
		connect_to_db();

		try {
			String query = "SELECT * FROM ProfitByPizza ORDER BY Profit ASC;";
			PreparedStatement os = conn.prepareStatement(query);
			ResultSet rset = os.executeQuery();

			System.out.printf("%-20s %-20s %-20s %-20s%n", "Pizza Size", "Pizza Crust", "Profit", "Last Order Date");
			System.out.printf("%-20s %-20s %-20s %-20s%n", "----------", "-----------", "------", "---------------");

			while(rset.next())
			{
				String size = rset.getString("Size");
				String crust = rset.getString("Crust");
				double profit = rset.getDouble("Profit");
				String orderMonth = rset.getString("OrderMonth");
				System.out.printf("%-20s %-20s %-20.2f %-20s%n", size, crust, profit, orderMonth);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}
	
	public static void printProfitByOrderTypeReport() throws SQLException, IOException
	{
		/*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 *
		 * HINT: You need to match the expected output EXACTLY....I would suggest
		 * you look at the printf method (rather that the simple print of println).
		 * It operates the same in Java as it does in C and will make your code
		 * better.
		 * 
		 */
		connect_to_db();

		try {
			String query = "SELECT * FROM ProfitByOrderType ORDER BY CustomerType, OrderMonth;";
			PreparedStatement os = conn.prepareStatement(query);
			ResultSet rset = os.executeQuery();

			System.out.printf("%-20s %-20s %-20s %-20s %-20s%n", "Customer Type", "Order Month", "Total Order Price", "Total Order Cost", "Profit");
			System.out.printf("%-20s %-20s %-20s %-20s %-20s%n", "-------------", "-----------", "-----------------", "----------------", "------");

			double totalPrice = 0.0;
			double totalCost = 0.0;
			double totalProfit = 0.0;

			while(rset.next())
			{
				String custType = rset.getString("CustomerType");
				String orderMonth = rset.getString("OrderMonth");
				double price = rset.getDouble("TotalOrderPrice");
				double cost = rset.getDouble("TotalOrderCost");
				double profit = rset.getDouble("Profit");
				System.out.printf("%-20s %-20s %-20.2f %-20.2f %-20.2f%n", custType, orderMonth, price, cost, profit);
		}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		conn.close();
	}
	
	
	/*
	 * These private methods help get the individual components of an SQL datetime object. 
	 * You're welcome to keep them or remove them....but they are usefull!
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}
	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}
	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


}
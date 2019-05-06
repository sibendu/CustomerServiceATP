package sd.example.atp;
/* Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.*/

/*
   DESCRIPTION    
   The code sample shows how to use the DataSource API to establish a connection
   to the Database. You can specify properties with "setConnectionProperties".
   This is the recommended way to create connections to the Database.
   Note that an instance of oracle.jdbc.pool.OracleDataSource doesn't provide
   any connection pooling. It's just a connection factory. A connection pool,
   such as Universal Connection Pool (UCP), can be configured to use an
   instance of oracle.jdbc.pool.OracleDataSource to create connections and 
   then cache them.
    
    Step 1: Enter the Database details in this file. 
            DB_USER, DB_PASSWORD and DB_URL are required
    Step 2: Run the sample with "ant DataSourceSample"
  
   NOTES
    Use JDK 1.7 and above
   MODIFIED    (MM/DD/YY)
    nbsundar    02/17/15 - Creation 
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;

public class DataSourceSample {
	// The recommended format of a connection URL is the long format with the
	// connection descriptor.

	// final static String DB_URL=
	// "jdbc:oracle:thin:@myhost:1521/myorcldbservicename";
	// For ATP and ADW - use the TNS Alias name along with the TNS_ADMIN when using
	// 18.3 JDBC driver
	// final static String
	// DB_URL="jdbc:oracle:thin:@wallet_dbname?TNS_ADMIN=/Users/test/wallet_dbname";
	// In case of windows, use the following URL
	final static String DB_URL = "jdbc:oracle:thin:@sddemoatp_high?TNS_ADMIN=/Wallet_demoatp";// D:\\workshop\\atp\\CustomerServiceATP\\Wallet_sddemoatp";
	final static String DB_USER = "admin";
	final static String DB_PASSWORD = "PAssword_12#";

	/*
	 * The method gets a database connection using
	 * oracle.jdbc.pool.OracleDataSource. It also sets some connection level
	 * properties, such as,
	 * OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH,
	 * OracleConnection.CONNECTION_PROPERTY_THIN_NET_CHECKSUM_TYPES, etc., There are
	 * many other connection related properties. Refer to the OracleConnection
	 * interface to find more.
	 */
	public static void main(String args[]) throws SQLException {
		DataSourceSample ds = new DataSourceSample();
		ds.getCustomer("1");
	}

	public Customer getCustomer(String id) throws SQLException {
		
		System.out.println("Retrieving record for Id = " + id);
		Customer cust = null;
		
		Properties info = new Properties();
		info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
		info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
		info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");

		OracleDataSource ods = new OracleDataSource();
		ods.setURL(DB_URL);
		ods.setConnectionProperties(info);

		// With AutoCloseable, the connection is closed automatically.
		try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
			DatabaseMetaData dbmd = connection.getMetaData();
			System.out.println("Driver: " + dbmd.getDriverName() + " :: " + dbmd.getDriverVersion() + " :: "
					+ connection.getDefaultRowPrefetch() + "  :: " + connection.getUserName());
			cust = getCustomer(connection,id);
		}
		
		return cust;
	}

	/*
	 * Displays first_name and last_name from the employees table.
	 */
	public Customer getCustomer(Connection connection, String id) throws SQLException {

		Customer cust = null;
		try (Statement statement = connection.createStatement()) {

			String query = "select id, name, address from CUSTOMER where id = " + id;
			// query = "SELECT CHANNEl_DESC, CHANNEL_CLASS FROM SH.CHANNELS";

			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				cust = new Customer(new Integer(id), resultSet.getString(2), resultSet.getString(3));
			}
		}
		
		if(cust == null) {
			cust = new Customer(new Integer(-999), "NOT_FOUND", null);
		}
		
		System.out.println("Customer : "+cust.getId()+ " :: "+cust.getName()+" :: "+cust.getAddress()); 
		
		return cust;
	}
}
## csv jdbc driver

I wrote a "csv-jdbc-driver" just for fun. It is far from production quality code, my goal was only to demonstrate (for myself) how to write a jdbc driver.

Here are my experiences:
* As others wrote, it is enough for implementing 4 interfaces from java.sql: Driver, Statement, Connection, ResultSet.
* To know, which method should be implemented with "real code", I have to know, how I want to use the csv driver. This is my example code:

           try (
       Connection conn = DriverManager.getConnection("jdbc:csv:/home/peter/csvdir");
       Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery("SELECT * FROM test.csv")
       ){
           while (rs.next()) System.out.println(rs.getString(1) + " - " + rs.getString(2));
       }

I give a jdbc url to DriverManager.getConnection. In the url I have to specify a directory, where different csv files are. The DriverManager getConnection method calls Driver.connect(url, info). This connect method should check the jdbc url and decides, if it can deal with the url or not. If yes, it returns with a Connection object, if not, returns with null.

In the stms.executeQuery I have to give an SQL select, where the "table name" is the name of the csv file.
I do not want to implement an sql parser, so this jdbc driver take into account only the table name.

I also have to use the ResultSet.next() and ResultSet.getString(int) methodes as a bare minimum.

So what I have to implement:
* Driver.connect(String, Properties). Because this method returns with a Connection class
* Connection constructor
* Connection.createStatement method. Because this returns with a Statement class
* Statement constructor
* Statement.executeQuery. Because this returns with a ResultSet class
* ResultSet constructor
* ResultSet.getString(int)
* ResultSet.next()

I also created a service provider file in src/main/resources/MET-INF/services/java.sql.Driver with the contents of org.example.CsvDriver (my Driver implementation class name)

I thought, that these would be enough, but not. My driver loaded, but the DriverManager did not find it.

I also had to call DriverManager.registerDriver(INSTANCE) from my Driver implementation's static initializer, where INSTANCE is an object from my Driver implementation. It seems to me superfluous (because I wrote a java service for avoiding this).

See the usage example (csv-jdbc-driver-example):
https://github.com/peterborkuti/csv-jdbc-driver-example

See the csv-jdbc-driver:
https://github.com/peterborkuti/csv-jdbc-driver

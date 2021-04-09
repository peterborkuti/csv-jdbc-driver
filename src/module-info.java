module CsvSqlDriver {
	requires java.sql;
	provides java.sql.Connection with org.example.CsvConnection;
	provides java.sql.Driver with org.example.CsvDriver;
	provides java.sql.ResultSet with org.example.CsvResultset;
	provides java.sql.Statement with org.example.CsvStatement;
}
module CsvSqlDriver {
	requires java.sql;
	provides java.sql.Driver with org.example.CsvDriver;
}
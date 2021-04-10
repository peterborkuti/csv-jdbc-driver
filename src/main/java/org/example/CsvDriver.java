package org.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class CsvDriver implements Driver {
	private static final Driver INSTANCE = new CsvDriver();
	private static boolean registered;
	public CsvDriver() {}

	@Override
	public Connection connect(String s, Properties properties) throws SQLException {
		String[] parts = s.split(":");

		if (parts.length < 2 ||	!parts[0].toLowerCase().equals("jdbc") || !parts[1].toLowerCase().equals("csv"))
			return null;

		if (parts.length != 3) throw new IllegalArgumentException("Connection string format should be jdbc:csv:directory");

		Path path = Paths.get(parts[2]).toAbsolutePath();

		if (!Files.isDirectory(path)) throw new SQLException("'" + path + "' is not a directory");

		return new CsvConnection(path);
	}

	@Override
	public boolean acceptsURL(String s) throws SQLException {
		return true;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	@Override
	public int getMajorVersion() {
		return 0;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return true;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	public static synchronized Driver load() throws SQLException {
		if (!registered) {
			registered = true;
			DriverManager.registerDriver(INSTANCE);
		}

		return INSTANCE;
	}

	static {
		try {
			load();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}

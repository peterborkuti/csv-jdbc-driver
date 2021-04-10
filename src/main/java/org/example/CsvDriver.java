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
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CsvDriver implements Driver {
	private static final Driver INSTANCE = new CsvDriver();
	private static boolean registered;
	public CsvDriver() {}

	@Override
	public Connection connect(String s, Properties properties) throws SQLException {
		String[] parts = s.split(":");

		if (parts.length < 2 ||	!parts[0].toLowerCase().equals("jdbc") || !parts[1].toLowerCase().equals("csv"))
			return null;

		String directory = Arrays.stream(parts).skip(2).collect(Collectors.joining(":"));

		Path path = Paths.get(directory).toAbsolutePath();

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

	public static synchronized Driver load() {
		if (!registered) {
			registered = true;
			try {
				DriverManager.registerDriver(INSTANCE);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}

		return INSTANCE;
	}

	static {
		load();
	}
}

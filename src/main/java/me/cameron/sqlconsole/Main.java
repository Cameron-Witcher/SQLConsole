package me.cameron.sqlconsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static IDatabase database;
	public static SQLDriver driver;

	public static void main(String[] args) {

//		System.out.println(new Date().getTime());
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		if (args.length == 0) {
			try {
				String s = "";
				for (SQLDriver driver : SQLDriver.values()) {
					s = s + driver.argname() + ", ";
				}
				s = s.substring(0, s.length() - 2);
				
				
				boolean done = false;
				while (!done) {
					System.out.println("What driver would you like to use? [" + s + "]");
					String d = reader.readLine();
					for (SQLDriver driver : SQLDriver.values()) {
						if (d.equalsIgnoreCase(driver.argname())) {
							Main.driver = driver;
							done = true;
							break;
						}
					}
				}
				System.out.println("What database would you like to use?");
				String db = reader.readLine();
				if (driver.equals(SQLDriver.MYSQL)) {
					database = new IDatabase(driver, "144.217.12.232", "Minecraft", 3306, "root", "v4pob8LW");
				}
				if (driver.equals(SQLDriver.SQLITE)) {
					database = new IDatabase(driver, db);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!database.init())
			System.out.println("Could not connect to SQL.");
		else
			System.out.println("Connected to SQL.");

		while (true)
			try {
				String name = reader.readLine();

				if (name.toUpperCase().startsWith("SELECT")) {
					ResultSet rs = database.query(name);
					int a = 1;
					while (rs.next()) {
						a = a + 1;
						System.out.println("-------Row " + a + "-------");
						for (int i = 1; i != rs.getMetaData().getColumnCount() + 1; i++) {
							System.out.println(rs.getMetaData().getColumnName(i) + ": " + rs.getString(i));
						}
						System.out.println("--------------------");
						System.out.println();
					}
				}

				if (name.toUpperCase().startsWith("INSERT") || name.toUpperCase().startsWith("CREATE")) {
					if (!database.input(name))
						System.out.println("There was an error sending that command.");
					else
						System.out.println("Success!");
				}

				if (name.toUpperCase().startsWith("UPDATE") | name.toUpperCase().startsWith("DELETE")) {
					System.out.println(database.update(name));
				}

			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}

	}
}

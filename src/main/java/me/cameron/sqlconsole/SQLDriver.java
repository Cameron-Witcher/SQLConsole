package me.cameron.sqlconsole;

public enum SQLDriver {
	
	SQLITE("sqlite"),
	MYSQL("mysql");
	
	String name;
	
	SQLDriver(String name){
		this.name = name;
	}
	
	public String argname() {
		return name;
	}

}

package main.dto;

public class Employee {
	
	private int id;
	private String name;
	private boolean manager;
	private double salary;
	private Address address;
	private String[] domains;
	private Address[] familyAddresses;
	
	private Employee(int id) {
		this.id = id;
		this.name = "Default";
	}
	
	public Employee(int id, String name) {
		this.id = id;
		this.name = name;
		this.manager = false;
	}
	
	public Employee(int id, String name, boolean manager) {
		this.id = id;
		this.name = name;
		this.manager = manager;
	}
	
	public Employee(int id, String name, boolean manager, double salary, Address address, String[] domains, Address[] familyAddresses) {
		this.id = id;
		this.name = name;
		this.manager = manager;
		this.salary = salary;
		this.address = address;
		this.domains = domains;
		this.familyAddresses = familyAddresses;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isManager() {
		return manager;
	}
	public void setManager(boolean manager) {
		this.manager = manager;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", manager=" + manager + "]";
	}
	
}

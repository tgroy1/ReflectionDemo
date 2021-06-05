package main.dto;

import java.util.Arrays;

public class GameConfig {
	
	private String name;
	private int year;
	private double price;
	private String[] characters;
	
	public String getName() {
		return name;
	}
	public int getYear() {
		return year;
	}
	public double getPrice() {
		return price;
	}
	public String[] getCharacters() {
		return characters;
	}
	
	@Override
	public String toString() {
		return "GameConfig [name=" + name + ", year=" + year + ", price=" + price + ", characters="
				+ Arrays.toString(characters) + "]";
	}
}

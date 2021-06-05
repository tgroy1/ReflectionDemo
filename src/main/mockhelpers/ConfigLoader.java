package main.mockhelpers;

import main.annotations.InitializeClass;
import main.annotations.InitializeMethod;

@InitializeClass
public class ConfigLoader {
	
	@InitializeMethod
	public void loadConfig() {
		System.out.println("Loading configuration");
	}
	
}

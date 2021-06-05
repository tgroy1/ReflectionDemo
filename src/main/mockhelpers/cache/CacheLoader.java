package main.mockhelpers.cache;

import main.annotations.InitializeClass;
import main.annotations.InitializeMethod;

@InitializeClass
public class CacheLoader {
	
	@InitializeMethod
	public void loadCache() {
		System.out.println("Loading cache data");
	}
	
	public void reloadCache() {
		System.out.println("Reloading cache data");
	}
}

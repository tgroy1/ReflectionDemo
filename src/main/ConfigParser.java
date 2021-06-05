package main;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import main.dto.GameConfig;

public class ConfigParser {
	
	private static final Path CONFIG_FILE_PATH = Paths.get("resources/game.cfg");

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		
		GameConfig gameConfig = createConfig(GameConfig.class, CONFIG_FILE_PATH);
		
		System.out.println(gameConfig);
	}
	
	
	private static <T> T createConfig(Class<?> clazz, Path filePath) throws IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Scanner scanner = new Scanner(filePath);
		
		Constructor<?> constructor = clazz.getDeclaredConstructor();
		constructor.setAccessible(true);
		
		T instance = (T) constructor.newInstance();
		
		while(scanner.hasNextLine()) {
			
			String config = scanner.nextLine();
			
			String[] nameValuePair = config.split("=");
			String propertyName = nameValuePair[0];
			String propertyValue = nameValuePair[1];
			
			Field field;
			try {
				field = clazz.getDeclaredField(propertyName);
			} catch(NoSuchFieldException e) {
				System.out.println(String.format("Property name : %s is not supported", propertyName));
				continue;
			}
			
			field.setAccessible(true);
			Object parsedValue; 
			if (field.getType().isArray()) {
				parsedValue = parseArray(field.getType().getComponentType(), propertyValue);
			} else {
				parsedValue = parseValue(field.getType(), propertyValue);
			}
			
			field.set(instance, parsedValue);
		}
		
		return instance;
	}

	private static Object parseValue(Class<?> type, String propertyValue) {
		
		if (type.equals(int.class)) {
			return Integer.parseInt(propertyValue);
		} else if (type.equals(short.class)) {
			return Short.parseShort(propertyValue);
		} else if (type.equals(long.class)) {
			return Long.parseLong(propertyValue);
		} else if (type.equals(boolean.class)) {
			return Boolean.parseBoolean(propertyValue);
		} else if (type.equals(float.class)) {
			return Float.parseFloat(propertyValue);
		} else if (type.equals(double.class)) {
			return Double.parseDouble(propertyValue);
		} else if (type.equals(String.class)) {
			return propertyValue.toString();
		}
		
		throw new RuntimeException(String.format("Type %s is not supported", type.getName()));
	}
	
	private static Object parseArray(Class<?> arrayElementType, String values) {
		
		String[] elements = values.split(",");
		Object arrayObject = Array.newInstance(arrayElementType, elements.length);
		
		for (int i=0; i<elements.length; i++) {
			Array.set(arrayObject, i, parseValue(arrayElementType, elements[i]));
		}
		
		return arrayObject;
	}

}

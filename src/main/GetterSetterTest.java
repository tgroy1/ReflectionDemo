package main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import main.dto.Address;

public class GetterSetterTest {

	public static void main(String[] args) {
		testGetters(Address.class);
		testSetters(Address.class);
	}

	private static void testGetters(Class<?> clazz) {

		Field[] fields = clazz.getDeclaredFields();

		Map<String, Method> methodMap = mapMethodNameToMethod(clazz);

		for (Field field : fields) {

			String getterMethodName = "get" + capitalizeFirstLetter(field.getName());

			if (!methodMap.containsKey(getterMethodName)) {
				throw new RuntimeException(
						String.format("Field \'%s\' does not have a getter method", field.getName()));
			}

			Method getterMethod = methodMap.get(getterMethodName);
			if (!getterMethod.getReturnType().equals(field.getType())) {
				throw new RuntimeException(
						String.format("Field \'%s\' has type: \'%s\' and getter method return type is: \'%s\' ",
								field.getName(), field.getType(), getterMethod.getReturnType()));
			}

			if (getterMethod.getParameterCount() > 0) {
				throw new RuntimeException(
						String.format("Getter method for field \'%s\' has parameters", field.getName()));
			}

		}

		System.out.println(String.format("All getter methods in %s class are implemented correctly", clazz.getName()));
	}

	private static void testSetters(Class<?> clazz) {

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			String setterMethodName = "set" + capitalizeFirstLetter(field.getName());

			Method setterMethod = null;
			try {
				setterMethod = clazz.getMethod(setterMethodName, field.getType());
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(
						String.format("Field \'%s\' does not have a proper setter method", field.getName()));
			}

			if (!setterMethod.getReturnType().equals(void.class)) {
				throw new RuntimeException(String
						.format("Setter method for field \'%s\' does not have a void return type", field.getName()));
			}
		}
		
		System.out.println(String.format("All setter methods in %s class are implemented correctly", clazz.getName()));

	}

	private static String capitalizeFirstLetter(String name) {
		return name.substring(0, 1).toUpperCase().concat(name.substring(1));
	}

	private static Map<String, Method> mapMethodNameToMethod(Class<?> clazz) {

		Map<String, Method> methodMap = new HashMap<>();

		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			methodMap.put(method.getName(), method);
		}

		return methodMap;
	}

}

package main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import main.dto.Employee;

public class ConstructorMain {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		printConstructorDetails(Employee.class);
		
		Employee emp1 = createInstance(Employee.class, 3, "Henry", true);
		System.out.println(emp1);
		
		Employee emp2 = createInstanceWithPrivateConstructor(Employee.class, 1);
		System.out.println(emp2);
	}

	public static void printConstructorDetails(Class<?> clazz) {

		Constructor<?>[] constructors = clazz.getDeclaredConstructors();

		System.out.println(
				String.format("Class %s has %d declared constructors", clazz.getSimpleName(), constructors.length));

		for (Constructor con : constructors) {
			Class<?>[] parameters = con.getParameterTypes();

			List<String> paramTypes = Arrays.stream(parameters).map(type -> type.getSimpleName())
					.collect(Collectors.toList());

			System.out.println(paramTypes);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<T> clazz, Object ...args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		for (Constructor<?> con : clazz.getDeclaredConstructors()) {
			if (con.getParameterTypes().length == args.length) {
				return (T) con.newInstance(args);
			}
		}
		
		System.out.println("An appropriate constructor was not found");
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createInstanceWithPrivateConstructor(Class<T> clazz, int id) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		Constructor<?> con = clazz.getDeclaredConstructor(int.class);
		
		con.setAccessible(true);
		return (T) con.newInstance(id);
	}
}

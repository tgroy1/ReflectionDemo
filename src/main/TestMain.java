package main;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import main.dto.Employee;
import main.enums.ColorEnum;

public class TestMain {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		Employee mike = new Employee(1, "Mike Wilson", false);
		Employee harry = new Employee(2, "Harry Styles", false);
		Employee jane = new Employee(3, "Jane Doe", true);
		
		//Class<?> empClass = mike.getClass();
		Class<Employee> empClass = Employee.class;
		//Class<?> empClass = Class.forName("main.Employee");
		
		Package pkg = empClass.getPackage();
		String className = empClass.getCanonicalName();
		Constructor<?>[] constructors  = empClass.getDeclaredConstructors();
		Field[] fields = empClass.getDeclaredFields();
		Method[] methods = empClass.getDeclaredMethods();

		System.out.println("Package name: " + pkg.getName());
		System.out.println();
		
		System.out.println("Class name: " + className);
		System.out.println();
		
		System.out.println("Constructors are ---");
		for(Constructor<?> con : constructors) {
			System.out.println(con);
		}
		System.out.println();
		
		System.out.println("Fields are ---");
		for (Field field: fields) {
			System.out.println("Field modifier is private: " + Modifier.isPrivate(field.getModifiers()));
			System.out.println("Field name : " + field.getName() + ", field type : " + field.getType().getName());
		}
		System.out.println();
		
		System.out.println("Methods are ---");
		for (Method method: methods) {
			System.out.println(method);
		}
		System.out.println();
		
//		System.out.println("Is array : " + empClass.isArray());
//		System.out.println("Is primitive : " + empClass.isPrimitive());
//		System.out.println("Is enum : " + empClass.isEnum());
//		System.out.println("Is interface : " + empClass.isInterface());
//		System.out.println("Is anonymous : " + empClass.isAnonymousClass());
//		System.out.println();
		
		//Checing synthetic fields of an enum
		Field[] enumFields = ColorEnum.class.getDeclaredFields();
		for (Field field : enumFields) {
			System.out.println("Field name : " + field.getName() + ", field type : " + field.getType().getName());
			System.out.println("Is field synthetic : " + field.isSynthetic());
		}
		
		//Get value of a particular instance field
		for (Field field: fields) {
			field.setAccessible(true); //required for private fields of another class (or any fields not accessible)
			System.out.println("Field name : " + field.getName() + ", field value : " + field.get(mike));
		}
		
		

	}

}

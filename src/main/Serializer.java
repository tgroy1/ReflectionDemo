package main;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import main.dto.Address;
import main.dto.Employee;

public class Serializer {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		
		Address address = new Address("UT", "USA");
		String[] domains = {"Java", "Angular"};
		
		Address familyAddress1 = new Address("NY", "USA");
		Address familyAddress2 = new Address("WB", "India");
		Address[] familyAddresses = {familyAddress1, familyAddress2};
		
		String[][] phoneArr = {{"Mobile", "9876543210"}, {"Office", "+1-911-123-4567"}};
		Employee mike = new Employee(1, "Mike Wilson", false, 100.123, address, domains, familyAddresses, phoneArr);
		
		String json = objectToJson(mike, 0);
		
		System.out.println(json);
	}
	
	private static String objectToJson(Object instance, int indentSize) throws IllegalArgumentException, IllegalAccessException {
		
		Field[] fields = instance.getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		
		sb.append(indent(indentSize));
		sb.append("{");
		sb.append("\n");
		
		for (int i=0; i<fields.length; i++) {			
			Field field = fields[i];
			field.setAccessible(true);
			
			if (field.isSynthetic()) {
				continue;
			}
			
			sb.append(indent(indentSize + 1));
			sb.append(formatStringValue(field.getName()));
			sb.append(": ");
			
			if (field.getType().isPrimitive()) {
				sb.append(formatPrimitiveValue(field.get(instance), field.getType()));
			} else if (field.getType().equals(String.class)) {
				sb.append(formatStringValue(field.get(instance).toString()));
			} else if (field.getType().isArray()) {
				sb.append(arrayToJson(field.get(instance), indentSize + 1));
			} else {
				sb.append(objectToJson(field.get(instance), indentSize + 1));
			}
			
			if (i != fields.length - 1) {
				sb.append(",");
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append(indent(indentSize));
		sb.append("}");
		return sb.toString();
		
	}
	
	private static String arrayToJson(Object arrayInstance, int indentSize) throws IllegalArgumentException, IllegalAccessException {
		
		StringBuilder sb = new StringBuilder();
		
		int arrayLength = Array.getLength(arrayInstance);
		Class<?> componentType = arrayInstance.getClass().getComponentType();
		
		sb.append(indent(indentSize));
		sb.append("[");
		sb.append("\n");
		
		for (int i=0; i<arrayLength; i++) {
			Object element = Array.get(arrayInstance, i);
			
			if (componentType.isPrimitive()) {
				sb.append(indent(indentSize + 1));
				sb.append(formatPrimitiveValue(element, componentType));
			} else if (componentType.equals(String.class)) {
				sb.append(indent(indentSize + 1));
				sb.append(formatStringValue(element.toString()));
			} else if (componentType.isArray()) {
				sb.append(arrayToJson(element, indentSize + 1));
			} else {
				sb.append(objectToJson(element, indentSize + 1));
			}
			
			if (i != arrayLength - 1) {
				sb.append(", ");
				sb.append("\n");
			}
		}
		
		sb.append("\n");
		sb.append(indent(indentSize));
		sb.append("]");
		
		return sb.toString();
	}

	private static String indent(int indentSize) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i=0; i<indentSize; i++) {
			stringBuilder.append("\t");
		}
		
		return stringBuilder.toString();
	}
	
	private static String formatPrimitiveValue(Object instance, Class<?> type) throws IllegalArgumentException, IllegalAccessException {
		
		if (type.equals(boolean.class) 
			|| type.equals(int.class)
			|| type.equals(short.class)
			|| type.equals(long.class)) {
			return instance.toString();
		} else if (type.equals(float.class) || type.equals(double.class) ) {
			return String.format("%.02f", instance);
		}
		
		throw new RuntimeException(String.format("Type : %s is not supported", type.getName()));
	}
	
	private static String formatStringValue(String value) {
		return String.format("\"%s\"", value);
	}
	
	
}

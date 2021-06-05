package main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import main.annotations.InitializeClass;
import main.annotations.InitializeMethod;
import main.annotations.RetryOperation;
import main.annotations.ScanPackages;

@ScanPackages({"main.mockhelpers", "main.mockhelpers.database", "main.mockhelpers.cache"})
public class AnnotationMain {

	public static void main(String[] args) throws Throwable {
		initialize();
	}
	
	private static void initialize() throws Throwable {
		
		ScanPackages packages = AnnotationMain.class.getAnnotation(ScanPackages.class);
		
		if (packages == null || packages.value().length == 0) {
			return;
		}
		
		List<Class<?>> classes = getAllClasses(packages.value());

		for (Class<?> clazz : classes) {
			if (clazz.isAnnotationPresent(InitializeClass.class)) {
				List<Method> initializerMethods = getAllInitializerMethods(clazz);
				
				if (!initializerMethods.isEmpty()) {
					Object instance = clazz.getDeclaredConstructor().newInstance();
					for (Method method : initializerMethods) {
						callInitializingMethods(instance, method);
					}
				}
			}
		}
	}
	
	private static void callInitializingMethods(Object instance, Method method) throws Throwable {

		RetryOperation retryOperation = method.getAnnotation(RetryOperation.class);

		int retryCount = retryOperation == null ? 0 : retryOperation.numberOfRetries();

		while (true) {

			try {
				method.invoke(instance);
				break;
			} catch (InvocationTargetException e) {

				Throwable targetException = e.getTargetException();

				if (retryCount > 0 && new HashSet<>(Arrays.asList(retryOperation.retryExceptions()))
						.contains(targetException.getClass())) {
					retryCount--;

					System.out.println("Retrying...");
					Thread.sleep(retryOperation.durationBetweenRetries());
				} else if (retryOperation != null) {
					throw new Exception(retryOperation.failureMessage(), targetException);
				} else {
					throw targetException;
				}
			}
		}
	}
	
	private static List<Method> getAllInitializerMethods(Class<?> clazz) {
		
		List<Method> initializerMethods = new ArrayList<>();
		
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(InitializeMethod.class)) {
				initializerMethods.add(method);
			}
		}
		return initializerMethods;
	}
	
	private static List<Class<?>> getAllClasses(String... packageNames) throws URISyntaxException, IOException, ClassNotFoundException {
        List<Class<?>> allClasses = new ArrayList<>();

        for (String packageName : packageNames) {
            String packageRelativePath = packageName.replace('.', '/');
            
            URI packageUri = AnnotationMain.class.getClassLoader().getResource(packageRelativePath).toURI();

            if (packageUri.getScheme().equals("file")) {
                Path packageFullPath = Paths.get(packageUri);
                allClasses.addAll(getAllPackageClasses(packageFullPath, packageName));
            } else if (packageUri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());

                Path packageFullPathInJar = fileSystem.getPath(packageRelativePath);
                allClasses.addAll(getAllPackageClasses(packageFullPathInJar, packageName));

                fileSystem.close();
            }
        }
        return allClasses;
    }
	
	private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName)
			throws IOException, ClassNotFoundException {

		if (!Files.exists(packagePath)) {
			return Collections.emptyList();
		}

		List<Path> files = Files.list(packagePath).filter(Files::isRegularFile).collect(Collectors.toList());

		List<Class<?>> classes = new ArrayList<>();

		for (Path filePath : files) {
			String fileName = filePath.getFileName().toString();

			if (fileName.endsWith(".class")) {
				String classFullName = packageName.concat(".").concat(fileName.replaceFirst("\\.class$", ""));
				Class<?> clazz = Class.forName(classFullName);
				classes.add(clazz);
			}
		}
		return classes;
	}
}

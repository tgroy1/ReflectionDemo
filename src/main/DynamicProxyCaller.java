package main;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import main.external.impl.DatabaseReaderImpl;
import main.external.impl.HttpClientImpl;
import main.external.interfaces.DatabaseReader;
import main.external.interfaces.HttpClient;

public class DynamicProxyCaller {

	public static void main(String[] args) throws InterruptedException {
		DatabaseReader databaseReader = createProxy(new DatabaseReaderImpl());
        HttpClient httpClient = createProxy(new HttpClientImpl());

        useDatabaseReader(databaseReader);
        System.out.println("--------------");
        
        useHttpClient(httpClient);
        System.out.println("--------------");
        
        List<String> listOfGreetings = createProxy(new ArrayList<>()); //can be used for internal library methods
        listOfGreetings.add("hello");
        listOfGreetings.add("good morning");
        listOfGreetings.remove("good night");
	}
	
	public static void useHttpClient(HttpClient httpClient) {
        httpClient.initialize();
        String response = httpClient.sendRequest("some request");

        System.out.println(String.format("Http response is : %s", response));
    }
	
	public static void useDatabaseReader(DatabaseReader databaseReader) throws InterruptedException {
        int rowsInGamesTable = 0;
        try {
            rowsInGamesTable = databaseReader.countRowsInTable("GamesTable");
        } catch (IOException e) {
            System.out.println("Catching exception " + e);
            return;
        }

        System.out.println(String.format("There are %s rows in GamesTable", rowsInGamesTable));

        String[] data = databaseReader.readRow("SELECT * from GamesTable");

        System.out.println(String.format("Received %s", String.join(" , ", data)));
    }
	
	@SuppressWarnings("unchecked")
    public static <T> T createProxy(Object originalObject) {
        Class<?>[] interfaces = originalObject.getClass().getInterfaces();

        TimeMeasuringProxyHandler timeMeasuringProxyHandler = new TimeMeasuringProxyHandler(originalObject);

        return (T) Proxy.newProxyInstance(originalObject.getClass().getClassLoader(), interfaces, timeMeasuringProxyHandler);
    }

    public static class TimeMeasuringProxyHandler implements InvocationHandler {
        private final Object originalObject;

        public TimeMeasuringProxyHandler(Object originalObject) {
            this.originalObject = originalObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result;

            System.out.println(String.format("Measuring Proxy - Before Executing method : %s()", method.getName()));

            long startTime = System.nanoTime();
            try {
                result = method.invoke(originalObject, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
            long endTime = System.nanoTime();

            System.out.println(String.format("Measuring Proxy - Execution of %s() took %dns", method.getName(), endTime - startTime));
            System.out.println("");
            
            return result;
        }
    }

}

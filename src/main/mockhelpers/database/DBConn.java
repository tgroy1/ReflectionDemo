package main.mockhelpers.database;

import java.io.IOException;

import main.annotations.InitializeClass;
import main.annotations.InitializeMethod;
import main.annotations.RetryOperation;

@InitializeClass
public class DBConn {
	
	private int failCounter = 3; //simulates failure to get database connectivity for some time
	
	@InitializeMethod
	@RetryOperation(
            numberOfRetries = 10,
            retryExceptions = IOException.class,
            durationBetweenRetries = 1000,
            failureMessage = "Connection to database failed after retries"
    )
	public void connectToDatabase() throws IOException {
		System.out.println("Connecting to database");
		
		if (failCounter > 0) {
			failCounter--;
			throw new IOException("Connection failed");
		}
		
		System.out.println("Connected to database");
	}
}

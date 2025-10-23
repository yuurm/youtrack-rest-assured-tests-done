package com.youtrack.api.tests;

import com.youtrack.api.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected RequestSpecification requestSpec;
    protected String baseUrl;
    protected String authToken;
    protected String testProjectId;

    @BeforeClass(alwaysRun = true)
    public void setupClass() {
        baseUrl = ConfigReader.getBaseUrl();
        authToken = ConfigReader.getAuthToken();
        testProjectId = ConfigReader.getTestProjectId();

        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = ConfigReader.getApiBasePath();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addHeader("Authorization", "Bearer " + authToken)
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .build();
    }

    protected void logTestInfo(String testName, String description) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Test: " + testName);
        System.out.println("Description: " + description);
        System.out.println("=".repeat(80));
    }

    protected void logTestResult(boolean passed, String message) {
        String status = passed ? "✓ PASSED" : "✗ FAILED";
        System.out.println("\nTest Result: " + status);
        System.out.println("Message: " + message);
        System.out.println("=".repeat(80) + "\n");
    }
}

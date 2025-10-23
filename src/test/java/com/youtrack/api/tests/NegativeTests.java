package com.youtrack.api.tests;

import com.youtrack.api.pojo.CustomField;
import com.youtrack.api.pojo.Issue;
import com.youtrack.api.pojo.Project;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

/**
 * Негативные тест-кейсы для YouTrack REST API
 * Проверяют обработку ошибочных ситуаций и валидацию данных
 */
public class NegativeTests extends BaseTest {

    @Test(priority = 1, description = "TC_NEG_001: Создание issue без обязательного поля project")
    public void testCreateIssueWithoutProject() {
        logTestInfo("TC_NEG_001", "Attempt to create issue without required project field");

        try {
            Issue issue = new Issue();
            issue.setSummary("Test Issue without project");
            issue.setDescription("This should fail validation");

            Response response = given()
                .spec(requestSpec)
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(400)
                .extract()
                .response();

            String errorMessage = response.getBody().asString();
            assertTrue(errorMessage != null && !errorMessage.isEmpty());

            System.out.println("\nExpected validation error received: 400 Bad Request");

            logTestResult(true, "Validation error returned as expected");

        } catch (AssertionError e) {
            logTestResult(false, "Test failed: Expected 400 status");
            throw e;
        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2, description = "TC_NEG_002: Получение несуществующего issue")
    public void testGetNonExistentIssue() {
        logTestInfo("TC_NEG_002", "Attempt to get non-existent issue");

        try {
            String nonExistentId = "NONEXISTENT-999999";

            given()
                .spec(requestSpec)
            .when()
                .get("/issues/" + nonExistentId)
            .then()
                .statusCode(404);

            System.out.println("\nAttempted to access issue: " + nonExistentId);
            System.out.println("  Status: 404 Not Found (as expected)");

            logTestResult(true, "404 Not Found returned as expected");

        } catch (AssertionError e) {
            logTestResult(false, "Test failed: Expected 404 status");
            throw e;
        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, description = "TC_NEG_003: Создание issue с невалидным project ID")
    public void testCreateIssueWithInvalidProjectId() {
        logTestInfo("TC_NEG_003", "Attempt to create issue with invalid project ID");

        try {
            Project invalidProject = new Project("invalid-project-id-99999");
            Issue issue = new Issue(
                "Test Issue with invalid project",
                "This should fail",
                invalidProject
            );

            Response response = given()
                .spec(requestSpec)
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(400)
                .extract()
                .response();

            System.out.println("\nValidation error for invalid project ID: 400");

            logTestResult(true, "Validation error for invalid project ID returned");

        } catch (AssertionError e) {
            logTestResult(false, "Test failed: Expected 400 status");
            throw e;
        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4, description = "TC_NEG_004: Запрос без токена авторизации")
    public void testRequestWithoutAuthorization() {
        logTestInfo("TC_NEG_004", "Attempt to access API without authorization token");

        try {
            RequestSpecification noAuthSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri(baseUrl)
                .setBasePath("/api")
                .build();

            given()
                .spec(noAuthSpec)
            .when()
                .get("/users/me")
            .then()
                .statusCode(401);

            System.out.println("\nAttempted unauthorized access: 401 Unauthorized");

            logTestResult(true, "401 Unauthorized returned as expected");

        } catch (AssertionError e) {
            logTestResult(false, "Test failed: Expected 401 status");
            throw e;
        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5, description = "TC_NEG_005: Обновление issue с пустым summary")
    public void testUpdateIssueWithEmptySummary() {
        logTestInfo("TC_NEG_005", "Attempt to update issue with empty summary");

        try {
            String issueId = createTestIssue("Valid Summary for Update Test");

            Issue updateData = new Issue();
            updateData.setSummary("");

            given()
                .spec(requestSpec)
                .body(updateData)
            .when()
                .post("/issues/" + issueId)
            .then()
                .statusCode(400);

            System.out.println("\nValidation error for empty summary: 400");

            logTestResult(true, "Validation error for empty summary returned");

        } catch (AssertionError e) {
            logTestResult(false, "Test failed: Expected 400 status");
            throw e;
        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 6, description = "TC_NEG_006: Создание issue с невалидным типом custom field")
    public void testCreateIssueWithInvalidCustomFieldType() {
        logTestInfo("TC_NEG_006", "Attempt to create issue with invalid custom field type");

        try {
            Project project = new Project(testProjectId);
            Issue issue = new Issue(
                "Test with invalid custom field type",
                "This should fail",
                project
            );

            Map<String, String> value = new HashMap<>();
            value.put("name", "Major");

            CustomField invalidField = new CustomField(
                "Priority",
                "InvalidFieldType",
                value
            );

            issue.setCustomFields(Arrays.asList(invalidField));

            given()
                .spec(requestSpec)
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(400);

            System.out.println("\nValidation error for invalid field type: 400");

            logTestResult(true, "Validation error for invalid field type returned");

        } catch (AssertionError e) {
            logTestResult(false, "Test failed: Expected 400 status");
            throw e;
        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    private String createTestIssue(String summary) {
        Project project = new Project(testProjectId);
        Issue issue = new Issue(summary, "Test description", project);

        Issue created = given()
            .spec(requestSpec)
            .body(issue)
        .when()
            .post("/issues")
        .then()
            .statusCode(200)
            .extract()
            .as(Issue.class);

        return created.getId();
    }
}

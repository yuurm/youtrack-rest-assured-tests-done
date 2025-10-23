package com.youtrack.api.tests;

import com.youtrack.api.pojo.CustomField;
import com.youtrack.api.pojo.Issue;
import com.youtrack.api.pojo.Project;
import io.restassured.response.Response;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

/**
 * Позитивные тест-кейсы для YouTrack REST API
 * Проверяют основные сценарии успешной работы API
 */
public class PositiveTests extends BaseTest {

    private String createdIssueId;

    @Test(priority = 1, description = "TC_POS_001: Создание issue с минимальным набором полей")
    public void testCreateIssueWithRequiredFields() {
        logTestInfo("TC_POS_001", "Create new issue with required fields only");

        try {
            Project project = new Project(testProjectId);
            Issue issue = new Issue(
                "Test Issue via RestAssured - " + System.currentTimeMillis(),
                "This is a test issue created by automated test",
                project
            );

            Issue createdIssue = given()
                .spec(requestSpec)
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(200)
                .extract()
                .as(Issue.class);

            assertNotNull(createdIssue.getId(), "Issue ID should not be null");
            assertNotNull(createdIssue.getSummary(), "Issue summary should not be null");
            assertEquals(createdIssue.getSummary(), issue.getSummary());

            createdIssueId = createdIssue.getId();

            logTestResult(true, "Issue created successfully with ID: " + createdIssueId);

        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2, description = "TC_POS_002: Получение списка всех проектов")
    public void testGetAllProjects() {
        logTestInfo("TC_POS_002", "Get list of all available projects");

        try {
            Project[] projects = given()
                .spec(requestSpec)
                .queryParam("fields", "id,name,shortName,$type")
            .when()
                .get("/admin/projects")
            .then()
                .statusCode(200)
                .extract()
                .as(Project[].class);

            assertTrue(projects.length > 0, "Projects list should not be empty");

            System.out.println("\nFound " + projects.length + " projects:");
            for (Project project : projects) {
                assertNotNull(project.getId(), "Project ID should not be null");
                System.out.println("  - " + project.getId() + ": " + project.getName());
            }

            logTestResult(true, "Successfully retrieved " + projects.length + " projects");

        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, description = "TC_POS_003: Получение информации о конкретном issue",
          dependsOnMethods = {"testCreateIssueWithRequiredFields"})
    public void testGetSpecificIssue() {
        logTestInfo("TC_POS_003", "Get detailed information about specific issue");

        try {
            Issue retrievedIssue = given()
                .spec(requestSpec)
                .queryParam("fields", "id,summary,description,project(id,name)")
            .when()
                .get("/issues/" + createdIssueId)
            .then()
                .statusCode(200)
                .extract()
                .as(Issue.class);

            assertNotNull(retrievedIssue.getId());
            assertEquals(retrievedIssue.getId(), createdIssueId);

            System.out.println("\nRetrieved Issue: " + retrievedIssue.getId());

            logTestResult(true, "Issue details retrieved successfully");

        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 4, description = "TC_POS_004: Обновление summary существующего issue",
          dependsOnMethods = {"testCreateIssueWithRequiredFields"})
    public void testUpdateIssueSummary() {
        logTestInfo("TC_POS_004", "Update issue summary field");

        try {
            String updatedSummary = "Updated Summary - " + System.currentTimeMillis();

            Issue updateData = new Issue();
            updateData.setSummary(updatedSummary);

            Response response = given()
                .spec(requestSpec)
                .queryParam("fields", "id,summary")
                .body(updateData)
            .when()
                .post("/issues/" + createdIssueId)
            .then()
                .statusCode(200)
                .extract()
                .response();

            String actualSummary = response.jsonPath().getString("summary");
            assertEquals(actualSummary, updatedSummary);

            logTestResult(true, "Summary updated successfully");

        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 5, description = "TC_POS_005: Получение информации о текущем пользователе")
    public void testGetCurrentUser() {
        logTestInfo("TC_POS_005", "Get current authenticated user profile");

        try {
            Response response = given()
                .spec(requestSpec)
                .queryParam("fields", "id,login,fullName,email")
            .when()
                .get("/users/me")
            .then()
                .statusCode(200)
                .extract()
                .response();

            String userId = response.jsonPath().getString("id");
            String login = response.jsonPath().getString("login");

            assertNotNull(userId, "User ID should not be null");
            assertNotNull(login, "User login should not be null");

            System.out.println("\nCurrent User: " + login);

            logTestResult(true, "User profile retrieved successfully");

        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 6, description = "TC_POS_006: Создание issue с custom fields")
    public void testCreateIssueWithCustomFields() {
        logTestInfo("TC_POS_006", "Create issue with custom fields");

        try {
            Project project = new Project(testProjectId);
            Issue issue = new Issue(
                "Issue with Custom Fields - " + System.currentTimeMillis(),
                "Testing custom fields functionality",
                project
            );

            Map<String, String> priorityValue = new HashMap<>();
            priorityValue.put("name", "Major");

            CustomField priorityField = new CustomField(
                "Priority",
                "SingleEnumIssueCustomField",
                priorityValue
            );

            issue.setCustomFields(Arrays.asList(priorityField));

            Response response = given()
                .spec(requestSpec)
                .queryParam("fields", "id,summary")
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(200)
                .extract()
                .response();

            String issueId = response.jsonPath().getString("id");
            assertNotNull(issueId, "Issue ID should not be null");

            System.out.println("\nCreated issue with custom fields: " + issueId);

            logTestResult(true, "Issue with custom fields created successfully");

        } catch (Exception e) {
            logTestResult(false, "Test failed: " + e.getMessage());
            throw e;
        }
    }
}

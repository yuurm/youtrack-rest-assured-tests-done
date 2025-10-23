package com.youtrack.api.tests;

import com.youtrack.api.pojo.CustomField;
import com.youtrack.api.pojo.Issue;
import com.youtrack.api.pojo.Project;
import com.youtrack.api.utils.CSVDataProvider;
import com.youtrack.api.utils.ExcelDataProvider;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

/**
 * Data-Driven тесты с использованием CSV и Excel файлов
 */
public class DataDrivenTests extends BaseTest {

    @Test(dataProvider = "csvIssueData", 
          priority = 1,
          description = "Create issues from CSV data")
    public void testCreateIssuesFromCSV(String testId, String projectId, 
                                       String summary, String description,
                                       String expectedStatus) {
        logTestInfo("Data-Driven Test (CSV)", 
                   "Create issue from CSV data - Test ID: " + testId);

        try {
            Project project = new Project(projectId);
            Issue issue = new Issue(summary, description, project);

            int expectedCode = Integer.parseInt(expectedStatus);

            Response response = given()
                .spec(requestSpec)
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(expectedCode)
                .extract()
                .response();

            if (expectedCode == 200) {
                String issueId = response.jsonPath().getString("id");
                assertNotNull(issueId, "Issue ID should not be null");

                System.out.println("\nCreated issue from CSV: " + issueId);
            }

            logTestResult(true, "Issue created from CSV data: " + testId);

        } catch (Exception e) {
            logTestResult(false, "Test failed for CSV data: " + testId);
            throw e;
        }
    }

    @DataProvider(name = "csvIssueData", parallel = true)
    public Object[][] getCsvData() {
        return CSVDataProvider.readCSV("src/test/resources/test_data.csv");
    }

    @Test(dataProvider = "excelIssueData",
          priority = 2,
          description = "Create issues from Excel data")
    public void testCreateIssuesFromExcel(String testId, String projectId,
                                         String summary, String description,
                                         String priority) {
        logTestInfo("Data-Driven Test (Excel)", 
                   "Create issue from Excel data - Test ID: " + testId);

        try {
            Project project = new Project(projectId);
            Issue issue = new Issue(summary, description, project);

            if (priority != null && !priority.isEmpty()) {
                Map<String, String> priorityValue = new HashMap<>();
                priorityValue.put("name", priority);

                CustomField priorityField = new CustomField(
                    "Priority",
                    "SingleEnumIssueCustomField",
                    priorityValue
                );
                issue.setCustomFields(Arrays.asList(priorityField));
            }

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

            System.out.println("\nCreated issue from Excel: " + issueId);

            logTestResult(true, "Issue created from Excel data: " + testId);

        } catch (Exception e) {
            logTestResult(false, "Test failed for Excel data: " + testId);
            throw e;
        }
    }

    @DataProvider(name = "excelIssueData", parallel = true)
    public Object[][] getExcelData() {
        return ExcelDataProvider.readExcel("src/test/resources/test_data.xlsx", 
                                          "IssueData");
    }

    @Test(dataProvider = "priorityData",
          priority = 3,
          description = "Test all priority values")
    public void testCreateIssuesWithDifferentPriorities(String priorityName) {
        logTestInfo("Data-Driven Test (Priority)", 
                   "Create issue with priority: " + priorityName);

        try {
            Project project = new Project(testProjectId);
            Issue issue = new Issue(
                "Issue with priority " + priorityName,
                "Testing different priority values",
                project
            );

            Map<String, String> priorityValue = new HashMap<>();
            priorityValue.put("name", priorityName);

            CustomField priorityField = new CustomField(
                "Priority",
                "SingleEnumIssueCustomField",
                priorityValue
            );

            issue.setCustomFields(Arrays.asList(priorityField));

            Response response = given()
                .spec(requestSpec)
                .body(issue)
            .when()
                .post("/issues")
            .then()
                .statusCode(200)
                .extract()
                .response();

            String issueId = response.jsonPath().getString("id");
            System.out.println("\nCreated issue with priority '" + priorityName + 
                             "': " + issueId);

            logTestResult(true, "Issue with priority " + priorityName + " created");

        } catch (Exception e) {
            logTestResult(false, "Test failed for priority: " + priorityName);
            throw e;
        }
    }

    @DataProvider(name = "priorityData", parallel = true)
    public Object[][] getPriorityData() {
        return new Object[][] {
            {"Critical"},
            {"Major"},
            {"Normal"},
            {"Minor"}
        };
    }
}

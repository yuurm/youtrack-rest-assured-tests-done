package com.youtrack.api.utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestReportListener implements ITestListener {

    private PrintWriter writer;
    private int passedTests = 0;
    private int failedTests = 0;
    private int skippedTests = 0;

    @Override
    public void onStart(ITestContext context) {
        try {
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "test-report-" + timestamp + ".txt";
            writer = new PrintWriter(new FileWriter(fileName));

            writer.println("=".repeat(80));
            writer.println("YouTrack REST API Test Execution Report");
            writer.println("Test Suite: " + context.getName());
            writer.println("Start Time: " + LocalDateTime.now());
            writer.println("=".repeat(80));
            writer.println();
        } catch (IOException e) {
            System.err.println("Failed to create report file: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passedTests++;
        writeTestResult(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests++;
        writeTestResult(result, "FAILED");
        if (result.getThrowable() != null) {
            writer.println("Error Details:");
            result.getThrowable().printStackTrace(writer);
            writer.println();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        skippedTests++;
        writeTestResult(result, "SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        writer.println("\n" + "=".repeat(80));
        writer.println("Test Execution Summary");
        writer.println("=".repeat(80));
        writer.println("Total Tests: " + (passedTests + failedTests + skippedTests));
        writer.println("Passed: " + passedTests);
        writer.println("Failed: " + failedTests);
        writer.println("Skipped: " + skippedTests);
        writer.println("Success Rate: " + 
            String.format("%.2f%%", (passedTests * 100.0) / 
            (passedTests + failedTests + skippedTests)));
        writer.println("End Time: " + LocalDateTime.now());
        writer.println("=".repeat(80));

        writer.close();

        System.out.println("\nTest report generated successfully!");
    }

    private void writeTestResult(ITestResult result, String status) {
        writer.println("-".repeat(80));
        writer.println("Test: " + result.getMethod().getMethodName());
        writer.println("Class: " + result.getTestClass().getRealClass().getSimpleName());
        writer.println("Status: " + status);
        writer.println("Duration: " + 
            (result.getEndMillis() - result.getStartMillis()) + "ms");

        if (result.getMethod().getDescription() != null) {
            writer.println("Description: " + result.getMethod().getDescription());
        }

        writer.println();
    }
}

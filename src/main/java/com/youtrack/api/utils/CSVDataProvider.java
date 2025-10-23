package com.youtrack.api.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVDataProvider {

    public static Object[][] readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            records = csvReader.readAll();
            // Skip header row
            records.remove(0);
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }

        Object[][] data = new Object[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            data[i] = records.get(i);
        }

        return data;
    }

    public static Object[][] getIssueTestData(String filePath) {
        return readCSV(filePath);
    }
}

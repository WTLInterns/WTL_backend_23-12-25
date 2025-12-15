package com.workshop.Config;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.workshop.Entity.onewayTrip;
import com.workshop.Repo.OnewayTripRepo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class SaveAllDataJob implements Job {

    @Autowired
    private OnewayTripRepo repository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Retrieve the file path from the job data
        String filePath = context.getJobDetail().getJobDataMap().getString("filePath");
        String startDateStr = context.getJobDetail().getJobDataMap().getString("startDate");
        String endDateStr = context.getJobDetail().getJobDataMap().getString("endDate");

        LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : LocalDate.now();
        LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr) : startDate;

        // Parse the Excel file and save data to the database
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            List<onewayTrip> trips = parseExcelFile(is, startDate, endDate); 
            repository.saveAll(trips);
            System.out.println("Saved all data to onewayTrip table on start date.");
        } catch (IOException e) {
            throw new JobExecutionException("Failed to read file: " + filePath, e);
        }
    }


    private List<onewayTrip> parseExcelFile(InputStream is, LocalDate startDate, LocalDate endDate) throws IOException {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        List<onewayTrip> trips = new ArrayList<>();

        int rowNumber = 0;
        while (rows.hasNext()) {
            Row currentRow = rows.next();

            // Skip header
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }

            Iterator<Cell> cellsInRow = currentRow.iterator();

            onewayTrip trip = new onewayTrip();

            int cellIdx = 0;
            while (cellsInRow.hasNext()) {
                Cell currentCell = cellsInRow.next();

                switch (cellIdx) {
                    case 0:
                        // ID column in Excel - ignored, DB id is auto-generated
                        break;
                    case 1:
                        trip.setSourceState(getCellValueAsString(currentCell));
                        break;
                    case 2:
                        trip.setSourceCity(getCellValueAsString(currentCell));
                        break;
                    case 3:
                        trip.setDestinationState(getCellValueAsString(currentCell));
                        break;
                    case 4:
                        trip.setDestinationCity(getCellValueAsString(currentCell));
                        break;
                    case 5:
                        trip.setHatchback((int) getCellValueAsNumeric(currentCell));
                        break;
                    case 6:
                        trip.setSedan((int) getCellValueAsNumeric(currentCell));
                        break;
                    case 7:
                        trip.setSedanpremium((int) getCellValueAsNumeric(currentCell));
                        break;
                    case 8:
                        trip.setSuv((int) getCellValueAsNumeric(currentCell));
                        break;
                    case 9:
                        trip.setSuvplus((int) getCellValueAsNumeric(currentCell));
                        break;
                    case 14:
                        trip.setErtiga((int) getCellValueAsNumeric(currentCell));
                        break;
                    default:
                        break;
                }

                cellIdx++;
            }

            trip.setStartDate(startDate);
            trip.setEndDate(endDate);
            trips.add(trip);
        }

        workbook.close();
        return trips;
    }

    // Helper method to get cell value as String
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()); // Convert numeric to string
            default:
                return "";
        }
    }

    // Helper method to get cell value as numeric
    private double getCellValueAsNumeric(Cell cell) {
        if (cell == null) {
            return 0;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue()); // Convert string to numeric
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

}
package com.workshop.Entity.ExcelGetData;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.workshop.Entity.onewayTrip;
import com.workshop.Entity.roundTrip;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class TransportRateExcelView extends AbstractXlsxView {

 @Override
protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
    // Check the type of data in the model and act accordingly
    Object data = model.get("rates");

    if (data != null && data instanceof List<?> list && !list.isEmpty()) {
        Object firstItem = list.get(0);

        if (firstItem instanceof onewayTrip) {
            response.setHeader("Content-Disposition", "attachment; filename=\"transport_rates.xlsx\"");
            Sheet sheet = workbook.createSheet("Transport Rates");

            int rowCount = 0;
            Row header = sheet.createRow(rowCount++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Source State");
            header.createCell(2).setCellValue("Source City");
            header.createCell(3).setCellValue("Destination State");
            header.createCell(4).setCellValue("Destination City");
            header.createCell(5).setCellValue("Hatchback");
            header.createCell(6).setCellValue("Sedan");
            header.createCell(7).setCellValue("Sedan Premium");
            header.createCell(8).setCellValue("SUV");
            header.createCell(9).setCellValue("SUV Plus");
            header.createCell(10).setCellValue("Status");
            header.createCell(11).setCellValue("Start Date");
            header.createCell(12).setCellValue("End Date");
            header.createCell(13).setCellValue("Distance");

            for (onewayTrip rate : (List<onewayTrip>) list) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(rate.getId());
                row.createCell(1).setCellValue(rate.getSourceState());
                row.createCell(2).setCellValue(rate.getSourceCity());
                row.createCell(3).setCellValue(rate.getDestinationState());
                row.createCell(4).setCellValue(rate.getDestinationCity());
                row.createCell(5).setCellValue(rate.getHatchback());
                row.createCell(6).setCellValue(rate.getSedan());
                row.createCell(7).setCellValue(rate.getSedanpremium());
                row.createCell(8).setCellValue(rate.getSuv());
                row.createCell(9).setCellValue(rate.getSuvplus());
                row.createCell(10).setCellValue(rate.getStatus());
                row.createCell(11).setCellValue(rate.getStartDate() != null ? rate.getStartDate().toString() : "");
                row.createCell(12).setCellValue(rate.getEndDate() != null ? rate.getEndDate().toString() : "");
                row.createCell(13).setCellValue(rate.getDistance());
            }

            for (int i = 0; i < 14; i++) sheet.autoSizeColumn(i);

        } else if (firstItem instanceof roundTrip) {
            response.setHeader("Content-Disposition", "attachment; filename=\"round_trip_rates.xlsx\"");
            Sheet sheet = workbook.createSheet("Round Trip Rates");

            int rowCount = 0;
            Row header = sheet.createRow(rowCount++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Source State");
            header.createCell(2).setCellValue("Source City");
            header.createCell(3).setCellValue("Destination State");
            header.createCell(4).setCellValue("Destination City");
            header.createCell(5).setCellValue("Hatchback");
            header.createCell(6).setCellValue("Sedan");
            header.createCell(7).setCellValue("Sedan Premium");
            header.createCell(8).setCellValue("SUV");
            header.createCell(9).setCellValue("SUV Plus");
            header.createCell(10).setCellValue("Status");

            for (roundTrip rate : (List<roundTrip>) list) {
                Row row = sheet.createRow(rowCount++);
                row.createCell(0).setCellValue(rate.getId());
                row.createCell(1).setCellValue(rate.getSourceState());
                row.createCell(2).setCellValue(rate.getSourceCity());
                row.createCell(3).setCellValue(rate.getDestinationState());
                row.createCell(4).setCellValue(rate.getDestinationCity());
                row.createCell(5).setCellValue(rate.getHatchback());
                row.createCell(6).setCellValue(rate.getSedan());
                row.createCell(7).setCellValue(rate.getSedanpremium());
                row.createCell(8).setCellValue(rate.getSuv());
                row.createCell(9).setCellValue(rate.getSuvplus());
                row.createCell(10).setCellValue(rate.getStatus());
            }

            for (int i = 0; i <= 10; i++) sheet.autoSizeColumn(i);
        }
    }
}
}
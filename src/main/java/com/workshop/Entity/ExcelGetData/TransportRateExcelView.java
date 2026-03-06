package com.workshop.Entity.ExcelGetData;
 
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.workshop.Entity.onewayTrip;

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
            }
        }
    }
}
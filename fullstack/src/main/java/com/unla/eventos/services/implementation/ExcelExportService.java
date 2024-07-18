package com.unla.eventos.services.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceResponse;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportAssistanceResponsesToExcel(List<AssistanceResponse> responses) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Assistance Responses");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Last Name", "Document Number", "Email", "QR Code", "Is Present", "Is Assistance Certify Sent"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for (AssistanceResponse response : responses) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(response.getName());
                row.createCell(1).setCellValue(response.getLastName());
                row.createCell(2).setCellValue(response.getDocumentNumber());
                row.createCell(3).setCellValue(response.getEmail());
                row.createCell(4).setCellValue(response.getQRCode());
                row.createCell(5).setCellValue(response.isPresent());
                row.createCell(6).setCellValue(response.isAssistanceCertifySent());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}

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

import jakarta.persistence.Column;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportAssistanceResponsesToExcel(List<AssistanceResponse> responses) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Assistance Responses");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Nombre/s", "Apellido/s", "documento", "mail", "Miembro UNLa", "Rol principal", "Otro Rol principal",
            					"Investigador Carreras", "Otro Investigador Carreras", "Tipo inscripción", "Source", "IsWelcomeMailSent",
            					"QRCode", "isPresent", "IsAssistanceCertifySent"};
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
                row.createCell(4).setCellValue(response.getMiembro());
                row.createCell(5).setCellValue(response.getRolPrincipal());
                row.createCell(6).setCellValue(response.getRolPrincipalOtro());
                row.createCell(7).setCellValue(response.getInvestigadorCarreras());
                row.createCell(8).setCellValue(response.getInvestigadorCarrerasOtro());
                row.createCell(9).setCellValue(response.getTipoInscripcion());
                row.createCell(10).setCellValue(response.getSource());
                row.createCell(11).setCellValue(response.isWelcomeMailSent());
                row.createCell(12).setCellValue(response.getQRCode());
                row.createCell(13).setCellValue(response.isPresent());
                row.createCell(14).setCellValue(response.isAssistanceCertifySent());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}

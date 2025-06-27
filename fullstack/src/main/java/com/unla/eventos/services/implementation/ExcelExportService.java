package com.unla.eventos.services.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.EventDays;
import com.unla.eventos.services.IAssistanceDaysService;
import com.unla.eventos.services.IEventDaysService;

@Service
public class ExcelExportService {

    @Autowired
    private IAssistanceDaysService assistanceDaysService;

    @Autowired
    private IEventDaysService eventDaysService;

    public ByteArrayInputStream exportAssistanceResponsesToExcel(List<AssistanceResponse> responses) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Assistance Responses");

            List<EventDays> eventDays = eventDaysService.findByEventId(responses.get(0).getEvent().getId());

            List<String> headers = new ArrayList<>(Arrays.asList(
                    "Nombre/s", "Apellido/s", "documento", "mail", "Miembro UNLa", "Rol principal",
                    "Otro Rol principal",
                    "Investigador Carreras", "Otro Investigador Carreras", "Tipo inscripción", "Source",
                    "IsWelcomeMailSent",
                    "QRCode", "isPresent", "IsAssistanceCertifySent"));

            headers.addAll(eventDays.stream()
                    .map(day -> "Asistencia " + day.getDate().toString())
                    .collect(Collectors.toList()));

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
}
            int rowIndex = 1;
            for (AssistanceResponse response : responses) {
                List<AssistanceDays> days =assistanceDaysService.findByAssistanceResponseId(response.getId());
                Map<Integer, Boolean> asistenciaPorDia = days.stream()
                        .collect(Collectors.toMap(d -> d.getEventDay().getId(), AssistanceDays::isPresent));
                Row row = sheet.createRow(rowIndex++);
                int colIndex = 0;
                row.createCell(colIndex++).setCellValue(response.getName());
                row.createCell(colIndex++).setCellValue(response.getLastName());
                row.createCell(colIndex++).setCellValue(response.getDocumentNumber());
                row.createCell(colIndex++).setCellValue(response.getEmail());
                row.createCell(colIndex++).setCellValue(response.getMiembro());
                row.createCell(colIndex++).setCellValue(response.getRolPrincipal());
                row.createCell(colIndex++).setCellValue(response.getRolPrincipalOtro());
                row.createCell(colIndex++).setCellValue(response.getInvestigadorCarreras());
                row.createCell(colIndex++).setCellValue(response.getInvestigadorCarrerasOtro());
                row.createCell(colIndex++).setCellValue(response.getTipoInscripcion());
                row.createCell(colIndex++).setCellValue(response.getSource());
                row.createCell(colIndex++).setCellValue(response.isWelcomeMailSent());
                row.createCell(colIndex++).setCellValue(response.getQRCode());
                row.createCell(colIndex++).setCellValue(response.isPresent());
                row.createCell(colIndex++).setCellValue(response.isAssistanceCertifySent());

                for (EventDays day : eventDays) {
                    boolean presente = asistenciaPorDia.getOrDefault(day.getId(), false);
                    row.createCell(colIndex++).setCellValue(presente ? "Si" : "No");
    }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}

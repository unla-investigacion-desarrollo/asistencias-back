package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.repositories.IAssistanceResponseRepository;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.IEventService;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Iterator;

@Service
public class AssistanceResponseService implements IAssistanceResponseService {

    @Autowired
    private IAssistanceResponseRepository assistanceResponseRepository;
    
    @Autowired
    private IEventService eventService;

    public void importFromExcel(InputStream is, int eventId) throws Exception {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        // Ignora la primera fila si tiene encabezados
        if (rows.hasNext()) rows.next();

        while (rows.hasNext()) {
            Row currentRow = rows.next();

            AssistanceResponse response = new AssistanceResponse();
            response.setName(currentRow.getCell(0).getStringCellValue());
            response.setLastName(currentRow.getCell(1).getStringCellValue());
            response.setDocumentNumber(currentRow.getCell(2).getStringCellValue());
            response.setEmail(currentRow.getCell(3).getStringCellValue());
            response.setMiembro(currentRow.getCell(4).getStringCellValue());
            response.setRolPrincipal(currentRow.getCell(5).getStringCellValue());
            response.setRolPrincipalOtro(currentRow.getCell(6).getStringCellValue());
            response.setInvestigadorCarreras(currentRow.getCell(7).getStringCellValue());
            response.setInvestigadorCarrerasOtro(currentRow.getCell(8).getStringCellValue());
            response.setTipoInscripcion(currentRow.getCell(9).getStringCellValue());
            response.setSource("Externo excel");

            // Marca que estas respuestas vienen del Google Form
            //response.setExternalForm(true);

            // Guarda la respuesta en la base de datos
            //assistanceResponseRepository.save(response);
        }
        workbook.close();
    }

    
    public Optional<Event> findByUniqueCode(String uniqueCode) {
        return eventService.findByUniqueCode(uniqueCode);
    }
    
	public AssistanceResponse findByQRCode(String QRCode) {
		return assistanceResponseRepository.findByQRCode(QRCode);
	}
	
	public Optional<AssistanceResponse> findByEmailAndEventId(String email, int eventId) {
		return assistanceResponseRepository.findByEmailAndEventId(email, eventId);
	}
    
    public AssistanceResponse save(AssistanceResponse assistanceResponse) {
        return assistanceResponseRepository.save(assistanceResponse);
    }
    
    public List<AssistanceResponse> findByEventId(int eventId) {
        return assistanceResponseRepository.findByEventId(eventId);
    }
}

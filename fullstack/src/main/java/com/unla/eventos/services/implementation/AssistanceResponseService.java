package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        Optional<Event> eventOp = this.findEventById(eventId);
        if(eventOp.isPresent()) {
        	Event event = eventOp.get();
        	Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Ignora la primera fila si tiene encabezados
            if (rows.hasNext()) rows.next();

            Row currentRow = rows.next();
            boolean endFile = currentRow.getCell(0) == null || currentRow.getCell(0).getCellType() == CellType.BLANK;
            while (rows.hasNext() && !endFile) {
                AssistanceResponse response = new AssistanceResponse();
                response.setName(getCellValue(currentRow.getCell(1)));
                response.setLastName(getCellValue(currentRow.getCell(2)));
                response.setDocumentNumber(getCellValue(currentRow.getCell(3)));
                response.setEmail(getCellValue(currentRow.getCell(4)));
                response.setMiembro(getCellValue(currentRow.getCell(5)));
                if(currentRow.getCell(6) != null) {
                    response.setRolPrincipal(getCellValue(currentRow.getCell(6)));
                }
                if(currentRow.getCell(7) != null) {
                    response.setInvestigadorCarreras(getCellValue(currentRow.getCell(7)));
                }
                response.setTipoInscripcion(getCellValue(currentRow.getCell(8)));
                
                response.setPresent(false);
                response.setAssistanceCertifySent(false);
                response.setSource("Externo excel");
                response.setQRCode(UUID.randomUUID().toString());
                response.setEvent(event);

                System.out.println(response);
                currentRow = rows.next();
                endFile = currentRow.getCell(0) == null || currentRow.getCell(0).getCellType() == CellType.BLANK;
                // Marca que estas respuestas vienen del Google Form
                //response.setExternalForm(true);

                // Guarda la respuesta en la base de datos
                //assistanceResponseRepository.save(response);
            }
            workbook.close();
        }
    }
    
    private String getCellValue(Cell cell) {
        if (cell != null) {
            DataFormatter dataFormatter = new DataFormatter();
            return dataFormatter.formatCellValue(cell);
        }
        return "";
    }

    
    public Optional<Event> findEventByUniqueCode(String uniqueCode) {
        return eventService.findByUniqueCode(uniqueCode);
    }
    
    public Optional<Event> findEventById(int eventId) {
        return eventService.findById(eventId);
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

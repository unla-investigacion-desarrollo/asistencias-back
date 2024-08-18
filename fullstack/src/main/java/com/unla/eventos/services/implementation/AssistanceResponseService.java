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

import java.util.ArrayList;
import java.util.Iterator;

@Service
public class AssistanceResponseService implements IAssistanceResponseService {

    @Autowired
    private IAssistanceResponseRepository assistanceResponseRepository;
    
    @Autowired
    private IEventService eventService;

    public int importFromExcel(InputStream is, int eventId) throws Exception {
    	List<AssistanceResponse> externalAssistanceResponses = new ArrayList<AssistanceResponse>();
        Optional<Event> eventOp = this.findEventById(eventId);
        if(eventOp.isPresent()) {
        	Event event = eventOp.get();
        	Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Ignora la primera fila si tiene encabezados
            if (rows.hasNext()) rows.next();

            String email = "";
            Row currentRow = rows.next();
            boolean endFile = currentRow.getCell(0) == null || currentRow.getCell(0).getCellType() == CellType.BLANK;
            try {
                while (rows.hasNext() && !endFile) {
                	email = getCellValue(currentRow.getCell(4));
                	Optional<AssistanceResponse> existingResponse = this.findByEmailAndEventId(email, eventId);
                	if(!existingResponse.isPresent()) {
		                AssistanceResponse response = new AssistanceResponse();
		                response.setName(getCellValue(currentRow.getCell(1)));
		                response.setLastName(getCellValue(currentRow.getCell(2)));
		                response.setDocumentNumber(getCellValue(currentRow.getCell(3)));
		                response.setEmail(email);
		                response.setMiembro(getCellValue(currentRow.getCell(5)));
		                response.setRolPrincipal(getCellValue(currentRow.getCell(6)));
		                response.setInvestigadorCarreras(getCellValue(currentRow.getCell(7)));
		                response.setTipoInscripcion(getCellValue(currentRow.getCell(8)));
		                
		                response.setPresent(false);
		                response.setAssistanceCertifySent(false);
		                response.setWelcomeMailSent(false);
		                response.setSource("Externo excel");
		                response.setQRCode(UUID.randomUUID().toString());
		                response.setEvent(event);
		                
		                externalAssistanceResponses.add(response);
                	}
                	currentRow = rows.next();
	                endFile = currentRow.getCell(0) == null || currentRow.getCell(0).getCellType() == CellType.BLANK;
                }
                workbook.close();
            }catch (Exception e) {
				throw e;
			}
            for (AssistanceResponse assistanceResponse : externalAssistanceResponses) {
				this.save(assistanceResponse);
			}
        }
        return externalAssistanceResponses.size();
    }
    
    private String getCellValue(Cell cell) {
        if (cell != null) {
            DataFormatter dataFormatter = new DataFormatter();
            return dataFormatter.formatCellValue(cell);
        }
        return null;
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

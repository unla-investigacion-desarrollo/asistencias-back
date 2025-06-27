package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.EventDays;
import com.unla.eventos.repositories.IAssistanceResponseRepository;
import com.unla.eventos.services.IAssistanceDaysService;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.IEventDaysService;
import com.unla.eventos.services.IEventService;
import com.unla.eventos.services.IMailService;

import java.io.InputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

@Service
public class AssistanceResponseService implements IAssistanceResponseService {

	@Autowired
	private IAssistanceResponseRepository assistanceResponseRepository;

	@Autowired
	private IEventService eventService;

	@Autowired
	private IMailService mailService;

	@Autowired
    private IAssistanceDaysService assistanceDaysService;

	@Autowired
	private IEventDaysService eventDaysService;

	public int importFromExcel(InputStream is, int eventId) throws Exception {
		List<AssistanceResponse> externalAssistanceResponses = new ArrayList<>();

		Optional<Event> eventOp = this.findEventById(eventId);
		if (!eventOp.isPresent())
			return 0;

		Event event = eventOp.get();
		List<EventDays> eventDays = eventDaysService.findByEventId(eventId);
		eventDays.sort(Comparator.comparing(EventDays::getDate));

		final int DAY_COLUMNS_START_INDEX = 15;

		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			if (!rows.hasNext())
				return 0;
			rows.next(); // saltea encabezado

			while (rows.hasNext()) {
				Row currentRow = rows.next();
				if (currentRow.getCell(0) == null || currentRow.getCell(0).getCellType() == CellType.BLANK) {
					break;
				}

				String email = getCellValue(currentRow.getCell(3));
				Optional<AssistanceResponse> existingResponse = this.findByEmailAndEventId(email, eventId);
				if (existingResponse.isPresent())
					continue;

				AssistanceResponse response = new AssistanceResponse();
				response.setName(getCellValue(currentRow.getCell(0)));
				response.setLastName(getCellValue(currentRow.getCell(1)));
				response.setDocumentNumber(getCellValue(currentRow.getCell(2)));
				response.setEmail(email);
				response.setMiembro(getCellValue(currentRow.getCell(4)));
				response.setRolPrincipal(getCellValue(currentRow.getCell(5)));
				response.setRolPrincipalOtro(getCellValue(currentRow.getCell(6)));
				response.setInvestigadorCarreras(getCellValue(currentRow.getCell(7)));
				response.setInvestigadorCarrerasOtro(getCellValue(currentRow.getCell(8)));
				response.setTipoInscripcion(getCellValue(currentRow.getCell(9)));
				response.setSource("Importado desde Excel");
				response.setWelcomeMailSent(false);
				response.setPresent(false);
				response.setAssistanceCertifySent(false);
				response.setQRCode(UUID.randomUUID().toString());
				response.setEvent(event);

				this.save(response);

				for (int i = 0; i < eventDays.size(); i++) {
					int cellIndex = DAY_COLUMNS_START_INDEX + i;
					Cell cell = currentRow.getCell(cellIndex);
					String val = cell != null ? getCellValue(cell).trim().toLowerCase() : "";
					boolean isPresent = val.equals("si") || val.equals("sí") || val.equals("x") || val.equals("✓")
							|| val.equals("true");

					AssistanceDays asistencia = new AssistanceDays();
					asistencia.setAssistanceResponse(response);
					asistencia.setEventDay(eventDays.get(i));
					asistencia.setPresent(isPresent);

					assistanceDaysService.save(asistencia);
				}

				externalAssistanceResponses.add(response);
			}

			return externalAssistanceResponses.size();
		}
	}

    @Async
    public void sendMailsForNewResponses(int eventId) throws Exception {
    	Optional<Event> eventOp = eventService.findById(eventId);
    	if(eventOp.isPresent()) {
    		Event event = eventOp.get();
    		List<AssistanceResponse> responsesToSendEmail = assistanceResponseRepository.findByEventIdAndWelcomeMailSent(eventId, false);
    		System.out.println("INICIA ENVIO DE MAILS DE BIENVENIDA");
    		for (AssistanceResponse response : responsesToSendEmail) {
        		try {
        			mailService.prepareAndSendEmail(response.getQRCode(), response.getName(), response.getLastName(),
        					event.getName(), event.getStartDate(), event.getEndDate(), event.getMailContact(),
        					response.getEmail());
            		response.setWelcomeMailSent(true);
            		this.save(response);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					throw e;
				}
    		}
    		System.out.println("FINALIZA ENVIO DE MAILS DE BIENVENIDA, mails enviados: " + responsesToSendEmail.size());
    	}
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

	@Override
	public List<AssistanceResponse> findByEventIdWithEvent(int eventId) {
		return assistanceResponseRepository.findByEventIdWithEvent(eventId);
	}
}

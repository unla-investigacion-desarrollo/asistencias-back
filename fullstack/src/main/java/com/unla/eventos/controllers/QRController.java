package com.unla.eventos.controllers;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IAssistanceDaysService;
import com.unla.eventos.services.IAssistanceResponseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QRController {

    @Autowired
    private IAssistanceResponseService assistanceResponseService;

	@Autowired
	private IAssistanceDaysService assistanceDaysService;

    @GetMapping("/qr/{qrCode}")
    public String procesarQRCode(@PathVariable String qrCode, Model model) {
    	Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if(user == "anonymousUser") return ViewRouteHelper.QR_ASSISTANCE;
        
    	AssistanceResponse assistanceResponse = assistanceResponseService.findByQRCode(qrCode);
        if (assistanceResponse != null) {
        	if (LocalDateTime.now().isBefore(assistanceResponse.getEvent().getStartDate())) {
        		model.addAttribute("mensaje",
        				"Evento no iniciado. Vuelva a leer el QR después del: " +
        				FunctionsHelper.formatLocalDateToARGTime(assistanceResponse.getEvent().getStartDate()));
        	} else {
				List<AssistanceDays> assistanceDayList = assistanceDaysService.findByAssistanceResponseId(assistanceResponse.getId());
				LocalDate today = LocalDate.now();

				// Verificar si ya se marcó presente hoy
				boolean alreadyPresentToday = assistanceDayList.stream()
						.anyMatch(ad -> ad.getEventDay().getDate().isEqual(today) && ad.isPresent());

				if (!alreadyPresentToday) {
					for (AssistanceDays ad : assistanceDayList) {
						if (ad.getEventDay().getDate().isEqual(today)) {
							ad.setPresent(true);
							assistanceDaysService.save(ad);
						}
					}

					model.addAttribute("mensaje", "Presencia marcada exitosamente. Disfrute del evento.");
					model.addAttribute("status", true);
				} else {
					model.addAttribute("mensaje", "Asistencia ya registrada.");
					model.addAttribute("status", false);
				}
        	}
        } else {
            model.addAttribute("mensaje", "Código QR inválido.");
        }
        return ViewRouteHelper.QR_RESULT;
    }
}

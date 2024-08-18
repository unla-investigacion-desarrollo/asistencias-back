package com.unla.eventos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.io.ByteArrayInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.implementation.ExcelExportService;
import com.unla.eventos.helpers.ViewRouteHelper;

@Controller
@RequestMapping("/responses")
public class AssistanceResponseController {

	@Autowired
    private IAssistanceResponseService assistanceResponseService;
	
	@Autowired
    private ExcelExportService excelExportService;
	
	@GetMapping("/list/{eventId}")
    public String viewEventResponses(@PathVariable int eventId, Model model) {
        List<AssistanceResponse> responses = assistanceResponseService.findByEventId(eventId);
        model.addAttribute("responses", responses);
        return ViewRouteHelper.ASSISTANCE_RESPONSE_INDEX;
    }
	
	@GetMapping("/export/{eventId}")
    public ResponseEntity<Resource> exportEventResponsesToExcel(@PathVariable int eventId) {
        List<AssistanceResponse> responses = assistanceResponseService.findByEventId(eventId);
        ByteArrayInputStream in = excelExportService.exportAssistanceResponsesToExcel(responses);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=event_responses.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
	
	@PostMapping("/import")
    public String handleFileUpload(@RequestParam MultipartFile file, @RequestParam int event, RedirectAttributes redirectAttributes) {
        try {
        	assistanceResponseService.importFromExcel(file.getInputStream(), event);
            redirectAttributes.addFlashAttribute("message", "El archivo se procesó correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Hubo un error al procesar el archivo.");
            e.printStackTrace();
        }
        return ViewRouteHelper.REDIRECT_ASSISTANCE_RESPONSE_INDEX + event;
    }
}

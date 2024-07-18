package com.unla.eventos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IAssistanceResponseService;

@Controller
@RequestMapping("/responses")
public class AssistanceResponseController {

	@Autowired
    private IAssistanceResponseService assistanceResponseService;
	
	@GetMapping("/list/{eventId}")
    public String viewEventResponses(@PathVariable("eventId") int eventId, Model model) {
        List<AssistanceResponse> responses = assistanceResponseService.findByEventId(eventId);
        model.addAttribute("responses", responses);
        return ViewRouteHelper.ASSISTANCE_RESPONSE_INDEX;
    }
}

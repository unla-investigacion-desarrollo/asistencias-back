package com.unla.eventos.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.FeedbackResponse;
import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IEventService;
import com.unla.eventos.services.IFeedbackResponseService;

@Controller
@RequestMapping("/feedback")
public class FeedbackResponseController {

    @Autowired
    private IFeedbackResponseService feedbackResponseService;

    @Autowired
    private IEventService eventService;

    @GetMapping("/{uniqueCode}")
    public String mostrarFormularioFeedback(@PathVariable String uniqueCode, Model model) {
        Optional<Event> eventOp = eventService.findByUniqueCode(uniqueCode);
        if (eventOp.isPresent()) {
            Event event = eventOp.get();

            model.addAttribute("eventName", event.getName());
            model.addAttribute("imagePath", event.getImagePath());
            model.addAttribute("title", event.getTitle());
            model.addAttribute("description", event.getDescription());
            model.addAttribute("eventStartDate", FunctionsHelper.formatLocalDateToARGTime(event.getStartDate()));
            model.addAttribute("eventEndDate", FunctionsHelper.formatLocalDateToARGTime(event.getEndDate()));
            model.addAttribute("uniqueCode", uniqueCode);

            model.addAttribute("feedbackResponse", new FeedbackResponse());

            return ViewRouteHelper.FEEDBACK_INDEX;
        } else {
            return "redirect:/" + ViewRouteHelper.FEEDBACK_NOTFOUND;
        }
    }

    @PostMapping("/submit")
    public String procesarFeedback(@RequestParam String uniqueCode,
                                   @ModelAttribute FeedbackResponse feedbackResponse) {
        Optional<Event> eventOp = eventService.findByUniqueCode(uniqueCode);
        if (eventOp.isPresent()) {
            feedbackResponse.setEvent(eventOp.get());
            feedbackResponseService.save(feedbackResponse);
            return "redirect:/" + ViewRouteHelper.FEEDBACK_EXITO;
        } else {
            return "redirect:/" + ViewRouteHelper.FEEDBACK_NOTFOUND;
        }
    }

    @GetMapping("/exito")
    public String exito() {
        return ViewRouteHelper.FEEDBACK_EXITO;
    }

    @GetMapping("/notfound")
    public String notfound() {
        return ViewRouteHelper.FEEDBACK_NOTFOUND;
    }
}

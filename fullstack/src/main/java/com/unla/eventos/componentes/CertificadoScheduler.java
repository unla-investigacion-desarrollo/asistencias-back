package com.unla.eventos.componentes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.unla.eventos.entities.Event;
import com.unla.eventos.services.ICertificadoService;
import com.unla.eventos.services.IEventService;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CertificadoScheduler {

    @Autowired
    private ICertificadoService certificadoService;

    @Autowired
    private IEventService eventService;

    @Value("${ENVIAR_CERTIFICADOS}")
    private String enviar;

    @Scheduled(cron = "0 0/30 * * * ?") 
    public void runBatch() throws MessagingException {
        if (!"true".equalsIgnoreCase(enviar)) {
            log.info("El envío de certificados está desactivado por configuración (ENVIAR_CERTIFICADOS != true).");
            return;
        }
        int totalEnviados = 0;
        List<Event> eventos = eventService.findAll();
        for(Event event: eventos){
            if (totalEnviados >= 50) break;
            int enviados = certificadoService.enviarCertificados(event.getId(), 50 - totalEnviados);
            totalEnviados += enviados;
        }
        log.info("Certificados enviados en este batch: {}", totalEnviados);
    }
}
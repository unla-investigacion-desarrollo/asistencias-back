package com.unla.asistencias.models.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailModel {
    private String email;
    private String mensaje;
    private String codigo;
}

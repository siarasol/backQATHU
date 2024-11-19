package org.example.backendfi2.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class DeliveryRequestDTO {
    private Long pedidoId;
    private String direccion;
    private String indicaciones;
    private BigDecimal latitud;
    private BigDecimal longitud;
}

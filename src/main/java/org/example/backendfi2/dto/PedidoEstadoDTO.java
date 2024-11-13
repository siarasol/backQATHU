package org.example.backendfi2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PedidoEstadoDTO {
    private Long pedidoId;
    private String estadoPedido;
    private String estadoPago;
    private Long pagoId;
    private BigDecimal totalPago;
    private String tipoPago;
    private LocalDateTime fechaPago;
    private Long archivoId;
    private String archivoNombre;
    private String archivoPath;
    private String estadoDelivery;
}

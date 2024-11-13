package org.example.backendfi2.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.example.backendfi2.model.Pedido;
import org.example.backendfi2.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class QRPaymentService {

    @Autowired
    private PedidoRepository pedidoRepository;

    private static final String CUENTA_BANCARIA = "10000020051904";  // Reemplaza con tu número de cuenta bancaria
    private static final String BANCO = "Banco Unión S.A.";      // Reemplaza con el nombre de tu banco

    public byte[] generarQRPago(Long pedidoId) {
        try {
            // Obtener el pedido
            Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedidoId);
            if (pedidoOptional.isEmpty()) {
                throw new RuntimeException("Pedido no encontrado.");
            }

            Pedido pedido = pedidoOptional.get();
            BigDecimal monto = pedido.getTotal();

            // Crear el texto que se convertirá en QR
            String datosPago = "Banco: " + BANCO + "\n" +
                    "Cuenta: " + CUENTA_BANCARIA + "\n" +
                    "Monto: " + monto + " BOB\n" +
                    "Pedido: " + pedidoId;

            // Generar el código QR
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(datosPago, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error al generar el QR de pago: " + e.getMessage(), e);
        }
    }
}

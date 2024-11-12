package org.example.backendfi2.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Método para enviar correos personalizados con encabezado y pie de página.
     *
     * @param destinatario Correo electrónico del destinatario.
     * @param asunto Asunto del correo.
     * @param cuerpo Cuerpo del mensaje en HTML que cambia según la necesidad.
     * @throws MessagingException Si ocurre algún error al enviar el correo.
     */
    public void enviarCorreoPersonalizado(String destinatario, String asunto, String cuerpo) throws MessagingException {
        String contenidoHtml = construirContenidoHtml(cuerpo);

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(contenidoHtml, true);

        mailSender.send(mensaje);
    }

    /**
     * Construye el contenido HTML del correo con encabezado, cuerpo personalizado y pie de página.
     *
     * @param cuerpo Contenido específico que será agregado entre el encabezado y pie de página.
     * @return Un String con el contenido HTML completo.
     */
    private String construirContenidoHtml(String cuerpo) {
        return """
            <div style="border: 2px solid #000; border-radius: 5px; max-width: 600px; margin: 20px auto; padding: 20px; font-family: Arial, sans-serif; background-color: #f8f9fa;">
                <div style="text-align: center; padding: 10px; background-color: #0D5E32; color: #FFFFFF;">
                    <h2 style="margin: 0; font-size: 24px;">QHATU BOLIVIA</h2>
                    <p style="margin: 0; font-size: 16px; color: #FCD116;">Tienda Virtual</p>
                </div>
                <div style="padding: 15px;">
                    %s
                </div>
                <div style="text-align: center; padding: 10px; background-color: #CE1126; color: #FFFFFF;">
                    <p style="font-size: 14px; margin: 0;">Gracias por confiar en nosotros, QHATU BOLIVIA.</p>
                </div>
            </div>
            """.formatted(cuerpo);
    }
}

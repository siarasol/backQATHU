package org.example.backendfi2.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://front-qhatu-i1311ki4u-garys-projects-4b811477.vercel.app") // Permite este origen
                //.allowedOrigins("http://localhost:4200") // Permite este origen
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos permitidos
                .allowedHeaders("*") // Permite todos los headers
                .allowCredentials(true); // Permite el uso de cookies/credenciales si es necesario

        // Configuración CORS para acceso a archivos de imágenes
        registry.addMapping("/imagenes/**")
                .allowedOrigins("https://front-qhatu-i1311ki4u-garys-projects-4b811477.vercel.app") // Permite este origen
                //.allowedOrigins("http://localhost:4200") // Permite este origen
                .allowedMethods("GET") // Solo GET para descargar imágenes
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}

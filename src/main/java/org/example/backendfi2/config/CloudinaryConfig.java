package org.example.backendfi2.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dlz01zss5",
                "api_key", "147269221379532",
                "api_secret", "ycQkfVZkPRgbtAFGUHYzaO9lY64"));
    }
}

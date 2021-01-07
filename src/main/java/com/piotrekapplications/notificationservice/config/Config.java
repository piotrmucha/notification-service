package com.piotrekapplications.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
public class Config {
    @Bean
    public String confirmationPage() {
        try {
            return StreamUtils
                    .copyToString(new ClassPathResource("test.html").getInputStream(),
                            Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}

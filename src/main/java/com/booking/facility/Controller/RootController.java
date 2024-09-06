package com.booking.facility.Controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> getHelp() {
        try {
            // Read the README.md file from the classpath
            ClassPathResource readmeResource = new ClassPathResource("README.md");
            String readmeContent = StreamUtils.copyToString(readmeResource.getInputStream(), StandardCharsets.UTF_8);

            return ResponseEntity.ok(readmeContent);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading help content");
        }
    }
}

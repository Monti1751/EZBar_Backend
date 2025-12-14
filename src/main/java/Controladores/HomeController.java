package Controladores;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<h1>EZBar Backend is Running!</h1>" +
                "<p>Use the following endpoints:</p>" +
                "<ul>" +
                "<li><a href='/seed'>/seed</a> - Populate Database</li>" +
                "<li><a href='/api/zonas'>/api/zonas</a> - View Zones</li>" +
                "</ul>";
    }
}

package taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {

    @Operation(
            summary = "Hello World",
            description = "Hello World Example"
    )
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello World";
    }
}

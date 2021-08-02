package com.santos.api.controller.health;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public Health health(){
        return new Health("UP");
    }

    @Getter
    @RequiredArgsConstructor
    private static class Health {
        private final String status;
    }
}

package com.househub.backend.domain.agent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agents")
public class AgentController {
    @GetMapping("")
    public String hello() {
        return "Hello, World!";
    }
}

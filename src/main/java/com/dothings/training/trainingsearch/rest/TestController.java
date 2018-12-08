package com.dothings.training.trainingsearch.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${name}")
    private  String name;

    @GetMapping("/get")
    public String getRest() {
        return name;
    }
}

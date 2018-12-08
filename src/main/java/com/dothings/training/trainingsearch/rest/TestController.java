package com.dothings.training.trainingsearch.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/get")
    public String getRest() {
        return "哈哈哈哈";
    }
}

package com.aluracursos.screematch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {

    @GetMapping("/series")
    public String getSeries() {
        return "Este es mi primer servicio web";
    }
}

package com.aluracursos.screematch.controller;

import com.aluracursos.screematch.dto.SerieDTO;
import com.aluracursos.screematch.model.Serie;
import com.aluracursos.screematch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obtenerTodasLasSeries() {
        return repository.findAll().stream()
                .map(s -> new SerieDTO(s.getTitulo(), s.getTotalTemporadas(),
                        s.getEvaluacion(), s.getPoster(), s.getGenero().toString(),
                        s.getActores(), s.getSinopsis())).collect(Collectors.toList());
    }

    @GetMapping("/inicio")
    public  String muestraMensaje() {
        return "Hola, bienvenido a ScreenMatch";
    }
}

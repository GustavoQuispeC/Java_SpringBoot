package com.aluracursos.screematch.dto;

import com.aluracursos.screematch.model.Categoria;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double evaluacion,
                       String poster,
                       Categoria genero,
                       String actores,
                       String sinopsis){
}
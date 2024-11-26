package com.aluracursos.screematch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosEpisodio(

        @JsonAlias("Title") String titulo,
        @JsonAlias("Episode") String numeroEpisodio,
        @JsonAlias("imdbRating") String Evaluaciones,
        @JsonAlias("Released") String fechaDeLanzamiento
) {

}

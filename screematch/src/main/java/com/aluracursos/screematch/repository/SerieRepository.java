package com.aluracursos.screematch.repository;

import com.aluracursos.screematch.model.Categoria;
import com.aluracursos.screematch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero (Categoria categoria);


}

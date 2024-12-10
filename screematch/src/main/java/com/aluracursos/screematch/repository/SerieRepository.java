package com.aluracursos.screematch.repository;

import com.aluracursos.screematch.model.Categoria;
import com.aluracursos.screematch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero (Categoria categoria);

    //List<Serie> findByTotalTemporadasLessThanEqualAndEvalucionGreaterThanEqual(int totalTemporadas, Double evaluacion);

    @Query(value ="SELECT * FROM series  WHERE series.total_temporadas <= 6 and series.evaluacion >= 7.5",nativeQuery = true)
    List<Serie> seriesPorTemporadaYEvaluacion(int total_temporadas, Double evaluacion);
}

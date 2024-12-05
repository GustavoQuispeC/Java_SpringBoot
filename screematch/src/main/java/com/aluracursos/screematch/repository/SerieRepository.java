package com.aluracursos.screematch.repository;

import com.aluracursos.screematch.model.Categoria;
import com.aluracursos.screematch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SerieRepository extends JpaRepository<Serie, Long> {

}

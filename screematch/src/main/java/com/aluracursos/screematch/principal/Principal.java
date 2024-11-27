package com.aluracursos.screematch.principal;

import com.aluracursos.screematch.model.DatosEpisodio;
import com.aluracursos.screematch.model.DatosSerie;
import com.aluracursos.screematch.model.DatosTemporadas;
import com.aluracursos.screematch.model.Episodio;
import com.aluracursos.screematch.service.ConsumoAPI;
import com.aluracursos.screematch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String  API_KEY = "&apikey=b5d7212b";
    private ConvierteDatos conversor = new ConvierteDatos();


    public void muestraElMenu(){
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar:");

        //Busca los datos de la serie
        var nombreSerie = teclado.nextLine();

        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);

        //var datos = conversor.obtenerDatos(json, DatosSerie.class);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        //temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para la temporada
//        for (int i = 0; i < datos.totalTemporadas(); i++) {
//            List<DatosEpisodio> epidosiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < epidosiosTemporada.size() ; j++) {
//                System.out.println(epidosiosTemporada.get(j).titulo());
//
//            }
//        }
        //Funcion lambda para mostrar solo el titulo de los episodios para la temporada
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //convertir todas las informaciones a una lista de tipo DAtosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Top 5 de episodios
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);

        //Convirtiendo los datos a una lista de tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);

        //Busqueda de episodios a partir de un X año
        System.out.println("Por favor escribe el año a partir del cual deseas buscar los episodios:");
        var fecha = teclado.nextInt();
        teclado.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episodio: " + e.getTitulo() +
                                " Fecha de lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)

                        )
                );


    }
}

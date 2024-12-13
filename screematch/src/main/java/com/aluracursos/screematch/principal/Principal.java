package com.aluracursos.screematch.principal;

import com.aluracursos.screematch.model.*;
import com.aluracursos.screematch.repository.SerieRepository;
import com.aluracursos.screematch.service.ConsumoAPI;
import com.aluracursos.screematch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;


public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=b5d7212b";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;

    private List<Serie> series;

    private Optional<Serie> seriesBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - Top 5 mejores series
                    6 - Buscar series por Categoria
                    7 - Filtrar series por temporada y evaluación      
                    8 - Buscar episodios por titulo 
                    9 - Top 5 episodios de una serie       
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    mostrarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }


    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    //Buscar episodios por serie
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la seria de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    //Mostrar series buscadas
    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    //Buscar series por titulo
    private void mostrarSeriesPorTitulo() {
        System.out.println("Escribe el titulo de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        seriesBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (seriesBuscada.isPresent()) {
            System.out.println(seriesBuscada.get());
        } else {
            System.out.println("No se encontró la serie");

        }
    }

    //Buscar las 5 mejores series
    private void buscarTop5Series() {
        List<Serie> top5 = repositorio.findTop5ByOrderByEvaluacionDesc();
        top5.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " Evaluación: " + s.getEvaluacion()));
    }

    //Buscar series por categoria
    private void buscarSeriesPorCategoria() {
        System.out.println("Escriba el genero/categoría de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoría " + genero);
        seriesPorCategoria.forEach(System.out::println);
    }

    //Filtrar series por temporada y evaluación
    public void filtrarSeriesPorTemporadaYEvaluacion() {
        System.out.println("Filtrar series con cuantas temporadas?");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Con evaluación apartir de cual valor?");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Series Filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + " - evaluacion: " + s.getEvaluacion())
        );

    }

    //Buscar episodios por titulo
    private void buscarEpisodiosPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada: %s Evaluación: %s\n",
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

    }

    //Buscar los 5 mejores episodios de una serie
    private void buscarTop5Episodios() {
        buscarEpisodiosPorTitulo();
        if (seriesBuscada.isPresent()) {
            Serie serie = seriesBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e -> System.out.printf("Serie: %s Temporada: %s Evaluación: %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

        }
    }

}
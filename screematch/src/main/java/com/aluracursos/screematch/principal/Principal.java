package com.aluracursos.screematch.principal;

import com.aluracursos.screematch.model.DatosEpisodio;
import com.aluracursos.screematch.model.DatosSerie;
import com.aluracursos.screematch.model.DatosTemporadas;
import com.aluracursos.screematch.service.ConsumoAPI;
import com.aluracursos.screematch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para la temporada
        for (int i = 0; i < datos.totalTemporadas(); i++) {
            List<DatosEpisodio> epidosios = temporadas.get(i).episodios();
        }




    }
}

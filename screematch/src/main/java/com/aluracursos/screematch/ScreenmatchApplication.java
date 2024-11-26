package com.aluracursos.screematch;

import com.aluracursos.screematch.model.DatosEpisodio;
import com.aluracursos.screematch.model.DatosSerie;
import com.aluracursos.screematch.model.DatosTemporadas;
import com.aluracursos.screematch.service.ConsumoAPI;
import com.aluracursos.screematch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//System.out.println("Hello World! desde spring boot");
		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&apikey=b5d7212b");
		//System.out.println(json);

//		var json = consumoApi.obtenerDatos("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);

		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);

		json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&&Season=1&apikey=b5d7212b");
		DatosEpisodio episodios = conversor.obtenerDatos(json, DatosEpisodio.class);
		System.out.println(episodios);

		List<DatosTemporadas> temporadas = new ArrayList<>();
		for (int i = 1; i <= datos.totalTemporadas(); i++) {

			json = consumoApi.obtenerDatos("https://www.omdbapi.com/?t=game+of+thrones&&Season=" + i + "&apikey=b5d7212b");
			var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
			temporadas.add(datosTemporadas);

		}
		temporadas.forEach(System.out::println);

	}
}

package com.aluracursos.screematch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void  muestraEjemplo(){
        List<String> nombres= Arrays.asList("Juan", "Pedro", "Maria", "Luis", "Ana", "Lucia", "Carlos", "Jose", "Luisa", "Pablo");
        nombres.stream()
                .sorted()
                .limit(8)
                .filter(n -> n.startsWith("L"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}

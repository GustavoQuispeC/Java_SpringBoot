package com.aluracursos.screematch.model;

import static java.awt.SystemColor.text;

public enum Categoria {
    Accion("Action", "Acción"),
    Romance("Romance", "Romance"),
    Comedia("Comedy", "Comedia"),
    Drama("Drama", "Drama"),
    Crimen("Crime", "Crimen"),
    ;

    private String categoriaOmdb;

    private String categoriaEspanol;

    Categoria(String categoriaOmdb, String categoriaEspanol) {
        {
            this.categoriaOmdb = categoriaOmdb;
            this.categoriaEspanol = categoriaEspanol;
        }
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
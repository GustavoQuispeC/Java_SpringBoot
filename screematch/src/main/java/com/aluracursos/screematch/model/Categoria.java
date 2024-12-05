package com.aluracursos.screematch.model;

public enum Categoria {
    Accion("Action"),
    Romance("Romance"),
    Comedia("Comedy"),
    Drama("Drama"),
    Crimen("Crime");

    private String categoriaOmdb;
    Categoria (String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}

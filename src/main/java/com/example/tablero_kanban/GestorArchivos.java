package com.example.tablero_kanban;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    public static String archivoTareas = "tareas.txt";
    public static String archivoProyectos = "proyectos.txt";

    public static void inicializarDatos() {
        File fProyectos = new File(archivoProyectos);
        File fTareas = new File(archivoTareas);

        try {
            if (fProyectos.createNewFile()) {
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fProyectos)))) {
                    out.println("1|Proyecto Universidad");
                    out.println("2|Proyecto Personal");
                }
            }
            if (fTareas.createNewFile()) {
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fTareas)))) {
                    out.println("1|Estudiar Fisica|Repasar informes del laboratorio|" + LocalDate.now().plusDays(2)
                            + "|Alta|Por hacer|1");
                    out.println("2|Programar Vaadin|Hacer el proyecto final de lógica|" + LocalDate.now().minusDays(1)
                            + "|Meida|En progreso|1");
                    out.println("3|Comprar comida|Para ver peliculas|" + LocalDate.now() + "|Baja|Hecho|2");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al crear archivos: " + e.getMessage());
        }
    }

    public static List<Tarea> leerTareas() {
        List<Tarea> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoTareas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");

                Tarea t = new Tarea();
                t.id = Integer.parseInt(datos[0]);
                t.titulo = datos[1];
                t.descripcion = datos[2];
                t.fechaLimite = LocalDate.parse(datos[3]);
                t.prioridad = datos[4];
                t.estado = datos[5];
                t.idProyecto = Integer.parseInt(datos[6]);

                lista.add(t);
            }
        } catch (Exception e) {
            System.out.println("Error al leer tareas: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarTareas(List<Tarea> listaTareas) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(archivoTareas)))) {
            for (Tarea t : listaTareas) {
                out.println(t.id + "|" + t.titulo + "|" + t.descripcion + "|" + t.fechaLimite + "|" + t.prioridad + "|"
                        + t.estado + "|" + t.idProyecto);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar tareas: " + e.getMessage());
        }
    }
}

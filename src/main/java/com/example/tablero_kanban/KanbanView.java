package com.example.tablero_kanban;

import java.util.List;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Tablero Kanban")
public class KanbanView extends VerticalLayout {

    public List<Tarea> todasLasTareas;

    public VerticalLayout columnaPorHacer;
    public VerticalLayout columnaEnProgreso;
    public VerticalLayout columnaHecho;

    public KanbanView() {
        setSizeFull();

        todasLasTareas = GestorArchivos.leerTareas();

        Select<String> filtroPrioridad = new Select<>();
        filtroPrioridad.setLabel("Filtrar por prioridad");
        filtroPrioridad.setItems("Todas", "Alta", "Media", "Baja");
        filtroPrioridad.setValue("Todas");

        filtroPrioridad.addValueChangeListener(e -> {dibujarTablero(e.getValue());});

        columnaPorHacer = new VerticalLayout();
        columnaEnProgreso = new VerticalLayout();
        columnaHecho = new VerticalLayout();

        HorizontalLayout tableroLayout = new HorizontalLayout();
        tableroLayout.add(columnaPorHacer, columnaEnProgreso, columnaHecho);
        tableroLayout.setWidthFull();
        
        tableroLayout.getStyle().set("flex-wrap", "wrap");

        add(filtroPrioridad, tableroLayout);
    
        dibujarTablero("Todas");
        revisarVencimientos();

        }

        public void dibujarTablero(String filtro){
            columnaPorHacer.removeAll();
            columnaEnProgreso.removeAll();
            columnaHecho.removeAll();

            columnaPorHacer.add(new H2("Por hacer"));
            columnaEnProgreso.add(new H2("En progreso"));
            columnaHecho.add(new H2("Hecho"));

            for (Tarea t : todasLasTareas) {

                if (filtro.equals("Todas") || t.prioridad.equals(filtro)) {

                    VerticalLayout tarjeta = crearTarjeta(t);

                    if (t.estado.equals("Por hacer")) {
                        columnaPorHacer.add(tarjeta);
                    } else if (t.estado.equals("En progreso")) {
                        columnaEnProgreso.add(tarjeta);
                    } else if (t.estado.equals("Hecho")) {
                        columnaHecho.add(tarjeta);
                    }
                }
            }
        }

    }



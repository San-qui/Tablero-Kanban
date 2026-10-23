package com.example.tablero_kanban;

import java.util.List;

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
        

    }

}

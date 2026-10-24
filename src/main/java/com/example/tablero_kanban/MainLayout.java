package com.example.tablero_kanban;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MainLayout extends AppLayout {
    public MainLayout() {
        GestorArchivos.inicializarDatos();

        DrawerToggle botonMenu = new DrawerToggle();

        H1 titulo = new H1("Mi kanban");

        HorizontalLayout barraSuperior = new HorizontalLayout();
        barraSuperior.add(botonMenu, titulo);

        Button btnTablero = new Button("Tablero Kanban", e -> UI.getCurrent().navigate(KanbanView.class));

        Button btnProyectos = new Button("Gestión de Proyectos", e -> UI.getCurrent().navigate(ProyectosView.class));

        Button btnTareas = new Button("Gestión de Tareas", e -> UI.getCurrent().navigate(TareasView.class));

        VerticalLayout menuLateral = new VerticalLayout();
        menuLateral.add(btnTablero, btnProyectos, btnTareas);

        addToDrawer(menuLateral);
    }

}

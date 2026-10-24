package com.example.tablero_kanban;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "proyectos", layout = MainLayout.class)
@PageTitle("Gestión de proyectos")
public class ProyectosView extends VerticalLayout {

    public List<Proyecto> listaProyectos;
    public Grid<Proyecto> gridProyectos;

    public ProyectosView() {
        listaProyectos = GestorArchivos.leerProyectos();

        add(new H1("Proyectos"));

        TextField txtNombre = new TextField("Nombre del nuevo proyecto");
        Button btnGuardar = new Button("Guardar");

        btnGuardar.addClickListener(e -> {
            if (txtNombre.isEmpty()) {
                Notification.show("Error: El nombre es obligatorio");
            } else {
                int nuevoId = 1;
                if (!listaProyectos.isEmpty()) {
                    nuevoId = listaProyectos.get(listaProyectos.size() - 1).id + 1;
                }
                Proyecto nuevoProyecto = new Proyecto();
                nuevoProyecto.id = nuevoId;
                nuevoProyecto.nombre = txtNombre.getValue();

                listaProyectos.add(nuevoProyecto);
                GestorArchivos.guardarProyectos(listaProyectos);

                gridProyectos.getDataProvider().refreshAll();
                txtNombre.clear();
                Notification.show("Proyecto guardado con exito");
            }
        });

        HorizontalLayout formulario = new HorizontalLayout(txtNombre, btnGuardar);
        formulario.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        add(formulario);

    }

}

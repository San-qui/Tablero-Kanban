package com.example.tablero_kanban;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
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

        gridProyectos = new Grid<>(Proyecto.class, false);
        gridProyectos.addColumn(p -> p.id).setHeader("ID").setWidth("80px").setFlexGrow(0);
        gridProyectos.addColumn(p -> p.nombre).setHeader("Nombre del Proyecto");

        gridProyectos.addComponentColumn(proyectoClickeado -> {
            Button btnBorrar = new Button("Borrar");
            btnBorrar.addClickListener(e -> abrirDialogoConfirmacion(proyectoClickeado));
            return btnBorrar;
        });

        gridProyectos.setItems(listaProyectos);
        add(gridProyectos);
    }
    public void abrirDialogoConfirmacion(Proyecto p){
        Dialog dialogo = new Dialog();
        dialogo.setHeaderTitle("¿Borrar proyecto?");
        dialogo.add(new Paragraph("¿Estás seguro que desea borrar el proyecto '" + p.nombre + "'? Esta acción no se puede deshacer. "));

        Button btnCancelar = new Button("Cancelar", e -> dialogo.close());
        Button btnConfirmar = new Button("Sí, borrar", e ->{
            listaProyectos.remove(p);
            GestorArchivos.guardarProyectos(listaProyectos);
            gridProyectos.getDataProvider().refreshAll();
            Notification.show("Proyecto borrado");
            dialogo.close();
        });
        dialogo.getFooter().add(btnCancelar, btnConfirmar);
        dialogo.open();
    }
}

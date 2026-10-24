package com.example.tablero_kanban;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "tareas", layout = MainLayout.class)
@PageTitle("Gestión de Tareas")
public class TareasView extends VerticalLayout {

    public List<Tarea> listaTareas;
    public List<Proyecto> listaProyectos;
    public Grid<Tarea> gridTareas;

    public TareasView() {
        listaTareas = GestorArchivos.leerTareas();
        listaProyectos = GestorArchivos.leerProyectos();

        add(new H1("Administración de Tareas"));

        TextField txtTitulo = new TextField("Título");
        TextField txtDesc = new TextField("Descripción");
        DatePicker pickerFecha = new DatePicker("Fecha Límite");

        Select<String> selectPrioridad = new Select<>();
        selectPrioridad.setLabel("Prioridad");
        selectPrioridad.setItems("Alta", "Media", "Baja");

        Select<Proyecto> selectProyecto = new Select<>();
        selectProyecto.setLabel("Proyecto Asociado");
        selectProyecto.setItems(listaProyectos);
        selectProyecto.setItemLabelGenerator(p -> p.nombre);

        FormLayout formulario = new FormLayout();
        formulario.add(txtTitulo, txtDesc, pickerFecha, selectPrioridad, selectProyecto);

        Button btnGuardar = new Button("Crear Tarea");
        btnGuardar.addClickListener(e -> {
            if (txtTitulo.isEmpty() || pickerFecha.isEmpty() || selectPrioridad.isEmpty() || selectProyecto.isEmpty()) {
                Notification.show("Error: Llena todos los campos obligatorios");
            } else {
                int nuevoId = 1;
                if (!listaTareas.isEmpty()) {
                    nuevoId = listaTareas.get(listaTareas.size() - 1).id + 1;
                }

                Tarea nuevaTarea = new Tarea();
                nuevaTarea.id = nuevoId;
                nuevaTarea.titulo = txtTitulo.getValue();
                nuevaTarea.descripcion = txtDesc.getValue();
                nuevaTarea.fechaLimite = pickerFecha.getValue();
                nuevaTarea.prioridad = selectPrioridad.getValue();
                nuevaTarea.estado = "Por hacer";
                nuevaTarea.idProyecto = selectProyecto.getValue().id;

                listaTareas.add(nuevaTarea);
                GestorArchivos.guardarTareas(listaTareas);
                gridTareas.getDataProvider().refreshAll();

                Notification.show("Tarea creada con éxito");

                txtTitulo.clear();
                txtDesc.clear();
                pickerFecha.clear();
                selectPrioridad.clear();
                selectProyecto.clear();
            }
        });
    }

}

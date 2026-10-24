package com.example.tablero_kanban;

import java.time.LocalDate;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
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

    public Select<String> filtroPrioridad;

    public KanbanView() {
        setSizeFull();

        todasLasTareas = GestorArchivos.leerTareas();

        filtroPrioridad = new Select<>();
        filtroPrioridad.setLabel("Filtrar por prioridad");
        filtroPrioridad.setItems("Todas", "Alta", "Media", "Baja");
        filtroPrioridad.setValue("Todas");

        filtroPrioridad.addValueChangeListener(e -> {
            dibujarTablero(e.getValue());
        });

        columnaPorHacer = new VerticalLayout();
        columnaEnProgreso = new VerticalLayout();
        columnaHecho = new VerticalLayout();

        columnaPorHacer.getStyle().set("background-color", "#f4f5f7");
        columnaEnProgreso.getStyle().set("background-color", "#f4f5f7");
        columnaHecho.getStyle().set("background-color", "#f4f5f7");

        columnaPorHacer.setMinWidth("300px");
        columnaEnProgreso.setMinWidth("300px");
        columnaHecho.setMinWidth("300px");

        columnaPorHacer.setWidth(null);
        columnaEnProgreso.setWidth(null);
        columnaHecho.setWidth(null);

        configurarColumnaDrop(columnaPorHacer, "Por hacer");
        configurarColumnaDrop(columnaEnProgreso, "En progreso");
        configurarColumnaDrop(columnaHecho, "Hecho");

        HorizontalLayout tableroLayout = new HorizontalLayout();
        tableroLayout.add(columnaPorHacer, columnaEnProgreso, columnaHecho);
        tableroLayout.setWidthFull();

        tableroLayout.getStyle().set("flex-wrap", "wrap");

        tableroLayout.setFlexGrow(1, columnaPorHacer, columnaEnProgreso, columnaHecho);

        add(filtroPrioridad, tableroLayout);

        dibujarTablero("Todas");
        revisarVencimientos();

    }

    public void dibujarTablero(String filtro) {
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

    public VerticalLayout crearTarjeta(Tarea t) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("background-color", "white");
        card.getStyle().set("border", "1px solid #ccc");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
        card.setWidthFull();

        Paragraph pTitulo = new Paragraph(t.titulo);

        Paragraph pInfo = new Paragraph("Prioridad: " + t.prioridad + " | Vence: " + t.fechaLimite);

        LocalDate hoy = LocalDate.now();

        if (t.fechaLimite.isBefore(hoy) && !t.estado.equals("Hecho")) {
            card.getStyle().set("background-color", "#ffe6e6");
            pInfo.setText(pInfo.getText() + " (Vencida)");
        }

        Checkbox checkSubtarea = new Checkbox("Marcar Subtarea como lista");

        checkSubtarea.addValueChangeListener(e -> {
            if (e.getValue() == true) {
                Notification.show("Subtarea completada").setPosition(Position.BOTTOM_END);
            }
        });
        HorizontalLayout botonesMover = new HorizontalLayout();

        Button btnRetroceder = new Button("←");
        btnRetroceder.addClickListener(e -> {
            if (t.estado.equals("Hecho"))
                t.estado = "En progreso";
            else if (t.estado.equals("En progreso"))
                t.estado = "Por hacer";

            GestorArchivos.guardarTareas(todasLasTareas);
            dibujarTablero(filtroPrioridad.getValue());
        });

        Button btnAvanzar = new Button("→");
        btnAvanzar.addClickListener(e -> {
            if (t.estado.equals("Por hacer"))
                t.estado = "En progreso";
            else if (t.estado.equals("En progreso"))
                t.estado = "Hecho";

            GestorArchivos.guardarTareas(todasLasTareas);
            dibujarTablero(filtroPrioridad.getValue());
        });

        if (t.estado.equals("Por hacer"))
            btnRetroceder.setVisible(false);
        if (t.estado.equals("Hecho"))
            btnAvanzar.setVisible(false);

        botonesMover.add(btnRetroceder, btnAvanzar);

        card.add(pTitulo, new Paragraph(t.descripcion), pInfo, checkSubtarea, botonesMover);

        DragSource<VerticalLayout> dragSource = DragSource.create(card);
        dragSource.setDraggable(true);

        dragSource.setDragData(t);

        return card;
    }

    public void revisarVencimientos() {
        LocalDate hoy = LocalDate.now();
        LocalDate manana = hoy.plusDays(1);

        for (Tarea t : todasLasTareas) {
            if (!t.estado.equals("Hecho")) {
                if (t.fechaLimite.isEqual(hoy)) {
                    Notification.show("La tarea '" + t.titulo + "' vence hoy.").setPosition(Position.TOP_CENTER);
                } else if (t.fechaLimite.isEqual(manana)) {
                    Notification.show("La tarea '" + t.titulo + "' vence mañana.").setPosition(Position.TOP_CENTER);
                }
            }
        }
    }

    public void configurarColumnaDrop(VerticalLayout columna, String estadoDestino) {
        DropTarget<VerticalLayout> dropTarget = DropTarget.create(columna);
        dropTarget.setDropEffect(DropEffect.MOVE);

        dropTarget.addDropListener(evento -> {
            evento.getDragData().ifPresent(datos -> {
                if (datos instanceof Tarea) {
                    Tarea tareaArrastrada = (Tarea) datos;

                    if (!tareaArrastrada.estado.equals(estadoDestino)) {
                        tareaArrastrada.estado = estadoDestino;
                        GestorArchivos.guardarTareas(todasLasTareas);
                        dibujarTablero(filtroPrioridad.getValue());
                    }
                }
            });
        });
    }
}

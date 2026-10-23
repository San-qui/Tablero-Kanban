package com.example.tablero_kanban;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cglib.core.Local;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
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
        }
    }



package com.example.application.views.list;

import com.example.application.data.entity.TipoComida;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

/**
 * A Designer generated component for the hc-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@PWA(name = "My App", shortName = "My App")
@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Raciones | Vaadin CRM")
@Route(value = "")
public class HCMainView extends VerticalLayout{

    //Components visuals
    private ComboBox<TipoComida> tipoComida;
    private DatePicker datePicker = new DatePicker("Fecha:");
    private Button nueva;

    private Button guardar;  //para volver a la selección de fecha y tipo de comida, guardando la ingesta

    private HCViewNoDesigner ingesta;   //datos de la ingesta


    //Servei d'accés a dades
    CrmService service;

    /**
     * Creates a new HcView.
     */
    @Autowired
    public HCMainView(CrmService service) {
        this.service=service;

        //Primera línia en tipo comida, data i botó
        HorizontalLayout hl1=new HorizontalLayout();
        hl1.setWidthFull();
        configureCombos();
        configureFecha();
        configureBotons();

        hl1.add(tipoComida,  datePicker);
//        footerLayout.addComponent(userLabel);
//        hl1.setComponentAlignment(tipoComida, Alignment.R);
        hl1.setAlignItems(Alignment.START);

        HorizontalLayout hl2=new HorizontalLayout();
        hl2.setWidthFull();
        hl2.add(nueva, guardar);

        add(hl1, hl2);
        setSizeFull();
    }

    private void configureFecha() {
        datePicker.setValue(LocalDate.now());
    }


    private void configureCombos() {
        tipoComida=new ComboBox<>("Horario de la comida");
        tipoComida.setPlaceholder("Elige una comida...");
        tipoComida.setItems(TipoComida.values());
        tipoComida.setValue(TipoComida.values()[0]);
        tipoComida.setItemLabelGenerator(t -> t.getName());


    }

    private void configureBotons() {
        nueva = new Button("Añadir");
        nueva.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nueva.setEnabled(true);
        nueva.setAutofocus(true);
        nueva.setDisableOnClick(true);

        nueva.addClickListener(
                e -> {
                    tipoComida.setEnabled(false);
                    datePicker.setEnabled(false);
                    guardar.setEnabled(true);
                    ingesta=new HCViewNoDesigner(service, datePicker.getValue(), tipoComida.getValue());
                    add(ingesta);
                }
        );

        //Botón de  hacer desaparecer la ingesta actual, y mostrar otra vez la selección de tipo de
        // comida y fecha, y botón de nueva ingesta
        guardar = new Button("Guardar");
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardar.setEnabled(false);
        guardar.addClickListener(
                e -> {
                    nueva.setEnabled(true);
                    tipoComida.setEnabled(true);
                    datePicker.setEnabled(true);
                    guardar.setEnabled(false);
                    this.remove(ingesta);
                }
        );

    }


}

package com.example.application.views.list;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.entity.TipoComida;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A Designer generated component for the hc-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Contacts | Vaadin CRM")
@Route(value = "demo")
public class HCViewNoDesigner  extends VerticalLayout{

    //Components visuals
    private ComboBox<TipoComida> tipoComida;
    private ComboBox<Alimento> alimento;
    private NumberField racions;
    private IntegerField grams;
    private Grid<Ingesta> consumido= new Grid<Ingesta>(Ingesta.class);
    private DatePicker datePicker = new DatePicker("Fecha:");
    private Button afegir;
    private Button modificar;
    private Button eliminar;
    private Button resetejar;

    private void configureBotons() {
        afegir = new Button("Añadir");
        afegir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        afegir.setEnabled(false);

        modificar = new Button("Modificar");
        modificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        modificar.setEnabled(false);

        eliminar = new Button("Eliminar");
        eliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        eliminar.setEnabled(false);
    }


    //Variables útils
    private int grPerRacio;

    //Servei d'accés a dades
    CrmService service;

    /**
     * Creates a new HcView.
     */
    @Autowired
    public HCViewNoDesigner(CrmService service) {
        this.service=service;

        //Primera línia en tipo comida, aliment, racions/grams
        HorizontalLayout hl1=new HorizontalLayout();
        hl1.setWidthFull();
        configureCombos();
        configureCampsNumerics();

        hl1.add(tipoComida, alimento, racions, grams, datePicker);

        //Segona línia en grid i botons CRUD
        HorizontalLayout hl2=new HorizontalLayout();
        hl2.setSizeFull();

        configureGrid();

        //Botons en vertical
        VerticalLayout vl1=new VerticalLayout();
        vl1.setHeightFull();

        configureBotons();
        vl1.add(afegir,modificar,eliminar);

        hl2.add(consumido, vl1);


        add(hl1, hl2);

        setSizeFull();
    }


    private void configureCampsNumerics() {
        racions = new NumberField("Raciones:");
        racions.setValue(1.0);
        racions.setStep(0.5);
        racions.setHasControls(true);
        racions.setMin(0.5);
        racions.setMax(8.5);
        racions.setValueChangeMode(ValueChangeMode.LAZY);

        racions.addValueChangeListener(
                e -> {
                    grams.setValue(Double.valueOf(e.getValue()*grPerRacio).intValue());

                }
        );


        grams = new IntegerField("Gramos:");
        grams.setValue(1);
        grams.setStep(5);
        grams.setHasControls(true);
        grams.setMin(0);
        Div gramSufix = new Div();
        gramSufix.setText("gr");
        grams.setSuffixComponent(gramSufix);
        grams.setValueChangeMode(ValueChangeMode.LAZY);

        grams.addValueChangeListener(
                e -> {
                    racions.setValue((double)e.getValue()/grPerRacio);
                }
        );

        //desactivem els camps numèrics mentre no tinguem aliment seleccionat
        activaCampsNumerics(false);

    }

    private void configureCombos() {
        tipoComida=new ComboBox<>("Horario de la comida");
        tipoComida.setPlaceholder("Elige una comida...");
        tipoComida.setItems(TipoComida.values());
        tipoComida.setValue(TipoComida.values()[0]);
        tipoComida.setItemLabelGenerator(t -> t.getName());

        alimento=new ComboBox<>("Alimento");
        alimento.setPlaceholder("Elige un alimento...");
        alimento.setItems(service.findAllAlimentos());
        alimento.setItemLabelGenerator(t -> t.getNombre());
        alimento.addValueChangeListener(
                e -> {
                    activaCampsNumerics(true);
                    afegir.setEnabled(true);
                    grPerRacio=service.findAlimentoByNombre(e.getValue().getNombre()).getGrRacion();
                    grams.setValue(Double.valueOf(racions.getValue()*grPerRacio).intValue());
                }

        );
//        add(tipoComida);
    }

    private void activaCampsNumerics(boolean b) {
        racions.setEnabled(b);
        grams.setEnabled(b);
    }

    private void configureGrid() {
        consumido.setSizeFull();
//        consumido.addColumn(ingesta -> ingesta.getAlimento().getNombre()).setHeader("Alimento");
//        consumido.setColumns("Alimento", "Raciones");
        consumido.addColumn(ingesta -> ingesta.getAlimento().getNombre()).setHeader("Alimento");
        consumido.setColumns("alimento", "raciones");
        consumido.getColumns().forEach(col -> col.setAutoWidth(true));
        consumido.setItems(service.findAllIngestas());
        consumido.setSelectionMode(Grid.SelectionMode.SINGLE);
        consumido.addSelectionListener(
                e -> {
                    boolean b=e.getAllSelectedItems().size() == 0 ? activarBotons(false): activarBotons(true);
                }
        );

    }

    private boolean activarBotons(boolean b) {
        eliminar.setEnabled(b);
        modificar.setEnabled(b);
        return true;
    }


}

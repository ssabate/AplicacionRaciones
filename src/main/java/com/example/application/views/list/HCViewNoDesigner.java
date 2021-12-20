package com.example.application.views.list;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.entity.TipoComida;
import com.example.application.data.filter.IngestaFiltro;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A Designer generated component for the hc-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Raciones | Vaadin CRM")
@Route(value = "ingesta")
public class HCViewNoDesigner  extends VerticalLayout{

    //Components visuals
    private ComboBox<Alimento> alimento;    //para escoger un tipo de alimento
    private NumberField racions;            //para indicar las raciones de HCs consumidas
    private IntegerField grams;             //para indicar los gramos de alimento consumidos

    private Grid<Ingesta> consumido= new Grid<Ingesta>(Ingesta.class);  //para mostrar los alimentos consumidos en una ingesta
    private Button afegir;                  //para añadir a la ingesta un alimento más raciones consumidas
    private Button modificar;               //para modificar en la ingesta un alimento más raciones consumidas
    private Button eliminar;                //para eliminar de la ingesta un alimento más raciones consumidas
    private Button resetejar;               //para borrar todos los alimentos consumidos en la ingesta

//    private Button registrar;               //para volver a la selección de fecha y tipo de comida, guardando la ingesta
//    private Button cancelar;               //para volver a la selección de fecha y tipo de comida, eliminando la ingesta

    //Valores recibidos de la clase RCMainView
    private LocalDate fecha;
    private TipoComida tipoComida;

    //Variables útils
    private int grPerRacio;

    //Servei d'accés a dades
    CrmService service;

    /**
     * Creates a new HcView.
     */
    @Autowired
    public HCViewNoDesigner(CrmService service, LocalDate fecha, TipoComida tipoComida) {
        this.service=service;
        this.fecha=fecha;
        this.tipoComida=tipoComida;

        //Primera línia en tipo comida, aliment, racions/grams
        HorizontalLayout hl1=new HorizontalLayout();
        hl1.setWidthFull();
        configureCombos();
        configureCampsNumerics();

        hl1.add(alimento, racions, grams);

        //Segona línia en grid i botons CRUD
        HorizontalLayout hl2=new HorizontalLayout();
        hl2.setSizeFull();

        configureGrid();

        //Botons en vertical
        VerticalLayout vl1=new VerticalLayout();
        vl1.setHeightFull();

        configureBotonsGrid();
        vl1.add(afegir,modificar,eliminar, resetejar);

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

        alimento=new ComboBox<>("Alimento");
        alimento.setPlaceholder("Elige un alimento...");
        alimento.setItems(service.findAllAlimentos());
        alimento.setItemLabelGenerator(t -> t.getNombre());
        alimento.addValueChangeListener(
                e -> {
                    activaCampsNumerics(true);
                    afegir.setEnabled(true);
                    if(consumido.getSelectedItems().size()!=0) modificar.setEnabled(true);
                    grPerRacio=service.findAlimentoByNombre(e.getValue().getNombre()).getGrRacion();
                    grams.setValue(Double.valueOf(racions.getValue()*grPerRacio).intValue());
                }

        );
    }

    private void activaCampsNumerics(boolean b) {
        racions.setEnabled(b);
        grams.setEnabled(b);
    }

    private void configureGrid() {
        consumido.setSizeFull();

//        consumido.addColumn(ingesta -> ingesta.getAlimento().getNombre()).setHeader("Alimento");
//        consumido.setColumns("Alimento", "Raciones");
//        consumido.setColumns(null);
        consumido.removeAllColumns();
        consumido.addColumn(ingesta -> ingesta.getAlimento().getNombre()).setHeader("Alimento");
        //Muestro las raciones con un solo decimal
        consumido.addColumn(ingesta -> String.format("%.2f", ingesta.getRaciones())).setHeader("Raciones");
//        consumido.setColumns("Alimento", "Raciones");
        consumido.getColumns().forEach(col -> col.setAutoWidth(true));
        //Muestro las raciones con un solo decimal
        consumido.getColumns().get(1).setFooter("Total raciones: "+
                String.format("%.2f", service.totalRaciones(fecha, tipoComida)));
//        consumido.setDataProvider(service.new IngestaDataProvider());

        IngestaFiltro gridFilter = new IngestaFiltro(fecha, tipoComida);

        DataProvider<Ingesta, IngestaFiltro> dataProvider =
                DataProvider.fromFilteringCallbacks(
                        query -> {
                            Optional<IngestaFiltro> filter = query.getFilter();
                            return service.fetchIngestas(
                                    query.getOffset(),
                                    query.getLimit(),
                                    filter.map(f -> f.getDate()).orElse(null),
                                    filter.map(f -> f.getComida()).orElse(null)
                            );
                        },
                        query -> {
                            Optional<IngestaFiltro> filter = query.getFilter();
                            return service.getIngestaCount(
                                    filter.map(f -> f.getDate()).orElse(null),
                                    filter.map(f -> f.getComida()).orElse(null)
                            );
                        }
                );
        ConfigurableFilterDataProvider<Ingesta,Void,IngestaFiltro> dp = dataProvider.withConfigurableFilter();
        dp.setFilter(gridFilter);

        //Para actualizar el total de raciones a cada cambio en el grid
        dp.addDataProviderListener(changeEvent -> {
            consumido.getColumns().get(1).setFooter("Total raciones: "+
                    String.format("%.2f", service.totalRaciones(fecha, tipoComida)));
        });


        consumido.setDataProvider(dp);
//    dp.refreshAll();



        //        consumido.setItems(service.findIngestasByDateTipoComida(fecha, tipoComida));
        consumido.setSelectionMode(Grid.SelectionMode.SINGLE);
        consumido.addSelectionListener(
                e -> {
                    if(e.getAllSelectedItems().size() != 0){
                        Ingesta ing=(Ingesta)consumido.getSelectedItems().stream().limit(1).collect(Collectors.toList()).get(0);
                        alimento.setValue(ing.getAlimento());
                        if(alimento.getValue()!=null) modificar.setEnabled(true);
                        eliminar.setEnabled(true);
                    }
                    else{
                        boolean b=e.getAllSelectedItems().size() == 0 ? activarBotons(false): activarBotons(true);
                    }
                }
        );

        //Añado un menú contextual al grid
        GridContextMenu<Ingesta> menu=consumido.addContextMenu();
        menu.addItem("View", event -> {});
        menu.addItem("Edit", event -> {});
        menu.addItem("Delete", event -> {});

    }

    private boolean activarBotons(boolean b) {
        eliminar.setEnabled(b);
        modificar.setEnabled(b);
        return true;
    }

    private void configureBotonsGrid() {
        //Botón de añadir alimento a la ingesta (fusiona el alimento si ya está introducido en la comida)
        afegir = new Button("Añadir");
        afegir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        afegir.setEnabled(false);

        afegir.addClickListener(
                e -> {
                    Ingesta ing=new Ingesta(fecha, tipoComida, alimento.getValue(), racions.getValue().doubleValue());
                    service.insertarIngesta(ing);
//                    consumido.getDataProvider().refreshItem(ing);
                    consumido.getDataProvider().refreshAll();
                    resetejar.setEnabled(true);
                }
        );

        //Botón de modificar alimento. No se activa si no hay alimento seleccionado en el grid. Cuando lo aprieten cambiará los valores
        //actuales por los de los diferentes componentes
        modificar = new Button("Modificar");
        modificar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        modificar.setEnabled(false);
        modificar.addClickListener(
                e -> {
                    Optional<Ingesta> modificado=consumido.getSelectedItems().stream().findFirst();
                    service.modificarIngesta(modificado, alimento.getValue(), racions.getValue());
//                    consumido.getDataProvider().refreshItem(modificado.get());
                    consumido.getDataProvider().refreshAll();
                }
        );

        //Botón de eliminar alimento. No se activa si no hay alimento seleccionado en el grid
        eliminar = new Button("Eliminar");
        eliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        eliminar.setEnabled(false);
        eliminar.addClickListener(
                e -> {
                    Optional<Ingesta> eliminado=consumido.getSelectedItems().stream().findFirst();
                    service.eliminarIngesta(eliminado);
                    consumido.getDataProvider().refreshItem(eliminado.get());
                    consumido.getDataProvider().refreshAll();
                    eliminar.setEnabled(false);
                    modificar.setEnabled(false);
                    resetejar.setEnabled(service.getIngestaCount(fecha, tipoComida)!=0);
                }
        );

        //Botón de eliminar los alimentos del grid, pero mantiéndonos en la misma ingesta (el de cancelar hace lo mismo
        // pero cierra la vista de la ingesta actual). No se activa si no hay alimentos en el grid
        resetejar = new Button("Eliminar todos");
        resetejar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        resetejar.setEnabled(service.getIngestaCount(fecha, tipoComida)!=0);
        resetejar.addClickListener(
                e -> {
                    service.eliminarIngestas(fecha, tipoComida);
                    consumido.getDataProvider().refreshAll();
                    resetejar.setEnabled(false);
                    eliminar.setEnabled(false);
                    modificar.setEnabled(false);

                }
        );
    }



}

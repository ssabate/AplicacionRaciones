package com.example.application.views.list;

import com.example.application.data.entity.Ingesta;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * A Designer generated component for the hc-view template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Tag("hc-view")
@JsModule("./hc-view.ts")
@Route(value = "demonios")
public class HcView extends LitTemplate {

    @Id("tipoComida")
    private ComboBox<String> tipoComida;
    @Id("alimento")
    private ComboBox<String> alimento;
    CrmService service;
    @Id("consumido")
    private Grid<Ingesta> consumido= new Grid<Ingesta>(Ingesta.class);

    /**
     * Creates a new HcView.
     */
    @Autowired
    public HcView(CrmService service) {
        this.service=service;

        configureGrid();

        // You can initialise any data required for the connected UI components here.
        tipoComida.setItems(Arrays.stream(TipoAlimento.names()));
        alimento.setItems(service.findAllAlimentos().stream().map(a -> a.getNombre()));
    }

    private void configureGrid() {
//        consumido.setSizeFull();
//        consumido.addColumn(ingesta -> ingesta.getAlimento().getNombre()).setHeader("Alimento");
//        consumido.setColumns("raciones");
//        consumido.getColumns().forEach(col -> col.setAutoWidth(true));
        consumido.setItems(service.findAllIngestas());
    }

    enum TipoAlimento{
        DESAYUNO, COMIDA, MERIENDA, CENA;

        public static String[] names() {
            TipoAlimento[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) {
                names[i] = states[i].name();
            }

            return names;
        }
    }
}

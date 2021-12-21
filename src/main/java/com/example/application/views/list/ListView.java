package com.example.application.views.list;

import com.example.application.data.entity.Alimento;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Ingesta;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;

@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Contacts | Vaadin CRM")
@Route(value = "foods")
public class ListView extends VerticalLayout {
    Grid<Alimento> grid = new Grid<>(Alimento.class);
    TextField filterText = new TextField();
    CrmService service;

    @Autowired
    public ListView(CrmService service) {
        this.service=service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        add(getToolbar(), grid);

        setSizeFull();
    }
    void addFood() {
        grid.asSingleSelect().clear();
        editFood(new Alimento());
    }

    public void editFood(Alimento food) {
//        if (contact == null) {
//            closeEditor();
//        } else {
//            form.setContact(contact);
//            form.setVisible(true);
//            addClassName("editing");
//        }
    }
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filtrar por nombre...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addFoodButton = new Button("Añadir alimento");
        addFoodButton.addClickListener(click -> addFood());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addFoodButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("nombre", "grRacion");
//        grid.addColumn(food -> food.getNombre()).setHeader("Nombre");
//        grid.addColumn(food -> food.getGrRacion()).setHeader("Gramos x ración");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
//        grid.setItems(service.findAllFoods(filterText.getValue()));
        grid.setItems(service.findAllAlimentos(filterText.getValue()));


        //Añado un menú contextual al grid
        GridContextMenu<Alimento> menu=grid.addContextMenu();
        menu.addItem("View", event -> {});
        menu.addItem("Edit", event -> {});
        menu.addItem("Delete", event -> {});

    }
    private void updateList() {
        grid.setItems(service.findAllAlimentos(filterText.getValue()));
    }
}

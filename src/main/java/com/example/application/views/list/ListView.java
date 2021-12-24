package com.example.application.views.list;

import com.example.application.data.entity.Alimento;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

//@Route(value="", layout = MainLayout.class)


@Component
@Scope("prototype")
@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Alimentos | Vaadin CRM")
@Route(value = "foods")
@PermitAll
public class ListView extends VerticalLayout {
    Grid<Alimento> grid = new Grid<>(Alimento.class);
    TextField filterText = new TextField();
    AlimentoForm form;
    CrmService service;

    @Autowired
    public ListView(CrmService service) {
        this.service=service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
//        configureForm();

        add(getToolbar(), configureForm());
//        add(getToolbar(), grid);
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event ->
                editFood(event.getValue()));
//        setSizeFull();
    }

    private FlexLayout configureForm() {
        form = new AlimentoForm();
        form.setWidth("25em");
        form.addListener(AlimentoForm.SaveEvent.class, this::saveAlimento);
        form.addListener(AlimentoForm.DeleteEvent.class, this::deleteAlimento);
        form.addListener(AlimentoForm.CloseEvent.class, e -> closeEditor());

//        Div content = new Div(grid, form);
//        content.addClassName("content");
//        content.setSizeFull();
        FlexLayout content = new FlexLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexShrink(0, form);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();
        return content;
    }

    private void deleteAlimento(AlimentoForm.DeleteEvent evt) {
        try {
            service.eliminarAlimento(evt.getAlimento());
            updateList();
            closeEditor();
        }catch(Exception e){
        // When creating a notification using the `show` static method,
// the duration is 5-sec by default.
        Notification notification = Notification.show("¡Atención! Alimento usado en ingesta. No puede ser borrado");
    }

    }

    private void saveAlimento(AlimentoForm.SaveEvent evt) {
        service.insertarAlimento(evt.getAlimento());
        updateList();
        closeEditor();
    }

    void addFood() {
        grid.asSingleSelect().clear();
        editFood(new Alimento());
    }

    public void editFood(Alimento food) {
        if (food == null) {
            closeEditor();
        } else {
            form.setAlimento(food);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setAlimento(null);
        form.setVisible(false);
        removeClassName("editing");
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
//        grid.setItems(service.findAllAlimentos(filterText.getValue()));


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

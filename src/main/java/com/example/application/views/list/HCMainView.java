package com.example.application.views.list;

import com.example.application.data.entity.TipoComida;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
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
@Route(value = "app-layout-basic")
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


        //Primera línia en tipo comida, data i botons
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

//        add(menuBox(), hl1, hl2);
        add( hl1, hl2);
        setSizeFull();
    }

//    private Div menuBox() {
//        Div comp = new Div();
//        MenuBar menuBar = new MenuBar();
//        Text selected = new Text("");
//        ComponentEventListener<ClickEvent<MenuItem>> listener = e -> selected.setText(e.getSource().getText());
//        Div message = new Div(new Text("Clicked item: "), selected);
//
//        menuBar.addItem("View", listener);
//        menuBar.addItem("Edit", listener);
//
//        Image image = new Image("images/3_lines.png", "dummy image");
//        image.setWidth(5, Unit.PERCENTAGE);
//        image.setHeight(10, Unit.PERCENTAGE);
//        menuBar.addItem(image, listener);
//
//        MenuItem share = menuBar.addItem("Share");
//        SubMenu shareSubMenu = share.getSubMenu();
//        MenuItem onSocialMedia = shareSubMenu.addItem("On social media");
//        SubMenu socialMediaSubMenu = onSocialMedia.getSubMenu();
//        socialMediaSubMenu.addItem("Facebook", listener);
//        socialMediaSubMenu.addItem("Twitter", listener);
//        socialMediaSubMenu.addItem("Instagram", listener);
//        shareSubMenu.addItem("By email", listener);
//        shareSubMenu.addItem("Get Link", listener);
//
//        MenuItem move = menuBar.addItem("Move");
//        SubMenu moveSubMenu = move.getSubMenu();
//        moveSubMenu.addItem("To folder", listener);
//        moveSubMenu.addItem("To trash", listener);
//
//        menuBar.addItem("Duplicate", listener);
//
//        comp.add(menuBar, message);
//        return comp;
//    }

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
        Image image = new Image("images/3_lines.png", "dummy image");
        image.setWidth(5, Unit.PERCENTAGE);
        image.setHeight(10, Unit.PERCENTAGE);
        Icon ic=new Icon(VaadinIcon.ACCORDION_MENU);
        nueva = new Button("Añadir", ic );
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
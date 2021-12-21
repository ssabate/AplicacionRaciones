package com.example.application.views.list;

import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
// tag::snippet[]
// De https://vaadin.com/docs/v14/ds/components/app-layout
public class AppLayoutBasic extends AppLayout {


    //Servei d'accés a dades
    CrmService service;

    public AppLayoutBasic(CrmService service) {

        this.service=service;

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Raciones HC");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);

    }
    // end::snippet[]

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
//                createTab(VaadinIcon.DASHBOARD, "Ingestas", HCMainView.class),
                createTab(VaadinIcon.DASHBOARD, "Ingestas", new HCMainView(this.service)),
                createTab(VaadinIcon.GLASS, "Comidas", new ListView(this.service)),
                createTab(VaadinIcon.PACKAGE, "Alimentos"),
                createTab(VaadinIcon.RECORDS, "Informes"),
                createTab(VaadinIcon.LIST, "Acerca de...")
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Component... view) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));link.addFocusListener(
                e -> {
//                    e.getSource().setRoute(view.length>0?view[0]:null);
                    this.setContent(view.length>0?view[0]:null);
                }

        );
        // Demo has no routes
        // link.setRoute(viewClass.java);
        //if(route.length>0) link.setRoute(route[0]); --> createTab(VaadinIcon viewIcon, String viewName, Class... route)
//        this.setContent(view.length>0?view[0]:null);

        link.setTabIndex(-1);

        return new Tab(link);
    }
    // tag::snippet[]
}
// end::snippet[]

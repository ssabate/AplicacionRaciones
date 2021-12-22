package com.example.application.views.list;

import com.example.application.data.entity.Alimento;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.shared.Registration;

public class AlimentoForm extends FormLayout {
    private Alimento food;

    TextField nombre = new TextField("Nombre");
    TextField grRacion = new TextField("Grs x ración");
    Binder<Alimento> binder = new BeanValidationBinder<>(Alimento.class);

    Button save = new Button("Guardar");
    Button delete = new Button("Borrar");
    Button close = new Button("Cancelar");

    public AlimentoForm() {
        addClassName("contact-form");
        binder.forField(grRacion)
                .withConverter(
                        new StringToIntegerConverter("No es un número correcto"))
                .bind(Alimento::getGrRacion,
                        Alimento::setGrRacion)
                ;
        binder.bindInstanceFields(this);

        add(nombre,
                grRacion,
                createButtonsLayout());
    }


    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, food)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));


        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setAlimento(Alimento food) {
        this.food = food;
        binder.readBean(food);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(food);
            fireEvent(new SaveEvent(this, food));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class AlimentoFormEvent extends ComponentEvent<AlimentoForm> {
        private Alimento food;

        protected AlimentoFormEvent(AlimentoForm source, Alimento food) {
            super(source, false);
            this.food = food;
        }

        public Alimento getAlimento() {
            return food;
        }
    }

    public static class SaveEvent extends AlimentoFormEvent {
        SaveEvent(AlimentoForm source, Alimento food) {
            super(source, food);
        }
    }

    public static class DeleteEvent extends AlimentoFormEvent {
        DeleteEvent(AlimentoForm source, Alimento food) {
            super(source, food);
        }

    }

    public static class CloseEvent extends AlimentoFormEvent {
        CloseEvent(AlimentoForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
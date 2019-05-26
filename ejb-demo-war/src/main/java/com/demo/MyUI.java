package com.demo;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;

import com.model.Patient;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@LocalBean
@Stateful
public class MyUI extends UI {

    private Grid grid;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        grid = new Grid();
        grid.addColumn("id",Integer.class);
        grid.addColumn("name",String.class);
        grid.addColumn("age",Integer.class);

        final VerticalLayout layout = new VerticalLayout();
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener( e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));


//            List<Patient> patientList = localTestClient.getPatients();
//            grid.addRow(patientList.get(0).getId(),
//                        patientList.get(0).getName(),
//                        patientList.get(0).getAge());

        });
        
        layout.addComponents(name, button,grid);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

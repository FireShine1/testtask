package ru.fireshine.testtask;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ru.fireshine.testtask.web.layouts.DoctorLayout;
import ru.fireshine.testtask.web.layouts.PatientLayout;
import ru.fireshine.testtask.web.layouts.RecipeLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MainUI extends UI {
	
	private final TabSheet tabs = new TabSheet();
    private final DoctorLayout doctorLayout = new DoctorLayout(this);
    private final PatientLayout patientLayout = new PatientLayout(this);
    private final RecipeLayout recipeLayout = new RecipeLayout(this);
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        tabs.addTab(doctorLayout, "Врачи");
        tabs.addTab(patientLayout, "Пациенты");
        tabs.addTab(recipeLayout, "Рецепты");
        
        layout.addComponent(tabs);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

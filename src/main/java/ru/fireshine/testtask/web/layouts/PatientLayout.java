package ru.fireshine.testtask.web.layouts;

import java.util.Iterator;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.fireshine.testtask.MainUI;
import ru.fireshine.testtask.logic.dao.ManagementService;
import ru.fireshine.testtask.logic.dao.PatientDao;
import ru.fireshine.testtask.logic.model.Patient;
import ru.fireshine.testtask.web.forms.PatientForm;

public class PatientLayout extends VerticalLayout {

	private PatientDao patientService = ManagementService.getInstance().getPatDao();
	private Grid<Patient> grid = new Grid<>(Patient.class);
	private Window window = new Window();
	private PatientForm patientForm = new PatientForm(this, window);
	
	public PatientLayout(MainUI mainUI) {
		
		grid.setColumns("surName", "firstName", "patronymic", "phoneNumber");
        grid.getColumn("surName").setCaption("Фамилия");
        grid.getColumn("firstName").setCaption("Имя").setSortable(false);
        grid.getColumn("patronymic").setCaption("Отчество").setSortable(false);
        grid.getColumn("phoneNumber").setCaption("Телефон").setSortable(false);
        grid.setSizeFull();
        
        VerticalLayout windowLayout = new VerticalLayout();
        windowLayout.addComponent(patientForm);
        window.setContent(windowLayout);
        window.center();
        window.setModal(true);
        window.setResizable(false);
        
        Button addBtn = new Button("Добавить");
        addBtn.addClickListener(e -> {
        	grid.asSingleSelect().clear();
            patientForm.setPatient(Patient.samplePatient());
            mainUI.addWindow(window);
        });
        
        Button updBtn = new Button("Изменить");
        updBtn.addClickListener(e -> {
        	Iterator<Patient> it = grid.getSelectedItems().iterator();
        	patientForm.setPatient(it.next());
        	mainUI.addWindow(window);
        });
        
        Button delBtn = new Button("Удалить");
        delBtn.addClickListener(e -> {
        	grid.getSelectedItems().forEach((Patient patient) -> {
        		patientService.delete(patient);
        	});
        	updateList();
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(addBtn, updBtn, delBtn);
        
        addComponents(grid, toolbar);
        
        updateList();
        
        delBtn.setEnabled(false);
        updBtn.setEnabled(false);
        
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                updBtn.setEnabled(false);
                delBtn.setEnabled(false);
            } else {
                updBtn.setEnabled(true);
                delBtn.setEnabled(true);
            }
        });
	}
	
	public void updateList() {
        List<Patient> patients = patientService.getAll();
        grid.setItems(patients);
    }
	
}

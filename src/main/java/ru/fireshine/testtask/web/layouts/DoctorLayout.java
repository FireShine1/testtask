package ru.fireshine.testtask.web.layouts;

import java.util.Iterator;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import ru.fireshine.testtask.MainUI;
import ru.fireshine.testtask.logic.dao.DoctorDao;
import ru.fireshine.testtask.logic.dao.ManagementService;
import ru.fireshine.testtask.logic.model.Doctor;
import ru.fireshine.testtask.logic.model.Statistics;
import ru.fireshine.testtask.web.forms.DoctorForm;

public class DoctorLayout extends VerticalLayout {

	private DoctorDao doctorService = ManagementService.getInstance().getDocDao();
	private Grid<Doctor> grid = new Grid<>(Doctor.class);
	private Grid<Statistics> statGrid = new Grid<>(Statistics.class);
	private Window window = new Window();
	private Window statWindow = new Window("Статистика");
	private DoctorForm doctorForm = new DoctorForm(this, window);
	
	public DoctorLayout(MainUI mainUI) {
		
		grid.setColumns("surName", "firstName", "patronymic", "specialization");
        grid.getColumn("surName").setCaption("Фамилия");
        grid.getColumn("firstName").setCaption("Имя").setSortable(false);
        grid.getColumn("patronymic").setCaption("Отчество").setSortable(false);
        grid.getColumn("specialization").setCaption("Специализация");
        grid.setSizeFull();
        
        statGrid.setColumns("doctor", "number");
        statGrid.getColumn("doctor").setCaption("Врач");
        statGrid.getColumn("number").setCaption("Количество рецептов");
        statGrid.setSizeUndefined();
        
        VerticalLayout windowLayout = new VerticalLayout();
        windowLayout.addComponent(doctorForm);
        window.setContent(windowLayout);
        window.center();
        window.setModal(true);
        window.setResizable(false);
        
        VerticalLayout statLayout = new VerticalLayout();
        Button closeBtn = new Button("Закрыть");
        closeBtn.addClickListener(e -> {
        	statWindow.close();
        });
        statLayout.addComponents(statGrid, closeBtn);
        statWindow.setContent(statLayout);
        statWindow.center();
        statWindow.setModal(true);
        statWindow.setResizable(false);
        
        Button addBtn = new Button("Добавить");
        addBtn.addClickListener(e -> {
        	grid.asSingleSelect().clear();
            doctorForm.setDoctor(new Doctor());
            mainUI.addWindow(window);
        });
        
        Button updBtn = new Button("Изменить");
        updBtn.addClickListener(e -> {
        	Iterator<Doctor> it = grid.getSelectedItems().iterator();
        	doctorForm.setDoctor(it.next());
        	mainUI.addWindow(window);
        });
        
        Button delBtn = new Button("Удалить");
        delBtn.addClickListener(e -> {
        	grid.getSelectedItems().forEach((Doctor doctor) -> {
        		doctorService.delete(doctor);
        	});
        	updateList();
        });
        
        Button statBtn = new Button("Показать статистику");
        statBtn.addClickListener(e -> {
        	statGrid.setItems(doctorService.getStatistics());
        	mainUI.addWindow(statWindow);
        	
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(addBtn, updBtn, delBtn, statBtn);
        
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
        List<Doctor> doctors = doctorService.getAll();
        grid.setItems(doctors);
    }
	
}

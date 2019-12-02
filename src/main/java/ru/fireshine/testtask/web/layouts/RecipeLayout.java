package ru.fireshine.testtask.web.layouts;

import java.util.Iterator;
import java.util.List;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ru.fireshine.testtask.MainUI;
import ru.fireshine.testtask.logic.dao.ManagementService;
import ru.fireshine.testtask.logic.dao.RecipeDao;
import ru.fireshine.testtask.logic.model.Recipe;
import ru.fireshine.testtask.logic.model.RecipePriority;
import ru.fireshine.testtask.web.forms.RecipeForm;

public class RecipeLayout extends VerticalLayout {

	private RecipeDao recipeService = ManagementService.getInstance().getRecDao();
	private Grid<Recipe> grid = new Grid<>(Recipe.class);
	private Window window = new Window();
	private RecipeForm recipeForm = new RecipeForm(this, window);
	private TextField filterPatient = new TextField();
	private NativeSelect<RecipePriority> filterPriority = new NativeSelect<>("Приоритет");
	private TextField filterDescription = new TextField();
	
	public RecipeLayout(MainUI mainUI) {
		
		grid.addColumn(Recipe -> Recipe.getDate().toString())
			.setCaption("Дата создания").setId("createDate");
        grid.getColumn("description").setCaption("Описание").setSortable(false);
        grid.getColumn("patient").setCaption("Пациент");
        grid.getColumn("doctor").setCaption("Врач");
        grid.getColumn("validity").setCaption("Срок действия (в днях)");
        grid.getColumn("priority").setCaption("Приоритет");
        grid.setColumns("description", "patient", "doctor", "createDate", "validity", "priority");
        grid.setSizeFull();
        
        VerticalLayout windowLayout = new VerticalLayout();
        windowLayout.addComponent(recipeForm);
        window.setContent(windowLayout);
        window.center();
        window.setModal(true);
        window.setResizable(false);
        
        Button addBtn = new Button("Добавить");
        addBtn.addClickListener(e -> {
        	grid.asSingleSelect().clear();
        	recipeForm.setPatient(new Recipe());
            mainUI.addWindow(window);
        });
        
        Button updBtn = new Button("Изменить");
        updBtn.addClickListener(e -> {
        	Iterator<Recipe> it = grid.getSelectedItems().iterator();
        	recipeForm.setPatient(it.next());
        	mainUI.addWindow(window);
        });
        
        Button delBtn = new Button("Удалить");
        delBtn.addClickListener(e -> {
        	grid.getSelectedItems().forEach((Recipe recipe) -> {
        		recipeService.delete(recipe);
        	});
        	updateList();
        });
        
        Button findBtn = new Button("Применить");
        findBtn.addClickListener(e -> updateList());
        
        filterPatient.setPlaceholder("ФИО пациента...");
        Button clearFilterPatientBtn = new Button(FontAwesome.TIMES);
        clearFilterPatientBtn.setDescription("Очистить фильтр");
        clearFilterPatientBtn.addClickListener(e -> filterPatient.clear());

        CssLayout patientFiltering = new CssLayout();
        patientFiltering.addComponents(filterPatient, clearFilterPatientBtn);
        patientFiltering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        filterDescription.setPlaceholder("Описание...");
        Button clearFilterDescriptionBtn = new Button(FontAwesome.TIMES);
        clearFilterDescriptionBtn.setDescription("Очистить фильтр");
        clearFilterDescriptionBtn.addClickListener(e -> filterDescription.clear());

        CssLayout descriptionFiltering = new CssLayout();
        descriptionFiltering.addComponents(filterDescription, clearFilterDescriptionBtn);
        descriptionFiltering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        filterPriority.setItems(RecipePriority.values());
        CssLayout priorityFiltering = new CssLayout();
        priorityFiltering.addComponents(filterPriority);
        priorityFiltering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        
        HorizontalLayout filtering = 
        		new HorizontalLayout(patientFiltering, priorityFiltering,
        							descriptionFiltering, findBtn);

        HorizontalLayout toolbar = new HorizontalLayout(addBtn, updBtn, delBtn);
        
        addComponents(filtering, grid, toolbar);
        
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
		String priority = null;
		if (filterPriority.getValue() != null) {
			priority = filterPriority.getValue().toString();
		}
        List<Recipe> recipes = recipeService.getAll(filterPatient.getValue(),
        											priority,
        											filterDescription.getValue());
        grid.setItems(recipes);
    }
	
}

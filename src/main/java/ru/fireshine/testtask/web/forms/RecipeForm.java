package ru.fireshine.testtask.web.forms;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ru.fireshine.testtask.logic.dao.DoctorDao;
import ru.fireshine.testtask.logic.dao.ManagementService;
import ru.fireshine.testtask.logic.dao.PatientDao;
import ru.fireshine.testtask.logic.dao.RecipeDao;
import ru.fireshine.testtask.logic.model.Doctor;
import ru.fireshine.testtask.logic.model.Patient;
import ru.fireshine.testtask.logic.model.Recipe;
import ru.fireshine.testtask.logic.model.RecipePriority;
import ru.fireshine.testtask.web.layouts.RecipeLayout;

public class RecipeForm extends FormLayout {

	private TextArea description = new TextArea("Описание");
    private ComboBox<Patient> patient = new ComboBox<>("Пациент");
    private ComboBox<Doctor> doctor = new ComboBox<>("Врач");
    private DateField createDate = new DateField("Дата создания");
    private TextField validity = new TextField("Срок действия");
    private NativeSelect<RecipePriority> priority = new NativeSelect<>("Приоритет");
    private Button save = new Button("ОК");
    private Button cancel = new Button("Отменить");
    
    private RecipeDao recipeService = ManagementService.getInstance().getRecDao();
    private PatientDao patientService = ManagementService.getInstance().getPatDao();
    private DoctorDao doctorService = ManagementService.getInstance().getDocDao();
    private Recipe recipe;
    private RecipeLayout layout;
    private Window window;
    private Binder<Recipe> binder = new Binder<>(Recipe.class);
    
    private LocalDate getDate(Recipe recipe) {
        return recipe.getDate().toLocalDate();
    }

    private void setDate(Recipe recipe, LocalDate localDate) {
        recipe.setDate(Date.valueOf(localDate));
    }
    
    private RecipePriority getPriorityForBind(Recipe recipe) {
        return recipe.getPriority();
    }

    private void setPriorityForBind(Recipe recipe, RecipePriority priority) {
        recipe.setPriority(priority);
    }
    
    public RecipeForm(RecipeLayout layout, Window window) {
    	this.layout = layout;
    	this.window = window;
    	
    	patient.setItems(patientService.getAll());
    	patient.setRequiredIndicatorVisible(true);
        patient.setEmptySelectionAllowed(false);
        
        doctor.setItems(doctorService.getAll());
        doctor.setRequiredIndicatorVisible(true);
        doctor.setEmptySelectionAllowed(false);
        
        priority.setItems(RecipePriority.values());
        priority.setEmptySelectionAllowed(false);
        
    	setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(description, patient, doctor, createDate, validity, priority, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.forField(description)
        		.withValidator(new StringLengthValidator(
        				"Введите описание", 1, null))
        		.bind(Recipe::getDescription, Recipe::setDescription);
        binder.forField(createDate)
        		.withValidator(localDate -> localDate.compareTo(LocalDate.now()) <= 0,
        				"Дата должна быть не позже текущей")
        		.bind(this::getDate, this::setDate);
        binder.forField(validity)
        		.withValidator(new RegexpValidator(
        				"Введите срок действия в днях", "\\d+"))
        		.withConverter(new StringToIntegerConverter(
        				"Поле должно содержать только цифры"))
        		.bind(Recipe::getValidity, Recipe::setValidity);
        binder.forField(priority)
        		.bind(this::getPriorityForBind, this::setPriorityForBind);

        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> window.close());
    }
    
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        binder.setBean(recipe);
        Optional<Patient> recPatient = patientService.getById(recipe.getPatientId());
        if (recPatient.isPresent()) {
        	patient.setValue(recPatient.get());
        }
        Optional<Doctor> recDoctor = doctorService.getById(recipe.getDoctorId());
        if (recDoctor.isPresent()) {
        	doctor.setValue(recDoctor.get());
        }

        setVisible(true);
        description.selectAll();
    }
    
    private void save() {
    	if ((recipe.getId() != null) && (binder.validate().isOk())) {
    		if (patient.getValue() == null) {
    			Notification.show("Выберите пациента");
    		} else {
    			if (doctor.getValue() == null) {
    				Notification.show("Выберите врача");
    			} else {
    				recipe.setPatient(patient.getValue().toString());
    				recipe.setPatientId(patient.getValue().getId());
    	    		recipe.setDoctor(doctor.getValue().toString());
    	    		recipe.setDoctorId(doctor.getValue().getId());
    	    		recipeService.update(recipe);
    	    		layout.updateList();
    	            window.close();
    			}
    		}
    	} else {
    		if (binder.validate().isOk()) {
    			if (patient.getValue() == null) {
        			Notification.show("Выберите пациента");
        		} else {
        			if (doctor.getValue() == null) {
        				Notification.show("Выберите врача");
        			} else {
        				recipe.setPatient(patient.getValue().toString());
        				recipe.setPatientId(patient.getValue().getId());
                		recipe.setDoctor(doctor.getValue().toString());
                		recipe.setDoctorId(doctor.getValue().getId());
            			recipeService.insert(recipe);
            			layout.updateList();
            	        window.close();
        			}
        		}
    		}
    	}
    }
	
}

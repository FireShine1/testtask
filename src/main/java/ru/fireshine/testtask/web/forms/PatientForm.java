package ru.fireshine.testtask.web.forms;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ru.fireshine.testtask.logic.dao.ManagementService;
import ru.fireshine.testtask.logic.dao.PatientDao;
import ru.fireshine.testtask.logic.model.Patient;
import ru.fireshine.testtask.web.layouts.PatientLayout;

public class PatientForm extends FormLayout {

	private TextField firstName = new TextField("Имя");
    private TextField surName = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField phoneNumber = new TextField("Телефон");
    private Button save = new Button("ОК");
    private Button cancel = new Button("Отменить");
    
    private PatientDao patientService = ManagementService.getInstance().getPatDao();
    private Patient patient;
    private PatientLayout layout;
    private Window window;
    private Binder<Patient> binder = new Binder<>(Patient.class);
    
    public PatientForm(PatientLayout layout, Window window) {
    	this.layout = layout;
    	this.window = window;
    	
    	setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(firstName, patronymic, surName, phoneNumber, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.forField(phoneNumber)
        		.withValidator(new RegexpValidator(
        				"Введите номер телефона", "(\\+?)(\\d{11})"))
        		.bind(Patient::getPhoneNumber, Patient::setPhoneNumber);      
        binder.forField(firstName)
        		.withValidator(new RegexpValidator(
        				"Введите имя. Имя должно начинаться с заглавной буквы",
        				"([А-Я]{1})([а-я]*)"))
        		.bind(Patient::getFirstName, Patient::setFirstName);       
        binder.forField(surName)
				.withValidator(new RegexpValidator(
						"Введите фамилию. Фамилия должна начинаться с заглавной буквы",
						"([А-Я]{1})([а-я]*)"))
				.bind(Patient::getSurName, Patient::setSurName);      
        binder.forField(patronymic)
				.withValidator(new RegexpValidator(
						"Введите отчество. Отчество должно начинаться с заглавной буквы",
						"([А-Я]{1})([а-я]*)"))
				.bind(Patient::getPatronymic, Patient::setPatronymic);

        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> window.close());
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
        binder.setBean(patient);

        setVisible(true);
        firstName.selectAll();
    }
    
    private void save() {
    	if ((patient.getId() != null) && (binder.validate().isOk())) {
    		patientService.update(patient);
    		layout.updateList();
            window.close();
    	} else {
    		if (binder.validate().isOk()) {
    			patientService.insert(patient);
    			layout.updateList();
    	        window.close();
    		}
    	}
    }
    
}

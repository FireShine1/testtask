package ru.fireshine.testtask.web.forms;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ru.fireshine.testtask.logic.dao.DoctorDao;
import ru.fireshine.testtask.logic.dao.ManagementService;
import ru.fireshine.testtask.logic.model.Doctor;
import ru.fireshine.testtask.web.layouts.DoctorLayout;

public class DoctorForm extends FormLayout {

	private TextField firstName = new TextField("Имя");
    private TextField surName = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField specialization = new TextField("Специализация");
    private Button save = new Button("ОК");
    private Button cancel = new Button("Отменить");
    
    private DoctorDao doctorService = ManagementService.getInstance().getDocDao();
    private Doctor doctor;
    private DoctorLayout layout;
    private Window window;
    private Binder<Doctor> binder = new Binder<>(Doctor.class);
    
    public DoctorForm(DoctorLayout layout, Window window) {
    	this.layout = layout;
    	this.window = window;
    	
    	setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(firstName, patronymic, surName, specialization, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        binder.forField(firstName)
				.withValidator(new RegexpValidator(
						"Введите имя. Имя должно начинаться с заглавной буквы",
						"([А-Я]{1})([а-я]*)"))
				.bind(Doctor::getFirstName, Doctor::setFirstName);
        binder.forField(surName)
				.withValidator(new RegexpValidator(
						"Введите фамилию. Фамилия должна начинаться с заглавной буквы",
						"([А-Я]{1})([а-я]*)"))
				.bind(Doctor::getSurName, Doctor::setSurName);
        binder.forField(patronymic)
				.withValidator(new RegexpValidator(
						"Введите отчество. Отчество должно начинаться с заглавной буквы",
						"([А-Я]{1})([а-я]*)"))
				.bind(Doctor::getPatronymic, Doctor::setPatronymic);
        binder.forField(specialization)
        		.withValidator(new StringLengthValidator(
        				"Введите специализацию", 1, null))
        		.bind(Doctor::getSpecialization, Doctor::setSpecialization);

        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> window.close());
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        binder.setBean(doctor);

        setVisible(true);
        firstName.selectAll();
    }
    
    private void save() {
    	if ((doctor.getId() != null) && (binder.validate().isOk())) {
    		doctorService.update(doctor);
    		layout.updateList();
            window.close();
    	} else {
    		if (binder.validate().isOk()) {
    			doctorService.insert(doctor);
    			layout.updateList();
    	        window.close();
    		}
    	}
    }
    
}

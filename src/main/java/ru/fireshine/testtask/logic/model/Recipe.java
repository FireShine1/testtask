package ru.fireshine.testtask.logic.model;

import java.sql.Date;
import java.time.LocalDate;

public class Recipe {

	private Long id;
	private String description;
	private String patient;
	private String doctor;
	private Date createDate;
	private Integer validity;
	private RecipePriority priority;
	private Long doctorId;
	private Long patientId;
	
	public final static Recipe sampleRecipe() {
		Recipe recipe = new Recipe();
		recipe.setDescription("Парацетамол");
		recipe.setDate(Date.valueOf(LocalDate.now()));
		recipe.setPriority(RecipePriority.Normal);
		recipe.setValidity(30);
		recipe.setPatientId(0L);
		recipe.setDoctorId(0L);
		return recipe;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the patient
	 */
	public String getPatient() {
		return patient;
	}
	
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(String patient) {
		this.patient = patient;
	}
	
	/**
	 * @return the doctor
	 */
	public String getDoctor() {
		return doctor;
	}
	
	/**
	 * @param doctor the doctor to set
	 */
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return createDate;
	}
	
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.createDate = date;
	}
	
	/**
	 * @return the validity
	 */
	public Integer getValidity() {
		return validity;
	}
	
	/**
	 * @param validity the validity to set
	 */
	public void setValidity(Integer validity) {
		this.validity = validity;
	}
	
	/**
	 * @return the priority
	 */
	public RecipePriority getPriority() {
		return priority;
	}
	
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(RecipePriority priority) {
		this.priority = priority;
	}

	/**
	 * @return the doctorId
	 */
	public Long getDoctorId() {
		return doctorId;
	}

	/**
	 * @param doctorId the doctorId to set
	 */
	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	/**
	 * @return the patientId
	 */
	public Long getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	@Override
	public String toString() {
		return "Recipe {id=" + id + ", description=" + description + ", patient=" + patient + ", doctor=" + doctor
				+ ", date=" + createDate + ", time=" + validity + ", priority=" + priority + "}";
	}
	
}

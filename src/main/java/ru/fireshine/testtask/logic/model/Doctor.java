package ru.fireshine.testtask.logic.model;

public class Doctor {
	
	private Long id;
	private String firstName;
	private String surName;
	private String patronymic;
	private String specialization;
	
	public final static Doctor sampleDoctor() {
		Doctor doctor = new Doctor();
		doctor.setFirstName("Иван");
		doctor.setPatronymic("Иванович");
		doctor.setSurName("Иванов");
		doctor.setSpecialization("Терапевт");
		return doctor;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the surName
	 */
	public String getSurName() {
		return surName;
	}
	
	/**
	 * @param surName the surName to set
	 */
	public void setSurName(String surName) {
		this.surName = surName;
	}
	
	/**
	 * @return the patronymic
	 */
	public String getPatronymic() {
		return patronymic;
	}
	
	/**
	 * @param patronymic the patronymic to set
	 */
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	
	/**
	 * @return the specialization
	 */
	public String getSpecialization() {
		return specialization;
	}
	
	/**
	 * @param specializtion the specialization to set
	 */
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	@Override
	public String toString() {
		return surName + " " + firstName + " " + patronymic;
	}
	
}

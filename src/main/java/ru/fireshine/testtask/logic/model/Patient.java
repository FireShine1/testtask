package ru.fireshine.testtask.logic.model;

public class Patient {

	private Long id;
	private String firstName;
	private String surName;
	private String patronymic;
	private String phoneNumber;
	
	public final static Patient samplePatient() {
		Patient patient = new Patient();
		patient.setFirstName("Иван");
		patient.setPatronymic("Иванович");
		patient.setSurName("Иванов");
		patient.setPhoneNumber("+77777777777");
		return patient;
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
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return surName + " " + firstName + " " + patronymic;
	}
	
}

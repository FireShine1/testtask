package ru.fireshine.testtask.logic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.fireshine.testtask.logic.model.Patient;

public class PatientDao implements IDao<Patient> {
	
	private static final String SQL_FIND_ALL = "SELECT * FROM Patients";
	private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO Patients "
			+ "(firstName, surName, patronymic, phoneNumber) "
			+ "VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE Patients SET "
			+ "firstName = ?, surName = ?, patronymic = ?, phoneNumber = ? "
			+ "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM Patients WHERE id = ?";
	
	public synchronized List<Patient> getAll() {
		ArrayList<Patient> result = new ArrayList<Patient>();
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
			ResultSet rs = ps.executeQuery()
		) {
			while(rs.next()) {
				Patient patient = new Patient();
				patient.setId(rs.getLong("id"));
				patient.setFirstName(rs.getString("firstName"));
				patient.setSurName(rs.getString("surName"));
				patient.setPatronymic(rs.getString("patronymic"));
				patient.setPhoneNumber(rs.getString("phoneNumber"));
				result.add(patient);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public synchronized Optional<Patient> getById(Long id) {
		Patient patient = null;
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID)
		) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						patient = new Patient();
						patient.setId(rs.getLong("id"));
						patient.setFirstName(rs.getString("firstName"));
						patient.setSurName(rs.getString("surName"));
						patient.setPatronymic(rs.getString("patronymic"));
						patient.setPhoneNumber(rs.getString("phoneNumber"));
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(patient);
	}
	
	public synchronized void insert(Patient patient) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_INSERT)
		) {
			ps.setString(1, patient.getFirstName());
			ps.setString(2, patient.getSurName());
			ps.setString(3, patient.getPatronymic());
			ps.setString(4, patient.getPhoneNumber());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void update(Patient patient) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_UPDATE)
		) {
			ps.setString(1, patient.getFirstName());
			ps.setString(2, patient.getSurName());
			ps.setString(3, patient.getPatronymic());
			ps.setString(4, patient.getPhoneNumber());
			ps.setLong(5, patient.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void delete(Patient patient) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_DELETE)
		) {
			ps.setLong(1, patient.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

package ru.fireshine.testtask.logic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.fireshine.testtask.logic.model.Doctor;
import ru.fireshine.testtask.logic.model.Statistics;

public class DoctorDao implements IDao<Doctor> {
	
	private static final String SQL_FIND_ALL = "SELECT * FROM Doctors";
	private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO Doctors "
			+ "(firstName, surName, patronymic, specialization) "
			+ "VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE Doctors SET "
			+ "firstName = ?, surName = ?, patronymic = ?, specialization = ? "
			+ "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM Doctors WHERE id = ?";
    private static final String SQL_STATISTICS = 
    		"SELECT Doctors.id, COUNT(Recipes.doctorId) AS number "
    		+ "FROM Doctors LEFT JOIN Recipes "
    		+ "ON Doctors.id = Recipes.doctorId "
    		+ "GROUP BY (Doctors.id)";
	
	public synchronized List<Doctor> getAll() {
		ArrayList<Doctor> result = new ArrayList<Doctor>();
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
			ResultSet rs = ps.executeQuery()
		) {
			while(rs.next()) {
				Doctor doctor = new Doctor();
				doctor.setId(rs.getLong("id"));
				doctor.setFirstName(rs.getString("firstName"));
				doctor.setSurName(rs.getString("surName"));
				doctor.setPatronymic(rs.getString("patronymic"));
				doctor.setSpecialization(rs.getString("specialization"));
				result.add(doctor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public synchronized Optional<Doctor> getById(Long id) {
		Doctor doctor = null;
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID)
		) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
					doctor = new Doctor();
					doctor.setId(rs.getLong("id"));
					doctor.setFirstName(rs.getString("firstName"));
					doctor.setSurName(rs.getString("surName"));
					doctor.setPatronymic(rs.getString("patronymic"));
					doctor.setSpecialization(rs.getString("specialization"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(doctor);
	}
	
	/**
	 * 
	 * @return table representation of statistics
	 * 		   about number of recipes created by each doctor
	 */
	public synchronized List<Statistics> getStatistics() {
		ArrayList<Statistics> result = new ArrayList<Statistics>();
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_STATISTICS);
			ResultSet rs = ps.executeQuery()
		) {
			while(rs.next()) {
				Statistics statistics = new Statistics();
				statistics.setDoctor(getById(rs.getLong("id")).get().toString());
				statistics.setNumber(rs.getLong("number"));
				result.add(statistics);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public synchronized void insert(Doctor doctor) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_INSERT)
		) {
			ps.setString(1, doctor.getFirstName());
			ps.setString(2, doctor.getSurName());
			ps.setString(3, doctor.getPatronymic());
			ps.setString(4, doctor.getSpecialization());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void update(Doctor doctor) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_UPDATE)
		) {
			ps.setString(1, doctor.getFirstName());
			ps.setString(2, doctor.getSurName());
			ps.setString(3, doctor.getPatronymic());
			ps.setString(4, doctor.getSpecialization());
			ps.setLong(5, doctor.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void delete(Doctor doctor) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_DELETE)
		) {
			ps.setLong(1, doctor.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

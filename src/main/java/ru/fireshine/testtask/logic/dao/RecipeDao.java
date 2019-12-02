package ru.fireshine.testtask.logic.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.fireshine.testtask.logic.model.Recipe;

public class RecipeDao implements IDao<Recipe> {
	
	private static final String SQL_FIND_ALL = "SELECT * FROM Recipes";
	private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " WHERE id = ?";
	private static final String SQL_INSERT = "INSERT INTO Recipes "
			+ "(description, patient, doctor, createDate, validity, priority, doctorId, patientId) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE Recipes SET "
			+ "description = ?, patient = ?, doctor = ?, createDate = ?, "
			+ "validity = ?, priority = ?, doctorId = ?, patientId = ? "
			+ "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM Recipes WHERE id = ?";
    
    public synchronized List<Recipe> getAll() {
    	return getAll(null, null, null);
    }
	
    /**
	 * Finds all Recipe's that match given filter.
	 *
	 * @param patient
	 *            filter that returned objects should match given patient
	 *            or null/empty string if all objects should be returned.
	 * 
	 * @param priority
	 *            filter that returned objects should match given priority
	 *            or null/empty string if all objects should be returned.
	 *            
	 * @param description
	 *            filter that returned objects should match given description
	 *            or null/empty string if all objects should be returned.
	 * 
	 * @return list of Recipe objects
	 */
	public synchronized List<Recipe> getAll(String patient, String priority,
											String description) {
		ArrayList<Recipe> result = new ArrayList<Recipe>();
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
			ResultSet rs = ps.executeQuery()
		) {
			while(rs.next()) {
				boolean passesPatient = (patient == null || patient.isEmpty())
						|| (rs.getString("patient").toLowerCase()
							.contains(patient.toLowerCase()));
				boolean passesPriority = (priority == null || priority.isEmpty())
						|| (rs.getString("priority").toLowerCase()
							.contains(priority.toLowerCase()));
				boolean passesDescription = (description == null || description.isEmpty())
						|| (rs.getString("description").toLowerCase()
							.contains(description.toLowerCase()));
				if (passesPatient && passesPriority && passesDescription) {
					Recipe recipe = new Recipe();
					recipe.setId(rs.getLong("id"));
					recipe.setDescription(rs.getString("description"));
					recipe.setPatient(rs.getString("patient"));
					recipe.setDoctor(rs.getString("doctor"));
					recipe.setDate(rs.getDate("createDate"));
					recipe.setValidity(rs.getInt("validity"));
					recipe.setPriority(rs.getString("priority"));
					recipe.setDoctorId(rs.getLong("doctorId"));
					recipe.setPatientId(rs.getLong("patientId"));
					result.add(recipe);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public synchronized Optional<Recipe> getById(Long id) {
		Recipe recipe = null;
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID)
		) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
					recipe = new Recipe();
					recipe.setId(rs.getLong("id"));
					recipe.setDescription(rs.getString("description"));
					recipe.setPatient(rs.getString("patient"));
					recipe.setDoctor(rs.getString("doctor"));
					recipe.setDate(rs.getDate("createDate"));
					recipe.setValidity(rs.getInt("validity"));
					recipe.setPriority(rs.getString("priority"));
					recipe.setDoctorId(rs.getLong("doctorId"));
					recipe.setPatientId(rs.getLong("patientId"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(recipe);
	}
	
	public synchronized void insert(Recipe recipe) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_INSERT)
		) {
			ps.setString(1, recipe.getDescription());
			ps.setString(2, recipe.getPatient());
			ps.setString(3, recipe.getDoctor());
			ps.setDate(4, new Date(recipe.getDate().getTime()));
			ps.setInt(5, recipe.getValidity());
			ps.setString(6, recipe.getPriority());
			ps.setLong(7, recipe.getDoctorId());
			ps.setLong(8, recipe.getPatientId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void update(Recipe recipe) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_UPDATE)
		) {
			ps.setString(1, recipe.getDescription());
			ps.setString(2, recipe.getPatient());
			ps.setString(3, recipe.getDoctor());
			ps.setDate(4, new Date(recipe.getDate().getTime()));
			ps.setInt(5, recipe.getValidity());
			ps.setString(6, recipe.getPriority());
			ps.setLong(7, recipe.getDoctorId());
			ps.setLong(8, recipe.getPatientId());
			ps.setLong(9, recipe.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void delete(Recipe recipe) {
		try (Connection con = ManagementService.getConnection();
			PreparedStatement ps = con.prepareStatement(SQL_DELETE)
		) {
			ps.setLong(1, recipe.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

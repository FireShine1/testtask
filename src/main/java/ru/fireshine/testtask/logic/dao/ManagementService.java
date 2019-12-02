package ru.fireshine.testtask.logic.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

public class ManagementService {
	
	private static ManagementService instance;
	private static final String url = "jdbc:hsqldb:file:./src/main/resources/clinic";
	private static final String login = "SA";
	private static final String password = "";
	private DoctorDao docDao = new DoctorDao();
	private PatientDao patDao = new PatientDao();
	private RecipeDao recDao = new RecipeDao();
	
	private ManagementService() {
	}
	
	public static ManagementService getInstance() {
		if (instance == null) {
			instance = new ManagementService();
		}
		return instance;
	}
	
	/**
	 * creates necessary tables if they not exist on start of the app
	 */
	public static void start() {
		try (Connection con = getConnection()) {
			SqlFile sf = new SqlFile(new File("./src/main/resources/script.sql"));
			sf.setConnection(con);
	        sf.execute();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SqlToolError e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the docDao
	 */
	public DoctorDao getDocDao() {
		return this.docDao;
	}

	/**
	 * @param docDao the docDao to set
	 */
	public void setDocDao(DoctorDao docDao) {
		this.docDao = docDao;
	}

	/**
	 * @return the patDao
	 */
	public PatientDao getPatDao() {
		return this.patDao;
	}

	/**
	 * @param patDao the patDao to set
	 */
	public void setPatDao(PatientDao patDao) {
		this.patDao = patDao;
	}

	/**
	 * @return the recDao
	 */
	public RecipeDao getRecDao() {
		return this.recDao;
	}

	/**
	 * @param recDao the recDao to set
	 */
	public void setRecDao(RecipeDao recDao) {
		this.recDao = recDao;
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, login, password);
	}

}

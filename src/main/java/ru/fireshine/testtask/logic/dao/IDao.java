package ru.fireshine.testtask.logic.dao;

import java.util.List;
import java.util.Optional;

interface IDao<T> {
	
	/**
	 * 
	 * @return list of all available <T> objects
	 */
	List<T> getAll();
	
	/**
	 * 
	 * @param id
	 * @return T if exists, Optional.empty() otherwise
	 */
	Optional<T> getById(Long id);
	
	/**
	 * inserts t in database
	 * @param t
	 */
	void insert(T t);
	
	/**
	 * updates t in database
	 * @param t
	 */
	void update(T t);
	
	/**
	 * delete t from database
	 * @param t
	 */
	void delete(T t);

}

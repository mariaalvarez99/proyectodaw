package com.dawes.servicio;

import java.util.List;
import java.util.Optional;

import com.dawes.modelo.RolVO;

public interface RolServicio {
	
	Optional<RolVO> findByNombre(String nombre);
	

	<S extends RolVO> S save(S entity);

	List<RolVO> findAll();

	Optional<RolVO> findById(Integer id);

	void delete(RolVO entity);

}
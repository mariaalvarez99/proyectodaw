package com.dawes.servicio;

import java.util.List;
import java.util.Optional;

import com.dawes.modelo.ComentarioVO;

public interface ComentarioServicio {

	<S extends ComentarioVO> S save(S entity);

	List<ComentarioVO> findAll();

	Optional<ComentarioVO> findById(Integer id);

	void delete(ComentarioVO entity);

}
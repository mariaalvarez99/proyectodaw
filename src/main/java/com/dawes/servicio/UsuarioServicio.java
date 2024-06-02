package com.dawes.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.dawes.modelo.UsuarioVO;

public interface UsuarioServicio {

	Optional<UsuarioVO> findByDni(String dni);
	
	<S extends UsuarioVO> S save(S entity);

	List<UsuarioVO> findAll(Sort sort);

	List<UsuarioVO> findAll();

	Optional<UsuarioVO> findById(Integer id);

	boolean existsById(Integer id);

	long count();

	void deleteById(Integer id);

	UsuarioVO getById(Integer id);

	void delete(UsuarioVO entity);

	void deleteAll();

}
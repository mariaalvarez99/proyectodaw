package com.dawes.servicio;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.dawes.modelo.CategoriaVO;

public interface CategoriaServicio {

	Optional<CategoriaVO> findByEstrellas(int estrellas);

	<S extends CategoriaVO> S save(S entity);

	List<CategoriaVO> findAll(Sort sort);

	List<CategoriaVO> findAll();

	Optional<CategoriaVO> findById(Integer id);

	boolean existsById(Integer id);

	long count();

	void deleteById(Integer id);

	CategoriaVO getById(Integer id);

	void delete(CategoriaVO entity);

	void deleteAll();

}
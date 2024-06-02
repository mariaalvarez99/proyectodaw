package com.dawes.servicio;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.dawes.modelo.HotelVO;

public interface HotelServicio {

	Optional<HotelVO> findByNombre(String nombre);
	
	<S extends HotelVO> S save(S entity);

	List<HotelVO> findAll(Sort sort);

	List<HotelVO> findAll();
	
	Optional<HotelVO> findById(Integer id);

	boolean existsById(Integer id);

	long count();

	void deleteById(Integer id);

	HotelVO getById(Integer id);

	void delete(HotelVO entity);

	void deleteAll();

}
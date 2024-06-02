package com.dawes.servicio;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.HotelVO;

public interface HabitacionServicio {

	Optional<HabitacionVO> findByTipoAndHotel(String tipo, HotelVO hotel);
	
	Optional<List<HabitacionVO>> findByHotel(HotelVO hotel);
	
	
	<S extends HabitacionVO> S save(S entity);

	List<HabitacionVO> findAll(Sort sort);

	List<HabitacionVO> findAll();
	
	Optional<HabitacionVO> findById(Integer id);

	boolean existsById(Integer id);

	long count();

	void deleteById(Integer id);

	HabitacionVO getById(Integer id);

	void delete(HabitacionVO entity);

	void deleteAll();

}
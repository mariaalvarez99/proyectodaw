package com.dawes.servicioImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.HotelVO;
import com.dawes.repositorio.HabitacionRepository;
import com.dawes.servicio.HabitacionServicio;

@Service
public class HabitacionServicioImpl implements HabitacionServicio {

	@Autowired
	HabitacionRepository hr;

	public Optional<HabitacionVO> findByTipoAndHotel(String tipo, HotelVO hotel) {
		return hr.findByTipoAndHotel(tipo, hotel);
	}

	public Optional<List<HabitacionVO>> findByHotel(HotelVO hotel) {
		return hr.findByHotel(hotel);
	}

	@Override
	public <S extends HabitacionVO> S save(S entity) {
		return hr.save(entity);
	}

	@Override
	public List<HabitacionVO> findAll(Sort sort) {
		return hr.findAll(sort);
	}

	@Override
	public List<HabitacionVO> findAll() {
		return hr.findAll();
	}

	public Optional<HabitacionVO> findById(Integer id) {
		return hr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return hr.existsById(id);
	}

	@Override
	public long count() {
		return hr.count();
	}

	@Override
	public void deleteById(Integer id) {
		hr.deleteById(id);
	}

	@Override
	public HabitacionVO getById(Integer id) {
		return hr.getById(id);
	}

	@Override
	public void delete(HabitacionVO entity) {
		hr.delete(entity);
	}

	@Override
	public void deleteAll() {
		hr.deleteAll();
	}
	
	
}

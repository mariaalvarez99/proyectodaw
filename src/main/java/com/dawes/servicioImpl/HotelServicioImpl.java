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

import com.dawes.modelo.HotelVO;
import com.dawes.repositorio.HotelRepository;
import com.dawes.servicio.HotelServicio;

@Service
public class HotelServicioImpl implements HotelServicio {

	@Autowired
	HotelRepository hr;

	public Optional<HotelVO> findByNombre(String nombre) {
		return hr.findByNombre(nombre);
	}

	@Override
	public <S extends HotelVO> S save(S entity) {
		return hr.save(entity);
	}

	@Override
	public List<HotelVO> findAll(Sort sort) {
		return hr.findAll(sort);
	}

	@Override
	public List<HotelVO> findAll() {
		return hr.findAll();
	}

	public Optional<HotelVO> findById(Integer id) {
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
	public HotelVO getById(Integer id) {
		return hr.getById(id);
	}

	@Override
	public void delete(HotelVO entity) {
		hr.delete(entity);
	}

	@Override
	public void deleteAll() {
		hr.deleteAll();
	}
	
	
}

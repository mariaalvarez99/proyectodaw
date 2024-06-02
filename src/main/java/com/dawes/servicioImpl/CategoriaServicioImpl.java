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

import com.dawes.modelo.CategoriaVO;
import com.dawes.repositorio.CategoriaRepository;
import com.dawes.servicio.CategoriaServicio;

@Service
public class CategoriaServicioImpl implements CategoriaServicio {

	@Autowired
	CategoriaRepository cr;

	@Override
	public Optional<CategoriaVO> findByEstrellas(int estrellas) {
		return cr.findByEstrellas(estrellas);
	}

	@Override
	public <S extends CategoriaVO> S save(S entity) {
		return cr.save(entity);
	}

	@Override
	public List<CategoriaVO> findAll(Sort sort) {
		return cr.findAll(sort);
	}

	@Override
	public List<CategoriaVO> findAll() {
		return cr.findAll();
	}

	@Override
	public Optional<CategoriaVO> findById(Integer id) {
		return cr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return cr.existsById(id);
	}

	@Override
	public long count() {
		return cr.count();
	}

	@Override
	public void deleteById(Integer id) {
		cr.deleteById(id);
	}

	@Override
	public CategoriaVO getById(Integer id) {
		return cr.getById(id);
	}

	@Override
	public void delete(CategoriaVO entity) {
		cr.delete(entity);
	}

	@Override
	public void deleteAll() {
		cr.deleteAll();
	}
	
	
}

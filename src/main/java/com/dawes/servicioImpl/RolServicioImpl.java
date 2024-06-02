package com.dawes.servicioImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.modelo.RolVO;
import com.dawes.repositorio.RolRepository;
import com.dawes.servicio.RolServicio;

@Service
public class RolServicioImpl implements RolServicio {

	@Autowired
	RolRepository rr;

	
	
	public Optional<RolVO> findByNombre(String nombre) {
		return rr.findByNombre(nombre);
	}

	@Override
	public <S extends RolVO> S save(S entity) {
		return rr.save(entity);
	}

	@Override
	public List<RolVO> findAll() {
		return rr.findAll();
	}

	@Override
	public Optional<RolVO> findById(Integer id) {
		return rr.findById(id);
	}

	@Override
	public void delete(RolVO entity) {
		rr.delete(entity);
	}
	
	
}

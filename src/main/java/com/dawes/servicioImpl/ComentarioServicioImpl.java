package com.dawes.servicioImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dawes.modelo.ComentarioVO;
import com.dawes.repositorio.ComentarioRepository;
import com.dawes.servicio.ComentarioServicio;

@Service
public class ComentarioServicioImpl implements ComentarioServicio {

	@Autowired
	ComentarioRepository cr;

	@Override
	public <S extends ComentarioVO> S save(S entity) {
		return cr.save(entity);
	}

	@Override
	public List<ComentarioVO> findAll() {
		return cr.findAll();
	}

	@Override
	public Optional<ComentarioVO> findById(Integer id) {
		return cr.findById(id);
	}

	@Override
	public void delete(ComentarioVO entity) {
		cr.delete(entity);
	}
	
	
}

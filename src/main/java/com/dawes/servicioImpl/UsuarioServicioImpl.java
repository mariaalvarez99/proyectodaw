package com.dawes.servicioImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawes.modelo.UsuarioVO;
import com.dawes.repositorio.UsuarioRepository;
import com.dawes.servicio.UsuarioServicio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	@Autowired
	UsuarioRepository ur;

	
	
	public Optional<UsuarioVO> findByDni(String dni) {
		return ur.findByDni(dni);
	}

	@Override
	public <S extends UsuarioVO> S save(S entity) {
		return ur.save(entity);
	}

	@Override
	public List<UsuarioVO> findAll(Sort sort) {
		return ur.findAll(sort);
	}

	@Override
	public List<UsuarioVO> findAll() {
		return ur.findAll();
	}

	@Override
	public Optional<UsuarioVO> findById(Integer id) {
		return ur.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return ur.existsById(id);
	}

	@Override
	public long count() {
		return ur.count();
	}

	@Override
	public void deleteById(Integer id) {
		ur.deleteById(id);
	}

	@Override
	public UsuarioVO getById(Integer id) {
		return ur.getById(id);
	}

	@Override
	public void delete(UsuarioVO entity) {
		ur.delete(entity);
	}

	@Override
	public void deleteAll() {
		ur.deleteAll();
	}
	
	
}

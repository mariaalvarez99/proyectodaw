package com.dawes.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.UsuarioVO;

@Repository
public interface UsuarioRepository  extends JpaRepository<UsuarioVO, Integer>  {

	Optional<UsuarioVO> findByDni(String dni);
}

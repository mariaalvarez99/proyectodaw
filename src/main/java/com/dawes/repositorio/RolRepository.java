package com.dawes.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.RolVO;

@Repository
public interface RolRepository extends JpaRepository<RolVO, Integer> {

	Optional<RolVO> findByNombre(String nombre);
}

package com.dawes.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.CategoriaVO;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaVO, Integer> {

	Optional<CategoriaVO> findByEstrellas(int estrellas);
}

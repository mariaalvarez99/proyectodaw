package com.dawes.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.HotelVO;

@Repository
public interface HotelRepository extends JpaRepository<HotelVO, Integer> {

	Optional<HotelVO> findByNombre(String nombre);
}

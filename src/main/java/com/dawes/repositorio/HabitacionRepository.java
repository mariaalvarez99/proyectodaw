package com.dawes.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.HotelVO;

@Repository
public interface HabitacionRepository extends JpaRepository<HabitacionVO, Integer> {

	Optional<HabitacionVO> findByTipoAndHotel(String tipo, HotelVO hotel);
	
	Optional<List<HabitacionVO>> findByHotel(HotelVO hotel);
}

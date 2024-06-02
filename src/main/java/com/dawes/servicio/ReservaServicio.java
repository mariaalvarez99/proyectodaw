package com.dawes.servicio;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.ReservaVO;

public interface ReservaServicio {
	
	public boolean fechasdisponibles(LocalDate finicio, LocalDate ffin, int idhabitacion, int nhabitaciones);
	
	public List<Object[]> getlistames(YearMonth anomes, int idhabitacion);
	

	Optional<ReservaVO> gestionReservaHabitacion(ReservaVO reserva);
	
	Optional<List<ReservaVO>> listadoReservasFecha(LocalDate fecha, HabitacionVO habitacion);
	
	Optional<List<ReservaVO>> listadoReservasEntreFechas(LocalDate fecha1, LocalDate fecha2, HabitacionVO habitacion);
	
	List<Object[]> obtenerTotalHabitacionesReservadas(LocalDate fechaInicio, LocalDate fechaFin);
	
	List<Object[]> obtenerHabitacionesReservadasHabitacion(LocalDate fechaInicio, LocalDate fechaFin,
			int idhabitacion);
	
	
	<S extends ReservaVO> S save(S entity);

	List<ReservaVO> findAll(Sort sort);

	List<ReservaVO> findAll();
	
	Optional<ReservaVO> findById(Integer id);

	boolean existsById(Integer id);

	long count();

	void deleteById(Integer id);

	ReservaVO getById(Integer id);

	void delete(ReservaVO entity);

	void deleteAll();

}
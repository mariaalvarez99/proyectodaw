package com.dawes.repositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.ReservaVO;

@Repository
public interface ReservaRepository extends JpaRepository<ReservaVO, Integer> {

	@Query("SELECT r FROM ReservaVO r WHERE (?1 BETWEEN fechainicio AND fechafin) AND habitacion=?2")
	Optional<List<ReservaVO>> listadoReservasFecha(LocalDate fecha, HabitacionVO habitacion);
	
	@Query("SELECT r FROM ReservaVO r WHERE ((fechainicio BETWEEN ?1 AND ?2) OR (fechafin BETWEEN ?1 AND ?2)) AND habitacion=?3")
	Optional<List<ReservaVO>> listadoReservasEntreFechas(LocalDate fecha1, LocalDate fecha2, HabitacionVO habitacion);
	
	
	 @Query(nativeQuery = true, value = 
			 	"SELECT fecha, SUM(numhabitaciones) AS ocupadas, h.idhotel, h.idhabitacion, h.cantidad "
			 	+ "FROM ( "
			 	+ "    SELECT DATE_ADD(r.fechainicio, INTERVAL n.numday DAY) AS fecha, r.numhabitaciones,  r.idhabitacion "
			 	+ "    FROM reservas r "
			 	+ "    JOIN "
			 	+ "        (SELECT t0.n + t1.n * 10 + t2.n * 100 AS numday "
			 	+ "         FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS t0 "
			 	+ "         CROSS JOIN (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS t1 "
			 	+ "         CROSS JOIN (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS t2 "
			 	+ "        ) n "
			 	+ "    ON DATE_ADD(r.fechainicio, INTERVAL n.numday DAY) <= r.fechafin "
			 	+ "    WHERE DATE_ADD(r.fechainicio, INTERVAL n.numday DAY) BETWEEN ?1 AND ?2 "
			 	+ ") AS reservas_en_rango "
			 	+ "JOIN habitaciones h ON reservas_en_rango.idhabitacion = h.idhabitacion " 
			 	+ "GROUP BY fecha, h.idhotel, h.idhabitacion, h.cantidad")
	 List<Object[]> obtenerTotalHabitacionesReservadas(LocalDate fechaInicio, LocalDate fechaFin);
	 
	 
	 
	 @Query(nativeQuery = true, value = 
			 	"SELECT todas_las_fechas.fecha,  COALESCE(ocupadas, 0) AS ocupadas, h.cantidad  "
			 	+ "FROM ( "
			 	+ "    SELECT DATE_ADD(?1, INTERVAL seq.seq DAY) AS fecha "
			 	+ "    FROM ( "
			 	+ "        SELECT (HUNDREDS.seq * 100 + TENS.seq * 10 + ONES.seq) AS seq "
			 	+ "        FROM (SELECT 0 AS seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS ONES "
			 	+ "        CROSS JOIN (SELECT 0 AS seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS TENS "
			 	+ "        CROSS JOIN (SELECT 0 AS seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS HUNDREDS "
			 	+ "    ) AS seq "
			 	+ "    WHERE DATE_ADD(?1, INTERVAL seq DAY) BETWEEN ?1 AND ?2 "
			 	+ ") AS todas_las_fechas "
			 	+ "LEFT JOIN ( "
			 	+ "    SELECT DATE_ADD(fechainicio, INTERVAL seq.seq DAY) AS fecha, SUM(r.numhabitaciones) AS ocupadas  "
			 	+ "    FROM reservas r  "
			 	+ "    JOIN ( "
			 	+ "        SELECT (HUNDREDS.seq * 100 + TENS.seq * 10 + ONES.seq) AS seq "
			 	+ "        FROM (SELECT 0 AS seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS ONES "
			 	+ "        CROSS JOIN (SELECT 0 AS seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS TENS "
			 	+ "        CROSS JOIN (SELECT 0 AS seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS HUNDREDS "
			 	+ "    ) AS seq  "
			 	+ "    ON DATE_ADD(fechainicio, INTERVAL seq.seq DAY) <= fechafin  "
			 	+ "    WHERE DATE_ADD(fechainicio, INTERVAL seq.seq DAY) BETWEEN ?1 AND ?2 "
			 	+ "    GROUP BY fecha "
			 	+ ") AS reservas_en_rango  "
			 	+ "    ON todas_las_fechas.fecha = reservas_en_rango.fecha "
			 	+ "LEFT JOIN habitaciones AS h ON h.idhabitacion = ?3 "
			 	+ "ORDER BY todas_las_fechas.fecha")
	 List<Object[]> obtenerHabitacionesReservadasHabitacion(LocalDate fechaInicio, LocalDate fechaFin, int idhabitacion);
	 
	 
	 
}

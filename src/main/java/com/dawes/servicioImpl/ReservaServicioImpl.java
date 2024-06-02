package com.dawes.servicioImpl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.ReservaVO;
import com.dawes.repositorio.ReservaRepository;
import com.dawes.servicio.ReservaServicio;

import jakarta.persistence.PersistenceException;

@Service
public class ReservaServicioImpl implements ReservaServicio {

	@Autowired
	ReservaRepository rhr;

	
	public boolean fechasdisponibles(LocalDate finicio, LocalDate ffin, int idhabitacion, int nhabitaciones) {
		boolean disponible = true;
		
		List<Object[]> listadiaocupacion = rhr.obtenerHabitacionesReservadasHabitacion(finicio, ffin, idhabitacion);
		for (Object[] datosdia : listadiaocupacion) {
			LocalDate fecha = LocalDate.parse(datosdia[0].toString());
			int ocupadas = Integer.parseInt(datosdia[1].toString());
			int disponibles = Integer.parseInt(datosdia[2].toString());
			
			if (disponibles - ocupadas - nhabitaciones < 0) {
				disponible = false;
				break;
			}
		}
		
		
		return disponible;
	}
	
	public List<Object[]> getlistames(YearMonth anomes, int idhabitacion) {
		
		LocalDate finicio = anomes.atDay(1);
        LocalDate ffin = anomes.atEndOfMonth();
        
        List<Object[]> listadiaocupacion = rhr.obtenerHabitacionesReservadasHabitacion(finicio, ffin, idhabitacion);
        List<Object[]> listanueva = new ArrayList<>();
        
        //añadir columna colores
        for (Object[] fila : listadiaocupacion) {
        	String fecha = fila[0].toString();
//        	fila[0] = fecha.split("-")[2];
//        	System.err.println(fecha);
            int ocupadas = Integer.parseInt(fila[1].toString());
            int cantidad = Integer.parseInt(fila[2].toString());
            String nivel;
            
            if (ocupadas < 0.5 * cantidad) 
                nivel = "verde";
            else if (ocupadas < 0.8 * cantidad) 
                nivel = "amarillo";
            else if (ocupadas < cantidad) 
                nivel = "naranja";
            else 
                nivel = "rojo";
            
            // nuevo array columna 
            Object[] nuevaFila = new Object[fila.length + 1];
            System.arraycopy(fila, 0, nuevaFila, 0, fila.length);
            nuevaFila[fila.length] = nivel;

            listadiaocupacion.set(listadiaocupacion.indexOf(fila), nuevaFila);
        }
        
        
        List<Object[]> listames = new ArrayList<>();
        List<Object[]> semana = new ArrayList<>();
        
        for (int i = 0; i < listadiaocupacion.size(); i++) {
            Object[] filadia = listadiaocupacion.get(i);
            
            if (i == 0) { //comprobar que día de la semana es el primero
            	LocalDate fecha = LocalDate.parse(filadia[0].toString());
            	int diaDeLaSemana = fecha.getDayOfWeek().getValue() % 7; // 0 domingo
            	
            	if (diaDeLaSemana == 0)
            		diaDeLaSemana = 7;
            	for (int j = 0; j < diaDeLaSemana - 1; j++) 
	            	semana.add(new Object[] {"", "", "", ""}); // Agregar objetos vacíos
            	
            }
            
            semana.add(filadia);
            
            if (i == listadiaocupacion.size() - 1) {
            	LocalDate fecha = LocalDate.parse(filadia[0].toString());
            	int diaDeLaSemana = fecha.getDayOfWeek().getValue() % 7; // 0 domingo
            	
            	if (diaDeLaSemana != 0)
	            	for (int j = diaDeLaSemana; j < 7; j++) 
	            		semana.add(new Object[] {"", "", "", ""}); // Agregar objetos vacíos
            }
            
            
            if (semana.size() == 7)
            {
//            	listames.add(semana);
            	listames.add(semana.toArray(new Object[0])); 
            	semana = new ArrayList<>();
            }
            
        }
        
        return listames;
	}
	
	public Optional<ReservaVO> gestionReservaHabitacion(ReservaVO reserva) {
		
		int habitacionesdisponibles = reserva.getHabitacion().getCantidad();
		int habitacionesocupadas = 0;
		for(ReservaVO rh: rhr.listadoReservasEntreFechas(reserva.getFechainicio(),reserva.getFechafin(),reserva.getHabitacion()).get()) {
			habitacionesocupadas += rh.getNumhabitaciones();
		}
		
		if (habitacionesdisponibles - habitacionesocupadas - reserva.getNumhabitaciones() >= 0) {
			try {
				rhr.save(reserva);
				return Optional.of(reserva);
			}catch (PersistenceException p) {
				System.out.println("Error al guardar reserva "+p.getMessage());
				return Optional.empty();
			}
		}else {
			System.out.println("No hay suficientes habitaciones disponibles");
			return Optional.empty();
		}
//		return Optional.empty();
	}

//	public int getHabitacionesOcupadas(String tipo) {
//		int habitacionesocupadas = 0;
//		for(ReservaHabitacionVO rh: rhr.listadoReservasEntreFechas(reserva.getFechainicio(),reserva.getFechafin()).get()) {
//			habitacionesocupadas += rh.getNumhabitaciones();
//		}
//	}
	
	public Optional<List<ReservaVO>> listadoReservasFecha(LocalDate fecha, HabitacionVO habitacion) {
		return rhr.listadoReservasFecha(fecha, habitacion);
	}

	public Optional<List<ReservaVO>> listadoReservasEntreFechas(LocalDate fecha1, LocalDate fecha2, HabitacionVO habitacion) {
		return rhr.listadoReservasEntreFechas(fecha1, fecha2, habitacion);
	}

	public List<Object[]> obtenerTotalHabitacionesReservadas(LocalDate fechaInicio, LocalDate fechaFin) {
		return rhr.obtenerTotalHabitacionesReservadas(fechaInicio, fechaFin);
	}
	
	public List<Object[]> obtenerHabitacionesReservadasHabitacion(LocalDate fechaInicio, LocalDate fechaFin,
			int idhabitacion) {
		return rhr.obtenerHabitacionesReservadasHabitacion(fechaInicio, fechaFin, idhabitacion);
	}

	
	
	@Override
	public <S extends ReservaVO> S save(S entity) {
		return rhr.save(entity);
	}

	@Override
	public List<ReservaVO> findAll(Sort sort) {
		return rhr.findAll(sort);
	}

	@Override
	public List<ReservaVO> findAll() {
		return rhr.findAll();
	}

	public Optional<ReservaVO> findById(Integer id) {
		return rhr.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return rhr.existsById(id);
	}

	@Override
	public long count() {
		return rhr.count();
	}

	@Override
	public void deleteById(Integer id) {
		rhr.deleteById(id);
	}

	@Override
	public ReservaVO getById(Integer id) {
		return rhr.getById(id);
	}

	@Override
	public void delete(ReservaVO entity) {
		rhr.delete(entity);
	}

	@Override
	public void deleteAll() {
		rhr.deleteAll();
	}
	
	
}

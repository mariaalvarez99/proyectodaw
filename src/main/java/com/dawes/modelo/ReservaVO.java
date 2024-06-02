package com.dawes.modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="reservas")
public class ReservaVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idreserva;
	private LocalDate fechareserva;
	private LocalDate fechainicio;
	private LocalDate fechafin;
	private int numhabitaciones;
	
	@ManyToOne
	@JoinColumn(name = "idhabitacion")
	private HabitacionVO habitacion;

	@ManyToOne
	@JoinColumn(name = "idusuario")
	private UsuarioVO usuario;
	
	private double precio;
	
	
}

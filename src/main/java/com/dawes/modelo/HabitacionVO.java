package com.dawes.modelo;


import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "habitaciones",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tipo", "idhotel"})})
public class HabitacionVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idhabitacion;
	private String tipo;
	private String descripcion;
	
	
	private int cantidad;
	private double precio;
	private String img;
	@ManyToOne
	@JoinColumn(name = "idhotel")
	private HotelVO hotel;
}

package com.dawes.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="comentarios")
public class ComentarioVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idcomentario;
	
	private String nombre;
	private String email;
	private String texto;
	
	private LocalDate fecha;
	private LocalTime hora;
}

package com.dawes.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="hoteles")
public class HotelVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idhotel;
	@Column(unique = true)
	private String nombre;
	@ManyToOne
	@JoinColumn(name="idcategoria")
	private CategoriaVO categoria;
	
	private String img;
	private String descripcion;
	private String direccion;
	
	
}

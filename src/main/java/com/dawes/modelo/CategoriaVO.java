package com.dawes.modelo;

import jakarta.persistence.Column;
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
@Table(name="categorias")
public class CategoriaVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idcategoria;
	@Column(unique = true)
	private int estrellas;
	private String descripcion;
}

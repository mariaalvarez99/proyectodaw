package com.dawes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dawes.modelo.CategoriaVO;
import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.HotelVO;
import com.dawes.servicio.CategoriaServicio;
import com.dawes.servicio.HabitacionServicio;
import com.dawes.servicio.HotelServicio;
import com.dawes.servicio.ReservaServicio;
import com.dawes.servicio.UsuarioServicio;

@SpringBootTest
class ProyectotestApplicationTests {

	@Autowired
	CategoriaServicio categorias;
	
	@Autowired
	UsuarioServicio usuario;
	
	@Autowired
	HabitacionServicio habitaciones;
	
	@Autowired
	HotelServicio hoteles;
	
	@Autowired
	ReservaServicio reserva;
	
	
	
	@Test
	void test01_insertar() {

	}
	


}

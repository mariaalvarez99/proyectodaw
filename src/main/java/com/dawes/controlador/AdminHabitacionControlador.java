package com.dawes.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.ComentarioVO;
import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.HotelVO;
import com.dawes.servicio.HabitacionServicio;
import com.dawes.servicio.HotelServicio;

@Controller
@RequestMapping("/administracion/habitaciones")
public class AdminHabitacionControlador {

	@Autowired
	private HabitacionServicio habitacionservicio;
	
	@Autowired
	private HotelServicio hotelservicio;
	
	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de habitaciones
	String habitaciones(Model model) {

		model.addAttribute("habitaciones", habitacionservicio.findAll());
		model.addAttribute("hoteles",hotelservicio.findAll());
		
		return "admin/adminhabitaciones";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un habitacion por su ID
	public String obtenerHabitacion(Model model, @PathVariable Integer id) {

		model.addAttribute("habitacion", habitacionservicio.findById(id).get());
		model.addAttribute("hoteles", hotelservicio.findAll());

		return "admin/adminhabitacion";
	}
	
	@GetMapping("/nuevo")  
	public String habitacionNuevo(Model model) {

		model.addAttribute("nuevoHabitacion",new HabitacionVO() );
		model.addAttribute("hoteles", hotelservicio.findAll());
		
		return "admin/adminhabitacionnuevo";  
	}
	
	@PostMapping("/add") //maneja solicitud POST para agregar habitacion
	public String addHabitacion(Model model, @ModelAttribute("nuevoHabitacion") HabitacionVO habitacion, BindingResult bindingResult) {

		habitacionservicio.save(habitacion);
		
		return "redirect:/administracion/habitaciones";
		
	}
	
	@GetMapping("/editar/{id}")  
	public String habitacioneditar(@PathVariable Integer id, Model model) {

		model.addAttribute("habitacion", habitacionservicio.findById(id).get());
		model.addAttribute("habitacionaEditar",new HabitacionVO() );
		model.addAttribute("hoteles", hotelservicio.findAll());
		
		return "admin/adminhabitacioneditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un habitacion existente
	public String editarHabitacion(Model model, @PathVariable Integer id, 
			@RequestParam(required = false) int idhabitacion,
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String descripcion,
            @RequestParam(required = false) int cantidad,
            @RequestParam(required = false) double precio,
            @RequestParam(required = false) String imagen,
            @RequestParam(required = false) int idhotel) {

		HabitacionVO habitacionAEditar = habitacionservicio.findById(id).get();
		habitacionAEditar.setTipo(tipo);
		habitacionAEditar.setDescripcion(descripcion);
		habitacionAEditar.setCantidad(cantidad);
		habitacionAEditar.setPrecio(precio);
		habitacionAEditar.setImg(imagen);
		habitacionAEditar.setHotel(hotelservicio.findById(idhotel).get());
		
		habitacionservicio.save(habitacionAEditar);

		return "redirect:/administracion/habitaciones/" + id;
		
	}
	
	@GetMapping("/eliminar/{id}")  
	public String habitacioneliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("habitacion", habitacionservicio.findById(id).get());
		
		return "admin/adminhabitacioneliminar";  
	}
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar habitacion por ID
	public String borrarHabitacion(@PathVariable Integer id) {

		habitacionservicio.deleteById(id);

		return "redirect:/administracion/habitaciones";
	}
	
	@GetMapping("/filtrar")
	public String filtrarHabitaciones(Model model, 
			@RequestParam(name = "idhotel", required = false) Integer idhotel) {

		List<HabitacionVO> lista = habitacionservicio.findAll();

		List<HabitacionVO> listafiltrada; 

		if (idhotel != null) {
			listafiltrada = lista.stream()
	                .filter(habitacion -> habitacion.getHotel() != null && habitacion.getHotel().getIdhotel() == idhotel)
	                .collect(Collectors.toList());
			lista = listafiltrada;
	    }

		model.addAttribute("habitaciones",lista);
		model.addAttribute("hoteles",hotelservicio.findAll());

		return "admin/adminhabitaciones";  
	}
	
}

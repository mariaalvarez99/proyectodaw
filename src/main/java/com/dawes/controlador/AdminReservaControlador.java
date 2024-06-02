package com.dawes.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.dawes.modelo.HotelVO;
import com.dawes.modelo.ReservaVO;
import com.dawes.servicio.HabitacionServicio;
import com.dawes.servicio.HotelServicio;
import com.dawes.servicio.ReservaServicio;
import com.dawes.servicio.UsuarioServicio;

@Controller
@RequestMapping("/administracion/reservas")
public class AdminReservaControlador {

	@Autowired
	private ReservaServicio reservaservicio;
	
	@Autowired
	private HabitacionServicio habitacionservicio;
	
	@Autowired
	private UsuarioServicio usuarioservicio;
	
	@Autowired
	private HotelServicio hotelservicio;
	

	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de reservas
	String reservas(Model model) {

		model.addAttribute("reservas", reservaservicio.findAll());
		model.addAttribute("usuarios", usuarioservicio.findAll());
		model.addAttribute("hoteles", hotelservicio.findAll());
		
		return "admin/adminreservas";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un reserva por su ID
	public String obtenerReserva(Model model, @PathVariable Integer id) {

		model.addAttribute("reserva", reservaservicio.findById(id).get());
		model.addAttribute("usuarios", usuarioservicio.findAll());
		model.addAttribute("habitaciones", habitacionservicio.findAll());
		model.addAttribute("hoteles", hotelservicio.findAll());

		return "admin/adminreserva";
	}
	
	@GetMapping("/nuevo")  
	public String resevaNuevo(Model model) {

		model.addAttribute("nuevoReserva",new ReservaVO() );
		model.addAttribute("hoteles", hotelservicio.findAll());
		model.addAttribute("habitaciones", habitacionservicio.findAll());
		model.addAttribute("usuarios", usuarioservicio.findAll());
		
		return "admin/adminreservanuevo";  
	}

	@PostMapping("/add") //maneja solicitud POST para agregar reserva
	public String addReserva(@ModelAttribute("nuevoReserva") ReservaVO reserva, BindingResult bindingResult) {
		
		reserva.setPrecio(reserva.getNumhabitaciones() * reserva.getHabitacion().getPrecio());

		reservaservicio.save(reserva);
		
		return "redirect:/administracion/reservas";
		
	}
	
	@GetMapping("/editar/{id}")  
	public String resevaeditar(@PathVariable Integer id, Model model) {

		model.addAttribute("reserva", reservaservicio.findById(id).get());
		model.addAttribute("reservaaEditar",new ReservaVO() );
		model.addAttribute("hoteles", hotelservicio.findAll());
		model.addAttribute("habitaciones", habitacionservicio.findAll());
		model.addAttribute("usuarios", usuarioservicio.findAll());
		
		return "admin/adminreservaeditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un reserva existente
	public String editarReserva(@PathVariable Integer id, 
			@RequestParam(required = false) int idreserva,
			@RequestParam(required = false) LocalDate fechareserva,
			@RequestParam(required = false) LocalDate fechainicio,
            @RequestParam(required = false) LocalDate fechafin,
            @RequestParam(required = false) int numhabitaciones,
            @RequestParam(required = false) int idhabitacion,
            @RequestParam(required = false) int idusuario) {

		ReservaVO reservaAEditar = reservaservicio.findById(id).get();
		reservaAEditar.setFechareserva(fechareserva);
		reservaAEditar.setFechainicio(fechainicio);
		reservaAEditar.setFechafin(fechafin);
		reservaAEditar.setNumhabitaciones(numhabitaciones);
		reservaAEditar.setHabitacion(habitacionservicio.findById(idhabitacion).get());
		reservaAEditar.setUsuario(usuarioservicio.findById(idusuario).get());
		reservaAEditar.setPrecio(reservaAEditar.getNumhabitaciones() * reservaAEditar.getHabitacion().getPrecio());
		
		reservaservicio.save(reservaAEditar);

		return "redirect:/administracion/reservas/" + id;
		
	}
	
	@GetMapping("/eliminar/{id}")  
	public String reservaeliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("reserva", reservaservicio.findById(id).get());
		
		return "admin/adminreservaeliminar";  
	}
	
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar reserva por ID
	public String borrarReserva(@PathVariable Integer id) {

		reservaservicio.deleteById(id);

		return "redirect:/administracion/reservas";
	}
	
	
	@GetMapping("/filtrar")
	public String filtrarReservas(Model model, 
			@RequestParam(name = "idhotel", required = false) Integer idhotel,
			@RequestParam(name = "idusuario", required = false) Integer idusuario,
			@RequestParam(name = "fecha1", required = false) LocalDate fecha1,
			@RequestParam(name = "fecha2", required = false) LocalDate fecha2) {

		List<ReservaVO> lista = reservaservicio.findAll();

		List<ReservaVO> listafiltrada; 
		
		if (idhotel != null && idhotel != 0) {
			listafiltrada = lista.stream()
                    .filter(reserva -> reserva.getHabitacion().getHotel().getIdhotel() == idhotel)
                    .collect(Collectors.toList());
			lista = listafiltrada;
		}

		if (idusuario != null && idusuario != 0) {
			listafiltrada = lista.stream()
                    .filter(reserva -> reserva.getUsuario().getIdusuario() == idusuario)
                    .collect(Collectors.toList());
			lista = listafiltrada;
		}
		
		if (fecha1 != null && fecha2 != null) {
			listafiltrada = lista.stream()
	                .filter(reserva -> 
                    (reserva.getFechainicio().isEqual(fecha1) || reserva.getFechainicio().isAfter(fecha1)) &&
                    reserva.getFechainicio().isBefore(fecha2))
	                .collect(Collectors.toList());
			lista = listafiltrada;
			
			listafiltrada = lista.stream()
	                .filter(reserva -> 
                    (reserva.getFechafin().isEqual(fecha2) || reserva.getFechafin().isBefore(fecha2) ) &&
                    reserva.getFechainicio().isBefore(fecha2))
	                .collect(Collectors.toList());
			lista = listafiltrada;
			
	    }

		

		model.addAttribute("reservas", lista);
		model.addAttribute("usuarios", usuarioservicio.findAll());
		model.addAttribute("hoteles", hotelservicio.findAll());

		return "admin/adminreservas";  
	}
	
}

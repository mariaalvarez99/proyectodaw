package com.dawes.controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.dawes.modelo.ReservaVO;
import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.servicio.ComentarioServicio;
import com.dawes.servicio.HabitacionServicio;
import com.dawes.servicio.HotelServicio;
import com.dawes.servicio.ReservaServicio;
import com.dawes.servicio.RolServicio;
import com.dawes.servicio.UsuarioServicio;

@Controller
public class WebControlador {
	
	@Autowired
	HotelServicio hotelservicio;
	
	@Autowired
	HabitacionServicio habicacionservicio;
	
	@Autowired
	ReservaServicio reservaservicio;
	
	@Autowired
	UsuarioServicio usuarioservicio;
	
	@Autowired
	RolServicio rolservicio;
	
	@Autowired
	ComentarioServicio comentarioservicio;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	@GetMapping("/")
	String home() {
		return "index";
	}
	
	@GetMapping("/hoteles")
	String hoteles(Model model) {
		
		model.addAttribute("hoteles", hotelservicio.findAll());
		
		return "hoteles";
	}
	
	@GetMapping("/habitaciones")
	String habitaciones(Model model,
			@RequestParam("idhotel") Integer id) {
		
		model.addAttribute("habitaciones", habicacionservicio.findByHotel(hotelservicio.findById(id).get()).get());
		model.addAttribute("hotel", hotelservicio.findById(id).get());
		
		return "habitaciones";
	}
	
	@GetMapping("/registrado/confirmarreserva/{id}")
	String confirmarreserva(Model model, @PathVariable Integer id, Authentication authentication) {
		
		if (authentication != null) {
            String username = authentication.getName();
            System.err.println(username + " - " + authentication.toString());
            Optional<UsuarioVO> usuario = usuarioservicio.findByDni(username);
            
            
            if (usuario.isPresent()) {
                model.addAttribute("usuario", usuario.get());
            } 
        }
		
		model.addAttribute("habitacion", habicacionservicio.findById(id).get());
		
		return "registrado/confirmarreserva";
	}
	
	@PostMapping({"/registrado/guardarreserva"})  
	String guardarreserva(Model model,
			@RequestParam(required = false) LocalDate finicio,
            @RequestParam(required = false) LocalDate ffin,
            @RequestParam(required = false) int nhabitaciones,
            @RequestParam(required = false) int idhabitacion,
            @RequestParam(required = false) int idusuario) {
		
		ReservaVO reserva = new ReservaVO();
		reserva.setFechareserva(LocalDate.now());
		
		reserva.setFechainicio(finicio);
		reserva.setFechafin(ffin);
		reserva.setNumhabitaciones(nhabitaciones);
		reserva.setHabitacion(habicacionservicio.findById(idhabitacion).get());
		reserva.setUsuario(usuarioservicio.findById(idusuario).get());
		
		reserva.setPrecio(reserva.getNumhabitaciones() * reserva.getHabitacion().getPrecio());
		
		
		if (reservaservicio.fechasdisponibles(finicio, ffin, idhabitacion, nhabitaciones)) {
			reservaservicio.save(reserva);
			
			model.addAttribute("reserva", reserva);
			
			return "registrado/reservaguardada";
		}else {
			model.addAttribute("habitacion", habicacionservicio.findById(idhabitacion).get());
			
			return "errordisponiblereserva";
		}
		
		
	}

	
	@GetMapping("/contacto")
	String contacto(Model model) {
		
		return "contacto";
	}
	
	@PostMapping({"/enviarcomentario"})  
	String enviarcomentario(
			@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String texto) {
		
		ComentarioVO comentario = new ComentarioVO();
		comentario.setEmail(email);
		comentario.setNombre(nombre);
		comentario.setTexto(texto);
		comentario.setFecha(LocalDate.now());
		comentario.setHora(LocalTime.now());
		
		comentarioservicio.save(comentario);
		
		return "redirect:/mensajeenviado";
	}
	
	@GetMapping("/mensajeenviado")
	String mensajeenviado() {
		return "mensajeenviado";
	}
	
	
	@GetMapping("/login")
	String login(Model model, Authentication authentication) {
		
		if (authentication != null) {
            String username = authentication.getName();
//            System.err.println(username + " - " + authentication.toString());
            Optional<UsuarioVO> usuario = usuarioservicio.findByDni(username);
            
            
            if (usuario.isPresent()) {
            	model.addAttribute("usuario", usuario.get());
//                model.addAttribute("username", usuario.get().getNombre());
//                model.addAttribute("roles", usuario.get().getRoles());
//                for (RolVO rol: usuario.get().getRoles())
//                	System.err.println(rol.getNombre());
            } 
        }
		
		
		model.addAttribute("nuevoUsuario", new UsuarioVO());
		
		return "login";
	}
	
	@PostMapping("/addUsuario") 
	public String addSala(@ModelAttribute("nuevoUsuario") UsuarioVO usuario, BindingResult bindingResult) {

		
		usuario.setActivado(true);
        
        usuario.setContrasenacifrada(passwordEncoder.encode(usuario.getContrasenacifrada()));
        
        System.err.println(usuario.getNombre());
        System.err.println(usuario.getContrasenacifrada());
        
        RolVO rol;
        if (!rolservicio.findByNombre("ROLE_USER").isPresent()) {
        	rol = new RolVO("ROLE_USER");
        	rolservicio.save(rol);
        }else {
        	rol = rolservicio.findByNombre("ROLE_USER").get();
        }
        
        rol.getUsuarios().add(usuario);
        usuario.getRoles().add(rol);
        
        
        usuarioservicio.save(usuario);
	    
		return "redirect:/login";
	}
	
	
	@GetMapping("/administracion")
	String administracion(Model model) {
		
		return "admin/administracion";
	}
	
	
	@GetMapping("/mapaweb")
	String mapaweb() {
		
		return "mapaweb";
	}
	
	@RequestMapping(value = "/403")
	public String accesoDenegado(Model modelo) {

		modelo.addAttribute("message", "No tienes permiso de acceso a esta página");
		return "403Page";
	}
	
}

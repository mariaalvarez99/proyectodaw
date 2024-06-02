package com.dawes.controlador;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.dawes.modelo.HabitacionVO;
import com.dawes.modelo.HotelVO;
import com.dawes.modelo.ReservaVO;
import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.servicio.HabitacionServicio;
import com.dawes.servicio.HotelServicio;
import com.dawes.servicio.ReservaServicio;
import com.dawes.servicio.RolServicio;
import com.dawes.servicio.UsuarioServicio;

@Controller
@RequestMapping("/recepcion")
public class RecepcionControlador {
	
	@Autowired
	HotelServicio hotelservicio;
	
	@Autowired
	HabitacionServicio habitacionservicio;
	
	@Autowired
	ReservaServicio reservaservicio;
	
	@Autowired
	UsuarioServicio usuarioservicio;
	
	@Autowired
	RolServicio rolservicio;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de recepcion
	String usuarios(Model model) {
		
		return "recepcion/recepcion";  
	}
	
	
	@GetMapping({"/comprobardisponibilidad"}) 
	public String comprobardisponibilidad(Model model,
			@RequestParam(name = "idhotel", required = false) Integer idhotel,
			@RequestParam(name = "anomes", required = false) YearMonth anomes) {
		
		 System.err.println("idhotel " + idhotel + " anomes " + anomes);
		    
		    if (idhotel != null && idhotel != 0 && anomes != null){
		        
		        HotelVO hotel = hotelservicio.findById(idhotel).get();
		        List<HabitacionVO> listhabitaciones = habitacionservicio.findByHotel(hotel).get();
		        
		        Map<String, Map<String, List<Object[]>>> meseshabitaciones = new TreeMap<>(); //treemap ordena
		        
		        for(HabitacionVO habitacion : listhabitaciones) {
		            int idhabitacion = habitacion.getIdhabitacion();
		            String tipohabitacion = habitacion.getTipo();
		            
		            Map<String, List<Object[]>> listahabitacion = new LinkedHashMap<>(); //Respeta el orden insertado
		            listahabitacion.put(anomes.toString(), reservaservicio.getlistames(anomes, idhabitacion));
		            listahabitacion.put(anomes.plusMonths(1).toString(), reservaservicio.getlistames(anomes.plusMonths(1), idhabitacion));
		            
		            meseshabitaciones.put(tipohabitacion, listahabitacion);
		        }
		        
		        model.addAttribute("lista", meseshabitaciones);
		        model.addAttribute("hotel", hotel);
		    }
		    
		    model.addAttribute("hoteles", hotelservicio.findAll());

		    return "recepcion/comprobardisponibilidad";
	}
	
	
	@GetMapping({"/reservas"}) 
	public String buscarreserva(Model model) {

		model.addAttribute("reservas", reservaservicio.findAll());
		model.addAttribute("usuarios", usuarioservicio.findAll());
		model.addAttribute("hoteles", hotelservicio.findAll());

		return "recepcion/recepcionreservas";
	}
	
	@GetMapping({"/reserva/{id}"}) 
	public String fichareserva(@PathVariable Integer id,Model model) {

		model.addAttribute("reserva", reservaservicio.findById(id).get());

		return "recepcion/recepcionreserva";
	}
	
	
	@GetMapping({"/editarreserva/{id}"}) 
	public String editarreserva(@PathVariable Integer id, Model model,
			@RequestParam(name = "idhotel", required = false) Integer idhotel) {

		model.addAttribute("reserva", reservaservicio.findById(id).get());
		model.addAttribute("reservaaEditar",new ReservaVO() );
		
		if (idhotel == null || idhotel == 0)
			model.addAttribute("hoteles", hotelservicio.findAll());
		else 
		{
			model.addAttribute("hotel", hotelservicio.findById(idhotel).get());
			model.addAttribute("habitaciones", habitacionservicio.findByHotel(hotelservicio.findById(idhotel).get()).get());
		}

		return "recepcion/recepcionreservaeditar";
	}
	
	@PostMapping({"/reserva/edit"}) 
	public String editreserva(Model model,
			@RequestParam(required = false) int idreserva,
			@RequestParam(required = false) int idusuario,
			@RequestParam(required = false) int idhabitacion,
			@RequestParam(required = false) LocalDate fechainicio,
            @RequestParam(required = false) LocalDate fechafin,
            @RequestParam(required = false) int numhabitaciones) {

		ReservaVO reserva = reservaservicio.findById(idreserva).get();
		reserva.setUsuario(usuarioservicio.findById(idusuario).get());
		reserva.setHabitacion(habitacionservicio.findById(idhabitacion).get());
		reserva.setFechainicio(fechainicio);
		reserva.setFechafin(fechafin);
		reserva.setNumhabitaciones(numhabitaciones);
		reserva.setFechareserva(LocalDate.now());
		reserva.setPrecio(numhabitaciones * habitacionservicio.findById(idhabitacion).get().getPrecio());
		
		if (reservaservicio.fechasdisponibles(fechainicio, fechafin, idhabitacion, numhabitaciones)) {
			reservaservicio.save(reserva);
			
			return "redirect:/recepcion/reservas";
		}else {
			model.addAttribute("idreserva", idreserva);
			
			return "recepcion/recepcionreservaerrordisponibleeditar";
		}
		
	}
	
	@GetMapping({"/nuevareserva"}) 
	public String nuevareserva(Model model,
			@RequestParam(name = "idusuario", required = true) Integer idusuario,
			@RequestParam(name = "idhotel", required = false) Integer idhotel) {

		model.addAttribute("nuevoReserva",new ReservaVO() );
		model.addAttribute("usuario", usuarioservicio.findById(idusuario).get());
		
		if (idhotel == null || idhotel == 0)
			model.addAttribute("hoteles", hotelservicio.findAll());
		else 
		{
			model.addAttribute("hotel", hotelservicio.findById(idhotel).get());
			model.addAttribute("habitaciones", habitacionservicio.findByHotel(hotelservicio.findById(idhotel).get()).get());
		}

		return "recepcion/recepcionreservanueva";
	}
	
	@PostMapping({"/reservas/add"}) 
	public String nuevareserva(Model model, 
			@RequestParam(required = false) int idusuario,
			@RequestParam(required = false) int idhabitacion,
			@RequestParam(required = false) LocalDate finicio,
            @RequestParam(required = false) LocalDate ffin,
            @RequestParam(required = false) int nhabitaciones ) {

		ReservaVO reserva = new ReservaVO();
		reserva.setUsuario(usuarioservicio.findById(idusuario).get());
		reserva.setHabitacion(habitacionservicio.findById(idhabitacion).get());
		reserva.setFechainicio(finicio);
		reserva.setFechafin(ffin);
		reserva.setNumhabitaciones(nhabitaciones);
		reserva.setFechareserva(LocalDate.now());
		reserva.setPrecio(nhabitaciones * habitacionservicio.findById(idhabitacion).get().getPrecio());
		
		if (reservaservicio.fechasdisponibles(finicio, ffin, idhabitacion, nhabitaciones)) {
			reservaservicio.save(reserva);
			
			return "redirect:/recepcion/reservas";
		}else {
			model.addAttribute("idusuario", idusuario);
			model.addAttribute("idhotel", habitacionservicio.findById(idhabitacion).get().getHotel().getIdhotel());
			
			return "recepcion/recepcionreservaerrordisponible";
		}
		
	}
	
	@GetMapping("/reservas/filtrar")
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

		return "recepcion/recepcionreservas";  
	}
	
	
	@GetMapping({"/usuarios"}) 
	public String buscarusuario(Model model) {

		model.addAttribute("usuarios", usuarioservicio.findAll());

		return "recepcion/recepcionusuarios";
	}
	
	@GetMapping({"/usuario/{id}"}) 
	public String fichausuario(@PathVariable Integer id,Model model) {

		model.addAttribute("usuario", usuarioservicio.findById(id).get());

		return "recepcion/recepcionusuario";
	}
	
	@GetMapping({"/editarusuario/{id}"}) 
	public String editarusuario(@PathVariable Integer id, Model model) {

		model.addAttribute("usuario", usuarioservicio.findById(id).get());
		model.addAttribute("usuarioaEditar",new UsuarioVO() );
		model.addAttribute("roles", rolservicio.findAll());

		return "recepcion/recepcionusuarioeditar";
	}
	
	@PostMapping({"/usuario/edit"}) 
	public String editusuario(
			@RequestParam(required = false) int idusuario,
			@RequestParam(required = false) String dni,
			@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) int telefono,
            @RequestParam(required = false) boolean activo) {

		UsuarioVO usuario = usuarioservicio.findById(idusuario).get();
		usuario.setDni(dni);
		usuario.setNombre(nombre);
		usuario.setEmail(email);
		usuario.setTelefono(telefono);
		usuario.setActivado(activo);
		
		usuarioservicio.save(usuario);

		return "redirect:/recepcion/usuarios";
	}
	
	
	@GetMapping({"/nuevousuario"}) 
	public String nuevousuario(Model model) {

		model.addAttribute("nuevoUsuario",new UsuarioVO() );
		model.addAttribute("roles", rolservicio.findAll());

		return "recepcion/recepcionusuarionuevo";
	}
	
	@PostMapping("/usuario/add") //maneja solicitud POST para agregar usuario
	public String addUsuario(@ModelAttribute("nuevoUsuario") UsuarioVO usuario, BindingResult bindingResult) {
		
         
        RolVO rol = rolservicio.findByNombre("ROLE_USER").get(); 
        
        rol.getUsuarios().add(usuario);
        usuario.getRoles().add(rol);
		
		usuario.setActivado(true);
		usuario.setContrasenacifrada(passwordEncoder.encode(usuario.getContrasenacifrada()));
		
		usuarioservicio.save(usuario);
		
		return "redirect:/recepcion/usuarios";
	}
	
	@GetMapping("/usuarios/filtrar")
	public String filtrarUsuarios(Model model, @RequestParam(name = "dni", required = false) String dni) {

		List<UsuarioVO> lista = new ArrayList<>();

		Optional<UsuarioVO> optional = usuarioservicio.findByDni(dni);
		if (optional.isPresent()) {
		    lista.add(optional.get());
		}
		model.addAttribute("usuarios",lista);

		return "recepcion/recepcionusuarios";  
	}
	
}

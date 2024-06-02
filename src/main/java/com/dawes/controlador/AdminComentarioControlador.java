package com.dawes.controlador;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.dawes.servicio.ComentarioServicio;

@Controller
@RequestMapping("/administracion/comentarios")
public class AdminComentarioControlador {

	@Autowired
	private ComentarioServicio comentarioservicio;
	
	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de comentarios
	String comentarios(Model model) {

		model.addAttribute("comentarios", comentarioservicio.findAll());
		
		return "admin/admincomentarios";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un comentario por su ID
	public String obtenerComentario(Model model, @PathVariable Integer id) {

		model.addAttribute("comentario", comentarioservicio.findById(id).get());

		return "admin/admincomentario";
	}
	
	@GetMapping("/nuevo")  
	public String comentarioNuevo(Model model) {

		model.addAttribute("nuevoComentario",new ComentarioVO() );
		
		return "admin/admincomentarionuevo";  
	}
	
	@PostMapping("/add") //maneja solicitud POST para agregar comentario
	public String addComentario(@ModelAttribute("nuevoComentario") ComentarioVO comentario, BindingResult bindingResult) {

		comentarioservicio.save(comentario);
		
		return "redirect:/administracion/comentarios";
	}
	
	@GetMapping("/editar/{id}")  
	public String comentarioeditar(@PathVariable Integer id, Model model) {

		model.addAttribute("comentario", comentarioservicio.findById(id).get());
		model.addAttribute("comentarioaEditar",new ComentarioVO() );
		
		return "admin/admincomentarioeditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un comentario existente
	public String editarComentario(@PathVariable Integer id, 
			@RequestParam(required = false) int idcomentario,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String email,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) LocalTime hora) {

		ComentarioVO comentarioAEditar = comentarioservicio.findById(id).get();
		comentarioAEditar.setNombre(nombre);
		comentarioAEditar.setEmail(email);
		comentarioAEditar.setTexto(texto);
		comentarioAEditar.setFecha(fecha);
		comentarioAEditar.setHora(hora);
		comentarioservicio.save(comentarioAEditar);

		return "redirect:/administracion/comentarios/" + id;
	}
	
	@GetMapping("/eliminar/{id}")  
	public String comentarioeliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("comentario", comentarioservicio.findById(id).get());
		
		return "admin/admincomentarioeliminar";  
	}
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar comentario por ID
	public String borrarComentario(@PathVariable Integer id) {

		comentarioservicio.delete(comentarioservicio.findById(id).get());

		return "redirect:/administracion/comentarios";
	}
	

	@GetMapping("/filtrar")
	public String filtrarComentarios(Model model, 
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "fecha1", required = false) LocalDate fecha1,
			@RequestParam(name = "fecha2", required = false) LocalDate fecha2) {

		List<ComentarioVO> lista = comentarioservicio.findAll();

		List<ComentarioVO> listafiltrada; 

		if (email != null && !email.isEmpty()) {
			listafiltrada = lista.stream()
                    .filter(categoria -> categoria.getEmail().equals(email))
                    .collect(Collectors.toList());
			lista = listafiltrada;
		}
		
		if (fecha1 != null && fecha2 != null) {
			listafiltrada = lista.stream()
	                .filter(comentario ->
	                        (comentario.getFecha().isAfter(fecha1) || comentario.getFecha().isEqual(fecha1))
	                        && (comentario.getFecha().isBefore(fecha2) || comentario.getFecha().isEqual(fecha2)))
	                .collect(Collectors.toList());
			lista = listafiltrada;
	    }

		

		model.addAttribute("comentarios",lista);

		return "admin/admincomentarios";  
	}
	
}

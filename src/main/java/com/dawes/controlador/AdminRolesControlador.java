package com.dawes.controlador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.servicio.RolServicio;
import com.dawes.servicio.UsuarioServicio;

@Controller
@RequestMapping("/administracion/roles")
public class AdminRolesControlador {

	@Autowired
	private RolServicio rolservicio;
	
	@Autowired
	private UsuarioServicio usuarioservicio;
	
	
	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de roles
	String roles(Model model) {

		model.addAttribute("roles", rolservicio.findAll());
		
		
		return "admin/adminroles";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un rol por su ID
	public String obtenerRol(Model model, @PathVariable Integer id) {

		model.addAttribute("rol", rolservicio.findById(id).get());
		model.addAttribute("usuarios", usuarioservicio.findAll());

		return "admin/adminrol";
	}
	
	@GetMapping("/nuevo")  
	public String rolNuevo(Model model) {

		model.addAttribute("nuevoRol",new RolVO() );
		model.addAttribute("usuarios", usuarioservicio.findAll());
		
		return "admin/adminrolnuevo";  
	}
	
	@PostMapping("/add") //maneja solicitud POST para agregar rol
	public String addRol(Model model, @ModelAttribute("nuevoRol") RolVO rol, BindingResult bindingResult,
			@RequestParam(value = "usuarios", required = false) List<Integer> usuariosSeleccionados) {
		
		if (usuariosSeleccionados != null && !usuariosSeleccionados.isEmpty()) {
	        
	        for (Integer usuarioId : usuariosSeleccionados) {
	        	UsuarioVO usuario = usuarioservicio.findById(usuarioId).get();
				usuario.getRoles().add(rol);
		        
				rol.getUsuarios().add(usuario);
	           
	        }
	        
	    }
		
		rolservicio.save(rol);
		
		return "redirect:/administracion/roles";
        
	}
	
	@GetMapping("/editar/{id}")  
	public String roleditar(@PathVariable Integer id, Model model) {

		model.addAttribute("rol", rolservicio.findById(id).get());
		model.addAttribute("rolaEditar",new RolVO() );
		model.addAttribute("usuarios", usuarioservicio.findAll());
		
		return "admin/adminroleditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un rol existente
	public String editarRol(Model model, @PathVariable Integer id, 
			@RequestParam(required = false) int idrol,
			@RequestParam(required = false) String nombre,
			@RequestParam(name = "listausuarios", required = false) List<Integer> listaUsuariosIds) {

		RolVO rolAEditar = rolservicio.findById(id).get();
		rolAEditar.setNombre(nombre);
		
		java.util.Set<UsuarioVO> listausuariosanterior = rolAEditar.getUsuarios(); 
		for (UsuarioVO usuario : listausuariosanterior) 
			usuario.getRoles().remove(rolAEditar);
		
		rolAEditar.getUsuarios().clear();
		
		if (listaUsuariosIds != null && !listaUsuariosIds.isEmpty()) {
//	        List<UsuarioVO> listaUsuarios = usuarioServicio.findByIdIn(listaUsuariosIds);
//	        rolAEditar.setUsuarios(new HashSet<>(listaUsuarios));
			for (Integer usuarioId : listaUsuariosIds) {
				UsuarioVO usuario = usuarioservicio.findById(usuarioId).get();
				System.err.println(usuario.getIdusuario() + " - " + usuario.getDni());
				rolAEditar.getUsuarios().add(usuario);
			 }
	    }
		
		rolservicio.save(rolAEditar);

		return "redirect:/administracion/roles/" + id;
	}
	
	@GetMapping("/eliminar/{id}")  
	public String roleliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("rol", rolservicio.findById(id).get());
		
		return "admin/adminroleliminar";  
	}
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar rol por ID
	public String borrarRol(@PathVariable Integer id) {

		rolservicio.delete(rolservicio.findById(id).get());

		return "redirect:/administracion/roles";
	}
	
	
	@GetMapping("/filtrar")
	public String filtrarRoles(Model model, @RequestParam(name = "nombre", required = false) String nombre) {

		List<RolVO> lista = new ArrayList<>();

		Optional<RolVO> optional = rolservicio.findByNombre(nombre);
		if (optional.isPresent()) {
		    lista.add(optional.get());
		}
		model.addAttribute("roles",lista);

		return "admin/adminroles";  
	}
}

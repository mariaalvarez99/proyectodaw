package com.dawes.controlador;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Set;
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

import com.dawes.modelo.RolVO;
import com.dawes.modelo.UsuarioVO;
import com.dawes.servicio.RolServicio;
import com.dawes.servicio.UsuarioServicio;

@Controller
@RequestMapping("/administracion/usuarios")
public class AdminUsuarioControlador {

	@Autowired
	UsuarioServicio usuarioservicio;
	
	@Autowired
	RolServicio rolservicio;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de usuarios
	String usuarios(Model model) {

		model.addAttribute("usuarios", usuarioservicio.findAll());
		
		return "admin/adminusuarios";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un usuario por su ID
	public String obtenerUsuario(Model model, @PathVariable Integer id) {

		model.addAttribute("usuario", usuarioservicio.findById(id).get());
		model.addAttribute("roles", rolservicio.findAll());

		return "admin/adminusuario";
	}

	@GetMapping("/nuevo")  
	public String usuarioNuevo(Model model) {

		model.addAttribute("nuevoUsuario",new UsuarioVO() );
		model.addAttribute("roles", rolservicio.findAll());
		
		return "admin/adminusuarionuevo";  
	}
	
	@PostMapping("/add") //maneja solicitud POST para agregar usuario
	public String addUsuario(Model model, @ModelAttribute("nuevoUsuario") UsuarioVO usuario, BindingResult bindingResult,
			@RequestParam(value = "roles", required = false) List<Integer> rolesSeleccionados) {

		System.err.println(rolesSeleccionados);
		
		
		if (rolesSeleccionados != null && !rolesSeleccionados.isEmpty()) {
	        
	        for (Integer rolId : rolesSeleccionados) {
	        	RolVO rol = rolservicio.findById(rolId).get();
				System.err.println(rol.getIdrol() + " - " + rol.getNombre());
				rol.getUsuarios().add(usuario);
		        
				usuario.getRoles().add(rol);
	           
	        }
	        
	    }
		
		
		usuario.setContrasenacifrada(passwordEncoder.encode(usuario.getContrasenacifrada()));

		usuarioservicio.save(usuario);
		
		return "redirect:/administracion/usuarios";
	}
	
	@GetMapping("/editar/{id}")  
	public String usuarioeditar(@PathVariable Integer id, Model model) {

		model.addAttribute("usuario", usuarioservicio.findById(id).get());
		model.addAttribute("usuarioaEditar",new UsuarioVO() );
		model.addAttribute("roles", rolservicio.findAll());
		model.addAttribute("infoerror", "No se ha podido guardar, Clave duplicada");
		
		return "admin/adminusuarioeditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un usuario existente
	public String editarUsuario(Model model, @PathVariable Integer id, 
			@RequestParam(required = false) int idusuario,
			@RequestParam(required = false) String dni,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) int telefono,
			@RequestParam(required = false) String contrasenacifrada,
			@RequestParam(required = false) boolean activado,
			@RequestParam(name = "listaroles", required = false) List<Integer> listaroles) {

		UsuarioVO usuarioAEditar = usuarioservicio.findById(id).get();
		usuarioAEditar.setDni(dni);
		usuarioAEditar.setNombre(nombre);
		usuarioAEditar.setEmail(email);
		usuarioAEditar.setTelefono(telefono);
//		usuarioAEditar.setContrasenacifrada(contrasenacifrada);
		usuarioAEditar.setContrasenacifrada(passwordEncoder.encode(contrasenacifrada));
		usuarioAEditar.setActivado(activado);
		
		java.util.Set<RolVO> listarolesanterior = usuarioAEditar.getRoles(); 
		for (RolVO rol : listarolesanterior) 
			rol.getUsuarios().remove(usuarioAEditar);
		
		usuarioAEditar.getRoles().clear();
		
		if (listaroles != null && !listaroles.isEmpty()) {
			for (Integer rolId : listaroles) {
				RolVO rol = rolservicio.findById(rolId).get();
				System.err.println(rol.getIdrol() + " - " + rol.getNombre());
				if (!rol.getUsuarios().contains(usuarioAEditar)) 
		            rol.getUsuarios().add(usuarioAEditar);
		        
				usuarioAEditar.getRoles().add(rol);
			 }
			
		}
	
		
//		for(UsuarioRolVO ur : usuarioAEditar.getRoles()) {
//			UsuarioVO us = ur.getUsuario();
//			
//			RolVO ro = ur.getRol();
//			ro.getUsuarios().remove(usuarioAEditar);
//		}
//		
//	    usuarioAEditar.getRoles().clear();
//
//	    if (listaroles != null && !listaroles.isEmpty()) {
//	        for (Integer rolId : listaroles) {
//	            UsuarioRolVO usuarioRol = new UsuarioRolVO();
//	            RolVO rol = rolservicio.findById(rolId).get();
//	            usuarioRol.setRol(rol);
//	            usuarioRol.setUsuario(usuarioAEditar);
//	            usuarioAEditar.getRoles().add(usuarioRol);
//	        }
//	    } else {
//	        RolVO rolRegistrado = rolservicio.findByNombre("registrado").get();
//	        UsuarioRolVO usuarioRol = new UsuarioRolVO();
//	        usuarioRol.setRol(rolRegistrado);
//	        usuarioRol.setUsuario(usuarioAEditar);
//	        usuarioAEditar.getRoles().add(usuarioRol);
//	    }
		
		
		//cifrar contraseña
		
		
	    usuarioservicio.save(usuarioAEditar);

		return "redirect:/administracion/usuarios/" + id;
	}
	
	@GetMapping("/eliminar/{id}")  
	public String usuarioeliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("usuario", usuarioservicio.findById(id).get());
		
		return "admin/adminusuarioeliminar";  
	}
	
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar usuario por ID
	public String borrarUsuario(@PathVariable Integer id) {

		usuarioservicio.deleteById(id);

		return "redirect:/administracion/usuarios";
	}
	
	
	@GetMapping("/filtrar")
	public String filtrarUsuarios(Model model, @RequestParam(name = "dni", required = false) String dni) {

		List<UsuarioVO> lista = new ArrayList<>();

		Optional<UsuarioVO> optional = usuarioservicio.findByDni(dni);
		if (optional.isPresent()) {
		    lista.add(optional.get());
		}
		model.addAttribute("usuarios",lista);

		return "admin/adminusuarios";  
	}
	
}

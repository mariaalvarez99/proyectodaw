package com.dawes.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.CategoriaVO;
import com.dawes.servicio.CategoriaServicio;

@Controller
@RequestMapping("/administracion/categorias")
public class AdminCategoriaControlador {

	@Autowired
	private CategoriaServicio categoriaservicio;
	
	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de categorias
	String categorias(Model model) {

		model.addAttribute("categorias", categoriaservicio.findAll());
		
		return "admin/admincategorias";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un categoria por su ID
	public String obtenerCategoria(Model model, @PathVariable Integer id) {

		model.addAttribute("categoria", categoriaservicio.findById(id).get());

		return "admin/admincategoria";
	}
	
	@GetMapping("/nuevo")  
	public String categoriaNuevo(Model model) {

		model.addAttribute("nuevoCategoria",new CategoriaVO() );
		
		return "admin/admincategorianuevo";  
	}
	
	@PostMapping("/add") //maneja solicitud POST para agregar categoria
	public String addCategoria(Model model, @ModelAttribute("nuevoCategoria") CategoriaVO categoria, BindingResult bindingResult) {

		categoriaservicio.save(categoria);
		
		return "redirect:/administracion/categorias";
		
	}
	
	@GetMapping("/editar/{id}")  
	public String categoriaeditar(@PathVariable Integer id, Model model) {

		model.addAttribute("categoria", categoriaservicio.findById(id).get());
		model.addAttribute("categoriaaEditar",new CategoriaVO() );
		
		return "admin/admincategoriaeditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un categoria existente
	public String editarCategoria(Model model, @PathVariable Integer id,
			@RequestParam(required = false) int idcategoria,
			@RequestParam(required = false) int estrellas,
			@RequestParam(required = false) String descripcion) {

		CategoriaVO categoriaAEditar = categoriaservicio.findById(id).get();
		categoriaAEditar.setEstrellas(estrellas);
		categoriaAEditar.setDescripcion(descripcion);
		
		categoriaservicio.save(categoriaAEditar);

		return "redirect:/administracion/categorias/" + id;

	}
	
	@GetMapping("/eliminar/{id}")  
	public String categoriaeliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("categoria", categoriaservicio.findById(id).get());
		
		return "admin/admincategoriaeliminar";  
	}
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar categoria por ID
	public String borrarCategoria(@PathVariable Integer id) {

		categoriaservicio.deleteById(id);

		return "redirect:/administracion/categorias";
	}
	
	
	@GetMapping("/filtrar")
	public String filtrarCategorias(Model model, @RequestParam(name = "estrellas", required = false) int estrellas) {

		List<CategoriaVO> lista = new ArrayList<>();

		Optional<CategoriaVO> optional = categoriaservicio.findByEstrellas(estrellas);
		if (optional.isPresent()) {
		    lista.add(optional.get());
		}
		model.addAttribute("categorias",lista);

		return "admin/admincategorias";  
	}
	
	
}

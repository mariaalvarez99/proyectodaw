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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dawes.modelo.CategoriaVO;
import com.dawes.modelo.HotelVO;
import com.dawes.servicio.CategoriaServicio;
import com.dawes.servicio.HotelServicio;

@Controller
@RequestMapping("/administracion/hoteles")
public class AdminHotelControlador {
	
	@Autowired
	private HotelServicio hotelservicio;
	
	@Autowired
	private CategoriaServicio categoriaservicio;
	
	
	@GetMapping(value={"","/"})  //maneja solicitudes GET para la página principal de hoteles
	String hoteles(Model model) {

		model.addAttribute("hoteles", hotelservicio.findAll());
		
		return "admin/adminhoteles";  
	}
	
	@GetMapping({"/{id}"})  //maneja solicitud GET para obtener detalles de un hotel por su ID
	public String obtenerHotel(Model model, @PathVariable Integer id) {

		model.addAttribute("hotel", hotelservicio.findById(id).get());
		model.addAttribute("categorias", categoriaservicio.findAll());

		return "admin/adminhotel";
	}
	
	@GetMapping("/nuevo")  
	public String hotelNuevo(Model model) {

		model.addAttribute("nuevoHotel",new HotelVO() );
		model.addAttribute("categorias", categoriaservicio.findAll());
		
		return "admin/adminhotelnuevo";  
	}
	
	@PostMapping("/add") //maneja solicitud POST para agregar hotel
	public String addHotel(Model model, @ModelAttribute("nuevoHotel") HotelVO hotel, BindingResult bindingResult) {

		System.err.println(hotel.toString());
		hotelservicio.save(hotel);
		
		return "redirect:/administracion/hoteles";
	}
	
	@GetMapping("/editar/{id}")  
	public String hoteleditar(@PathVariable Integer id, Model model) {

		model.addAttribute("hotel", hotelservicio.findById(id).get());
		model.addAttribute("hotelaEditar",new HotelVO() );
		model.addAttribute("categorias", categoriaservicio.findAll());
		
		return "admin/adminhoteleditar";  
	}
	
	@PostMapping("/edit/{id}") //maneja solicitud POST para editar un hotel existente
	public String editarHotel(Model model, @PathVariable Integer id, 
			@RequestParam(required = false) int idhotel,
			@RequestParam(required = false) String nombre,
			@RequestParam(required = false) int idcategoria,
            @RequestParam(required = false) String imagen,
			@RequestParam(required = false) String descripcion,
			@RequestParam(required = false) String direccion) {

		HotelVO hotelAEditar = hotelservicio.findById(id).get();
		hotelAEditar.setNombre(nombre);
		hotelAEditar.setCategoria(categoriaservicio.findById(idcategoria).get());
		hotelAEditar.setImg(imagen);
		hotelAEditar.setDescripcion(descripcion);
		hotelAEditar.setDireccion(direccion);
		
		hotelservicio.save(hotelAEditar);

		return "redirect:/administracion/hoteles/" + id;
	}
	
	@GetMapping("/eliminar/{id}")  
	public String hoteleliminar(@PathVariable Integer id, Model model) {

		model.addAttribute("hotel", hotelservicio.findById(id).get());
		
		return "admin/adminhoteleliminar";  
	}
	
	
	@GetMapping({"/delete/{id}"})  //maneja solicitud GET para eliminar hotel por ID
	public String borrarHotel(@PathVariable Integer id) {

		hotelservicio.deleteById(id);

		return "redirect:/administracion/hoteles";
	}
	
	
	@GetMapping("/filtrar")
	public String filtrarhoteles(Model model, @RequestParam(name = "nombre", required = false) String nombre) {

		List<HotelVO> lista = new ArrayList<>();

		Optional<HotelVO> optional = hotelservicio.findByNombre(nombre);
		if (optional.isPresent()) {
		    lista.add(optional.get());
		}
		model.addAttribute("hoteles",lista);

		return "admin/adminhoteles";  
	}
}

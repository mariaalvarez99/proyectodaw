package com.dawes.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException exception, Model model) {
        ModelAndView modelAndView = new ModelAndView("error");

        String errorMessage = "Error al insertar";
        String errorDetails = "Error de restricción de clave foránea";

        // Verificar si la causa de la excepción está relacionada con una restricción de clave foránea
        if (exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            org.hibernate.exception.ConstraintViolationException cause = (org.hibernate.exception.ConstraintViolationException) exception.getCause();
            if (cause.getConstraintName().toLowerCase().contains("foreign key")) {
                errorMessage = "Error de restricción de clave foránea";
                errorDetails = "La clave foránea especificada no existe en la tabla relacionada.";
            }
        }

        modelAndView.addObject("errorMessage", errorMessage);
        modelAndView.addObject("errorDetails", errorDetails);
        return modelAndView;
    }
	
	@ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(ResponseStatusException exception, Model model) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Recurso no encontrado");
        modelAndView.addObject("errorDetails", exception.getReason());
        return modelAndView;
    }
	
	
	 @ExceptionHandler(Exception.class)
	    public ModelAndView handleException(Exception exception, Model model) {
	        ModelAndView modelAndView = new ModelAndView("error");
	        modelAndView.addObject("errorMessage", exception.getMessage());
	        modelAndView.addObject("errorDetails", exception.toString());
	        return modelAndView;
	    }
	 
}

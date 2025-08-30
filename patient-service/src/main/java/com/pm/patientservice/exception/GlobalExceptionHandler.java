package com.pm.patientservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

		HashMap<String, String> errors = new HashMap<>(); // Stored in key value

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage())); // We get error from arg and
																							// then we bind it

		return ResponseEntity.badRequest().body(errors);

	}

	@ExceptionHandler(EmailAllReadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleEmailAllReadyExistsException(EmailAllReadyExistsException ex) {
		log.warn("Email already exist {}", ex.getMessage()); // for catch bug
		HashMap<String, String> errors = new HashMap<>();
		errors.put("message", "Email address already exists ohhh");
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(PatientNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex){
		log.warn("Patient not found {}",ex.getMessage());
		
		HashMap<String,String> errors = new HashMap<>();
		errors.put("message", "Patient not found");
		return ResponseEntity.badRequest().body(errors);
	}

}

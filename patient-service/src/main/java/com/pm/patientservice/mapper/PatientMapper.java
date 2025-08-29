package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

public class PatientMapper { 
	
	public static PatientResponseDTO toDTO(Patient patient) {
		PatientResponseDTO patientDto = new PatientResponseDTO();
		
		patientDto.setId(patient.getId().toString());
		patientDto.setName(patient.getName());
		patientDto.setAddress(patient.getAddress());
		patientDto.setEmail(patient.getEmail());
		patientDto.setDateOfBirth(patient.getDateOfBirth().toString());
		
		return patientDto;
	}

}

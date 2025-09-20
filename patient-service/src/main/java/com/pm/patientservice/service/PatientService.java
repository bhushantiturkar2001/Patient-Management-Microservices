package com.pm.patientservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAllReadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceCliet;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;

@Service
public class PatientService {

	private PatientRepository patientRepository;
	private final BillingServiceCliet billingServiceCliet;
	private final KafkaProducer kafkaProducer;

	public PatientService(PatientRepository patientRepository, BillingServiceCliet billingServiceCliet, KafkaProducer kafkaProducer) {
		this.patientRepository = patientRepository;
		this.billingServiceCliet = billingServiceCliet;
		this.kafkaProducer = kafkaProducer;
	}

	public List<PatientResponseDTO> getPatients() {
		List<Patient> patients = patientRepository.findAll();

		List<PatientResponseDTO> patientResponseDTOs = patients.stream().map(patient -> PatientMapper.toDTO(patient))
				.toList();

		return patientResponseDTOs;
	}

	public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
		// Verify email
		if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
			throw new EmailAllReadyExistsException(
					"A patient with this email is all ready exists" + patientRequestDTO.getEmail());
		}

		Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

		billingServiceCliet.createBillingAccount(newPatient.getId().toString(), newPatient.getName(),
				newPatient.getEmail());
		
		kafkaProducer.sendEvent(newPatient);

		return PatientMapper.toDTO(newPatient);
	}

	public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

		if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {

			throw new EmailAllReadyExistsException(
					"Email all ready register with this email: " + patientRequestDTO.getEmail());

		}

		patient.setName(patientRequestDTO.getName());
		patient.setEmail(patientRequestDTO.getEmail());
		patient.setAddress(patientRequestDTO.getAddress());
		patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

		Patient updatePatient = patientRepository.save(patient);

		return PatientMapper.toDTO(updatePatient);

	}

	public void deletePatient(UUID id) {
		patientRepository.deleteById(id);
	}

}

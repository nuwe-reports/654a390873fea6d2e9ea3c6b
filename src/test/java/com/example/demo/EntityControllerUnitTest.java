
package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllDoctors_ReturnsListOfDoctors() throws Exception {
        List<Doctor> doctors = Arrays.asList(
                new Doctor("Manuel", "Valera", 35, "manuelvalera@hotmail.com"),
                new Doctor("Antonio", "Rodriguez", 40, "example@example.com")
        );
     Mockito.when(doctorRepository.findAll()).thenReturn(doctors);

    mockMvc.perform(get("/api/doctors")
                   .contentType(MediaType.APPLICATION_JSON))
    .andExpect(MockMvcResultMatchers.status().isOk())
    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    void getAllDoctors_ReturnsNotFoundDoctors() throws Exception {
        Mockito.when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getDoctorById_ReturnsDoctorById() throws Exception {
        Doctor doctor = new Doctor("Manuel", "Valera", 35, "manuelvalera@hotmail.com");
        long doctorId = doctor.getId();

        Mockito.when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/{id}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is((int) doctorId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Manuel")));
    }

    @Test
    void getDoctorById_ReturnsNotFoundDoctorById() throws Exception {
        long doctorId = 99L;
        Mockito.when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/{id}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createDoctor_CreatesNewDoctor() throws Exception {
        Doctor doctor = new Doctor("Manuel", "Valera", 35, "manuelvalera@hotmail.com");
        String requestBody = objectMapper.writeValueAsString(doctor);
        Mockito.when(doctorRepository.save(Mockito.any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Manuel")));
    }

    @Test
    void deleteDoctor_DeletesExistingDoctor() throws Exception {
        Doctor doctor = new Doctor("Manuel", "Valera", 35, "manuelvalera@hotmail.com");
        long doctorId = doctor.getId();
        Mockito.when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(new Doctor()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctors/{id}", doctorId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteDoctor_ReturnsNotFoundForNonExistingDoctor() throws Exception {
        long nonExistingDoctorId = 99999;
        Mockito.when(doctorRepository.findById(nonExistingDoctorId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctors/{id}", nonExistingDoctorId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteAllDoctors_ReturnsNonExistingDoctors() throws Exception {
        Mockito.when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctors"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPatients_ReturnsListOfPatients() throws Exception {
        List<Patient> patients = Arrays.asList(
                new Patient("Manuel", "Valera", 35, "manuelvalera@hotmail.com"),
                new Patient("Antonio", "Rodriguez", 40, "example@example.com")
        );
        Mockito.when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    void getAllPatients_ReturnsNotFoundPatients() throws Exception {
        Mockito.when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void getPatientById_ReturnsPatientById() throws Exception {
        Patient patient = new Patient("Manuel", "Valera", 35, "manuelvalera@hotmail.com");
        long patientId = patient.getId();

        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is((int) patientId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Manuel")));
    }

    @Test
    void getPatientById_ReturnsNotFoundPatientById() throws Exception {
        long patientId = 99L;
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createPatient_CreatesNewPatient() throws Exception {
        Patient patient = new Patient("Manuel", "Valera", 35, "manuelvalera@hotmail.com");
        String requestBody = objectMapper.writeValueAsString(patient);
        Mockito.when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is("Manuel")));
    }

    @Test
    void deletePatient_DeletesExistingPatient() throws Exception {
        Patient patient = new Patient("Manuel", "Valera", 35, "manuelvalera@hotmail.com");
        long patientId = patient.getId();
        Mockito.when(patientRepository.findById(patientId)).thenReturn(Optional.of(new Patient()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/{id}", patientId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deletePatient_ReturnsNotFoundForNonExistingPatient() throws Exception {
        long nonExistingDoctorId = 99999;
        Mockito.when(patientRepository.findById(nonExistingDoctorId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients/{id}", nonExistingDoctorId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteAllPatients_ReturnsNonExistingPatients() throws Exception {

        Mockito.when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patients"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRooms_ReturnsListOfRooms() throws Exception {
        List<Room> rooms = Arrays.asList(
                new Room("Dermatology"),
                new Room("Ophthalmology")
        );
        Mockito.when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    void getAllRooms_ReturnsNotFoundRooms() throws Exception {
        Mockito.when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getRoomByName_ReturnsRoomByName() throws Exception {
        Room room = new Room("Dermatology");
        String roomName = room.getRoomName();
        Mockito.when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.of(room));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms/{roomName}", roomName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomName", Matchers.is(roomName)));
    }

    @Test
    void getRoomByName_ReturnsNotFoundRoomByName() throws Exception {
        String roomName = "Ophthalmology";
        Mockito.when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/room/{roomName}", roomName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void createRoom_CreatesNewRoom() throws Exception {
        Room room = new Room("Dermatology");
        String requestBody = objectMapper.writeValueAsString(room);
        Mockito.when(roomRepository.save(Mockito.any(Room.class))).thenReturn(room);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roomName", Matchers.is("Dermatology")));
    }

    @Test
    void deleteRoom_DeletesExistingRoom() throws Exception {
        Room room = new Room("Dermatology");
        String roomName = room.getRoomName();
        Mockito.when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.of(new Room()));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms/{roomName}", roomName))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteRoom_ReturnsNotFoundForNonExistingRoom() throws Exception {
        String nonExistingRoomName = "Ophthalmology";
        Mockito.when(roomRepository.findByRoomName(nonExistingRoomName)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms/{roomName}", nonExistingRoomName))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteAllRooms_ReturnsNonExistingRooms() throws Exception {

        Mockito.when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rooms"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}



package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

    @Test
    void doctorConstructorShouldSetValues() {
        Doctor d1 = new Doctor("Manuel", "Valera", 35, "manuelvalera@example.com");

        assertThat(d1.getFirstName()).isEqualTo("Manuel");
        assertThat(d1.getLastName()).isEqualTo("Valera");
        assertThat(d1.getAge()).isEqualTo(35);
        assertThat(d1.getEmail()).isEqualTo("manuelvalera@example.com");
    }

    @Test
    void patientConstructorShouldSetValues() {
        Patient p1 = new Patient("Alice", "Smith", 28, "alice.smith@example.com");

        assertThat(p1.getFirstName()).isEqualTo("Alice");
        assertThat(p1.getLastName()).isEqualTo("Smith");
        assertThat(p1.getAge()).isEqualTo(28);
        assertThat(p1.getEmail()).isEqualTo("alice.smith@example.com");
    }

    @Test
    void setIdAndGetIdShouldWork() {
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        Appointment appointment = new Appointment();

        patient.setId(1L);
        doctor.setId(2L);
        appointment.setId(3L);

        assertThat(patient.getId()).isEqualTo(1L);
        assertThat(doctor.getId()).isEqualTo(2L);
        assertThat(appointment.getId()).isEqualTo(3L);
    }

    @Test
    void roomConstructorShouldSetValues() {
        Room r1 = new Room("Dermatology");

        assertThat(r1.getRoomName()).isEqualTo("Dermatology");
    }

    @Test
    void appointmentConstructorShouldSetValues() {
        // Crear objetos necesarios
        Patient patient = new Patient("Alice", "Smith", 28, "alice.smith@example.com");
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        Room room = new Room("Dermatology");

        LocalDateTime startsAt = LocalDateTime.now();
        LocalDateTime finishesAt = startsAt.plusHours(1);
        Appointment a1 = new Appointment(patient, doctor, room, startsAt, finishesAt);

        // Verificar valores
        assertThat(a1.getPatient()).isEqualTo(patient);
        assertThat(a1.getDoctor()).isEqualTo(doctor);
        assertThat(a1.getRoom()).isEqualTo(room);
        assertThat(a1.getStartsAt()).isEqualTo(startsAt);
        assertThat(a1.getFinishesAt()).isEqualTo(finishesAt);
    }

    @Test
    void overlapsShouldReturnTrueForOverlappingAppointments() {
        // Crear objetos necesarios
        Room room = new Room("Dermatology");
        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime finishesAt1 = startsAt1.plusHours(1);
        LocalDateTime startsAt2 = startsAt1.minusMinutes(30);
        LocalDateTime finishesAt2 = startsAt1.plusMinutes(30);

        Appointment a2 = new Appointment(new Patient(), new Doctor(), room, startsAt1, finishesAt1);
        Appointment a3 = new Appointment(new Patient(), new Doctor(), room, startsAt2, finishesAt2);

        // Verificar superposición
        assertThat(a2.overlaps(a3)).isTrue();
    }

    @Test
    void overlapsShouldReturnFalseForNonOverlappingAppointments() {
        // Crear objetos necesarios
        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Ophthalmology");
        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime finishesAt1 = startsAt1.plusHours(1);

        Appointment a2 = new Appointment(new Patient(), new Doctor(), room1, startsAt1, finishesAt1);
        Appointment a3 = new Appointment(new Patient(), new Doctor(), room2, startsAt1, finishesAt1);

        // Verificar que no haya superposición
        assertThat(a2.overlaps(a3)).isFalse();
    }

    @Test
    void overlapsShouldReturnFalseForNonOverlappingAppointmentsSameRoom() {
        // Crear objetos necesarios
        Room room1 = new Room("Dermatology");
        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime finishesAt1 = startsAt1.plusHours(1);
        LocalDateTime startsAt2 = startsAt1.plusHours(2);
        LocalDateTime finishesAt2 = startsAt2.plusHours(1);

        Appointment a2 = new Appointment(new Patient(), new Doctor(), room1, startsAt1, finishesAt1);
        Appointment a3 = new Appointment(new Patient(), new Doctor(), room1, startsAt2, finishesAt2);

        // Verificar que no haya superposición
        assertThat(a2.overlaps(a3)).isFalse();
    }

    @Test
    void overlapsShouldReturnTrueForOverlappingAppointmentsSameRoom() {
        // Crear objetos necesarios
        Room room1 = new Room("Dermatology");
        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime finishesAt1 = startsAt1.plusHours(1);

        Appointment a2 = new Appointment(new Patient(), new Doctor(), room1, startsAt1, finishesAt1);
        Appointment a3 = new Appointment(new Patient(), new Doctor(), room1, startsAt1, finishesAt1);

        // Verificar superposición
        assertThat(a2.overlaps(a3)).isTrue();
    }
}

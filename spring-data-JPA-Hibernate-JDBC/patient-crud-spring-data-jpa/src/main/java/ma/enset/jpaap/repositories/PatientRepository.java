package ma.enset.jpaap.repositories;

import ma.enset.jpaap.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PatientRepository extends JpaRepository<Patient, Long> {
}

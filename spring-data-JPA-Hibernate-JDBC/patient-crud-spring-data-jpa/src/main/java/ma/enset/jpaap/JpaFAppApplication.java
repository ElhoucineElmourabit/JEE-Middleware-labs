package ma.enset.jpaap;

import ma.enset.jpaap.entities.Patient;
import ma.enset.jpaap.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class JpaFAppApplication implements CommandLineRunner {

    @Autowired
    PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaFAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i=0; i<100;i++){
            patientRepository.save(
                    new Patient(null, "Hassan", new Date(), false, (int)Math.random()*100));
        }

        Page<Patient> patients = patientRepository.findAll(PageRequest.of(1,5));
        System.out.println("Total Pages : " + patients.getTotalPages());
        System.out.println("Total elements : " + patients.getTotalElements());
        System.out.println("Num Page : " + patients.getNumber());
        List <Patient> content = patients.getContent();

        content.forEach(p->{
            System.out.println("=====================");
            System.out.println(p.toString());
        });
        System.out.println("********************************");
        Patient patient = patientRepository.findById(1L).orElse(null);
        if(patient!=null){
            System.out.println(patient.toString());
        }
        patient.setScore(870);
        patientRepository.save(patient);
        System.out.println("--------------------------");
        System.out.println(patient.toString());
        System.out.println("--------------------------");
        patientRepository.deleteById(1L);
        System.out.println(patient.toString());

    }
}

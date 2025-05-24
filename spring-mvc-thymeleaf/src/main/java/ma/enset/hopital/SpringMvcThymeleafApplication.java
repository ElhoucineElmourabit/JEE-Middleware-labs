package ma.enset.hopital;

import ma.enset.hopital.entity.Patient;
import ma.enset.hopital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class SpringMvcThymeleafApplication {


	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringMvcThymeleafApplication.class, args);
	}

	@Bean
	CommandLineRunner start(PatientRepository patientRepository){
		return args->{
			patientRepository.save(new Patient(null, "Mohammed", new Date(), false, 34));
			patientRepository.save(new Patient(null, "Hanane", new Date(), false, 4321));
			patientRepository.save(new Patient(null, "Imane", new Date(), false, 34));
			patientRepository.save(new Patient(null, "Jean Dupont", new Date(), false, 120));
			patientRepository.save(new Patient(null, "Marie Martin", new Date(), true, 80));
			patientRepository.save(new Patient(null, "Pierre Durand", new Date(), false, 210));
			patientRepository.save(new Patient(null, "Sophie Lambert", new Date(), true, 95));
			patientRepository.save(new Patient(null, "Lucie Petit", new Date(), false, 150));
			patientRepository.save(new Patient(null, "Thomas Moreau", new Date(), true, 60));
			patientRepository.save(new Patient(null, "Amélie Roux", new Date(), false, 180));
			patientRepository.save(new Patient(null, "Nicolas Leroy", new Date(), true, 75));
			patientRepository.save(new Patient(null, "Sarah Bernard", new Date(), false, 200));
			patientRepository.save(new Patient(null, "David Michel", new Date(), true, 110));

		};
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}

package hu.waiter.blsz;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WaiterApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WaiterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}

}
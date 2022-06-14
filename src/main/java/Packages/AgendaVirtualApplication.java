package Packages;

import Packages.Entities.Contact;
import Packages.Repositories.ContactRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class AgendaVirtualApplication {

    /**
     * Aquí estoy utilizando un plugin/extensión de Thymeleaf para añadir la paginación al proyecto.
     */
    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AgendaVirtualApplication.class, args);
    }

}

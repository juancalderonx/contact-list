package Packages.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Contact {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    ¿Por qué no usar NotNull? Con NotBlank lo que hago es que necesita al menos 1 caracter y no puede ser un espacio.
     */
    @Column(name = "fullname")
    @NotBlank(message = "¡Ops! This field is required.")
    private String fullname;

    @Size(max = 15, message = "¡Ops! This field must have a maximum of 15 values.")
    @NotNull
    private String cellphone;

    /*
    Se puede usar la anotación @Email, sin embargo, a nivel de escalabilidad, lo mejor es usar la anotación
    @Pattern(regexp ="") Que básicamente con una expresión regular me permite añadir más validaciones para el correo.
     */
    @Email(message = "The email has an invalid format.")
    @NotNull
    private String email;

    /*
    Puedo usar esta o solo @Past | Significa que la fecha de nacimiento debe ser hoy o antes de hoy. No mañana.
    */
    @PastOrPresent(message = "¡Ops! Date cannot be in the future.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate birthday;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime lastUpdate;

}

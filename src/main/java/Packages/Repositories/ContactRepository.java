package Packages.Repositories;

import Packages.Entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    /*
    Este método me permite hacer el filtro de los nombres de los contactos. Es una consulta personalizada
    Lo que quiere decir containing es que ese texto que el usuario ingrese en el input del filtro debe estar
    contenido dentro del nombre del objeto contacto, sino no hará ningún filtro.
     */
    Page<Contact> findByFullnameContaining(String fullname, Pageable pageable);

}

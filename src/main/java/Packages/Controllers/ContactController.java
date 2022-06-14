package Packages.Controllers;

import Packages.Entities.Contact;
import Packages.Repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
public class ContactController {

    /***
     * ¿Qué hace esta clase?
     * A futuro, la idea es implementar en este proyecto
     *      * la opción de manejar usuarios y que dependiendo del usuario se muestre una u otra lista
     *      * de contactos.
     */

    @Autowired
    private ContactRepository repository;

    /**
     * Este método me retorna el index.html de mi proyecto. Este index lo que contiene es la lista de contactos de
     * la persona. El parámetro Pageable es para poder usar la paginación en nuestra web, ya que antes de usar una Page
     * estábamos usando una List, devolvíamos una lista a la página web pero ahora es paginación. Por otro lado, la
     * anotación @PageableDefault me permite configurar la paginación misma. Atributos como sort son para ordenar en
     * base a un atributo del contacto, el atributo direction es para indicar el tipo de ordenación, si es ascendente,
     * descendente, etc.
     */
    @GetMapping
    public String index(
            @PageableDefault(sort = "fullname", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String searchName,
            /*
            Este requestParam lo que hace es obtener el nombre a filtrar escrito por el usuario, sin embargo,
            si el usuario no ingresa nada no es obligatorio el atributo, para eso uso el atributo required=false
            dentro de la anotación @RequestParam
             */
            Model model) {

        Page<Contact> contactsPage;

        if(searchName != null && searchName.trim().length() > 0){
            contactsPage = repository.findByFullnameContaining(searchName, pageable);
        } else {
            contactsPage = repository.findAll(pageable);
        }
        model.addAttribute("contactsPage",contactsPage);
        return "index";
    }

    /**
     * Este método me retorna la página "createContact" que básicamente es un formulario
     * para poder crear contactos nuevos.
     */
    @GetMapping("/createContact")
    public String createContactForm(Model model){
        model.addAttribute("contact", new Contact());
        //La idea de usar un new Contact es para poder acceder a los atributos de la clase Contact.
        return "form";
    }

    /**
     * Este método obtiene los valores que enviamos mediante el formulario que contiene un atributo de método
     * con el valor "post" para crear un contacto. Entonces, esos datos enviados desde el formulario creado
     * en HTML llegan aquí como un Contact para luego ser tratados.
     */
    @PostMapping("/createContact")
    public String create(@Validated Contact contact, BindingResult bindingResult,RedirectAttributes ra, Model model){

        //Captura de errores
        if(bindingResult.hasErrors()){
            model.addAttribute("contact", contact);
            List<ObjectError> errores = bindingResult.getAllErrors();
            for (ObjectError oe : errores) {
                System.out.println(oe);
            }
            return "form";
        }

        //Actualizo la última modificación de ese contacto.
        contact.setLastUpdate(LocalDateTime.now(ZoneId.systemDefault()));
        repository.save(contact);
        ra.addFlashAttribute("msgExito",
               "The contact " + contact.getFullname() + " has been successfully created!");
        return "redirect:/";
    }

    /**
     * Este método le estamos enviando un id de un contacto y en base a ese id retornamos la plantilla HTML de
     * creación de un contacto (modificado en HTML para que muestre que es editar) que vendrá ya cargada con los datos
     * del contacto con dicho ID.
     */
    @GetMapping("/{id}/edit")
    public String editContactForm(@PathVariable Long id ,Model model){
        Contact contact = repository.getById(id);
        model.addAttribute("contact",contact);
        return "form";
    }

    /**
     * Este método es para editar un contacto. Lo que hacemos aquí es capturar los nuevos valores del contacto una vez
     * sean editados.
     */
    @PostMapping("/{id}/edit")
    public String updateContact(@PathVariable Long id, @Validated Contact contact,
                                BindingResult bindingResult,RedirectAttributes ra, Model model){

        //Validación de errores
        if(bindingResult.hasErrors()){
            model.addAttribute("contact", contact);

            List<ObjectError> errores = bindingResult.getAllErrors();
            for (ObjectError oe : errores) {
                System.out.println(oe);
            }
            return "form";
        }

        Contact contactDB = repository.getById(id);
        contact.setLastUpdate(LocalDateTime.now());
        contactDB.setLastUpdate(contact.getLastUpdate());
        contactDB.setFullname(contact.getFullname());
        contactDB.setCellphone(contact.getCellphone());
        contactDB.setEmail(contact.getEmail());
        contactDB.setBirthday(contact.getBirthday());
        repository.save(contact);

        ra.addFlashAttribute("msgExito",
                "The contact " + contact.getFullname() + " has been successfully updated!");
        return "redirect:/";
    }

    /**
     * Método para eliminar un contacto. Lo que sucede aquí es que básicamente en base recibimos un ID y en base
     * a ese ID lo eliminamos. Simplemente, al presionar el botón, se capta el id del contacto y es el id que usamos
     * para buscarlo dentro del programa y así borrarlo.
     */
    @PostMapping("/{id}/delete")
    public String deleteContact(@PathVariable Long id,RedirectAttributes ra){

        Contact contactDB = repository.getById(id);
        repository.delete(contactDB);

        ra.addFlashAttribute("msgExito",
                "The contact " +contactDB.getFullname() + " has been successfully deleted!");

        return "redirect:/";
    }

}

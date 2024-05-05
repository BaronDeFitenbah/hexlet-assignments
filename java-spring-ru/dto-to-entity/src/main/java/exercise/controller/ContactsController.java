package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    // BEGIN
    @PostMapping(path = "")
    public ResponseEntity<ContactDTO> create(@RequestBody ContactCreateDTO conctactData) {
        var contact = new Contact();
        contact.setPhone(conctactData.getPhone());
        contact.setFirstName(conctactData.getFirstName());
        contact.setLastName(conctactData.getLastName());
        contactRepository.save(contact);

        var contactDto = new ContactDTO();
        contactDto.setCreatedAt(contact.getCreatedAt());
        contactDto.setUpdatedAt(contact.getUpdatedAt());
        contactDto.setId(contact.getId());
        contactDto.setPhone(contact.getPhone());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setLastName(contact.getLastName());
        return new ResponseEntity<>(contactDto, HttpStatus.CREATED);
    }
    // END
}

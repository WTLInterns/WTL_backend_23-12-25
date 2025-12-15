package com.workshop.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.Contact;
import com.workshop.Service.ContactService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/contacts")
public class ContactController {
 
    @Autowired
    private ContactService contactService;

    @PostMapping("/create-contact")
   public Contact createContact(@RequestBody Contact contact){
        return this.contactService.saveContact(contact);
   }

   @GetMapping("/getAllContacts")
   public List<Contact> getAllContacts(){
        return this.contactService.getAllContacts();
   }

   @DeleteMapping("/deleteContact/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Integer id) {
        try {
            this.contactService.deleteContact(id);
            return ResponseEntity.ok()
                .body(Map.of("success", true, "message", "Contact deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Bad Request", "message", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
 .body(Map.of("error", "Not Found", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }


}
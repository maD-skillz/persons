package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.dto.PersonDTO;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService persons;

    private BCryptPasswordEncoder encoder;

    @GetMapping("/all")
    @Validated(Person.class)
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    @Validated(Person.class)
    public Person findById(@Valid @PathVariable int id) {
        return persons.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Person is not found. Please, check login is correct."
        ));
    }

    @PostMapping("/create")
    @Validated(Person.class)
    public Person create(
            @Valid @RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
           throw new NullPointerException("Login or password should not be empty!");
        }
        if (person.getLogin().length() < 3 || person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return persons.save(person).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.CONFLICT, "Person with this login is already exist!"
        ));
    }

    @PutMapping("/")
    @Validated(Person.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw  new NullPointerException("Login or password should not be empty!");
        }
        if (person.getLogin().length() < 3 || person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        if (persons.update(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @Validated(Person.class)
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (persons.delete(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    @Validated(PersonDTO.class)
    public ResponseEntity<Void> updatePartial(@Valid @RequestBody PersonDTO personDTO) {
        Optional<Person> optionalPerson = persons.findById(personDTO.getId());
        if (optionalPerson.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Person is not found. Please, check login is correct."
            );
        }
        Person person = optionalPerson.get();
        if (personDTO.getPassword() != null) {
            person.setPassword(encoder.encode(personDTO.getPassword()));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}

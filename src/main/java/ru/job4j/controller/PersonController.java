package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService persons;

    private BCryptPasswordEncoder encoder;

    @GetMapping("/all")
    public List<Person> findAll() {
        return this.persons.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = this.persons.findById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Person> create(
            @RequestBody Person person) throws ConstraintViolationException {
        if (person.getLogin() == null || person.getPassword() == null) {
           throw new NullPointerException("Login or password should not be empty!");
        }
        if (person.getLogin().length() < 3 || person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        var optionalPerson = persons.save(person);
        return new ResponseEntity<Person>(
                optionalPerson.isPresent() ? HttpStatus.OK : HttpStatus.CONFLICT
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
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
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        if (persons.delete(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}

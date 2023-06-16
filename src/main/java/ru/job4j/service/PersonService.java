package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Optional<Person> save(Person person) throws ConstraintViolationException {
        try {
            repository.save(person);
        } catch (ConstraintViolationException e) {
            return Optional.empty();
        }
        return Optional.of(person);
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> optionalPerson = repository.findById(id);
        return optionalPerson.isEmpty() ? Optional.empty() : optionalPerson;
    }

    public boolean update(Person person) {
        Optional<Person> personById = findById(person.getId());
        if (personById.isEmpty()) {
            return false;
        }
        repository.save(person);
        return true;
    }

    public boolean delete(Person person) {
        Optional<Person> personById = findById(person.getId());
        if (personById.isPresent()) {
            repository.delete(person);
            return true;
        }
        return false;
    }
}

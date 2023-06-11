package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Person save(Person person) {
        return repository.save(person);
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> optionalPerson = repository.findById(id);
        return optionalPerson.isEmpty() ? Optional.empty() : optionalPerson;
    }

    public void update(Person person) {
        repository.save(person);
    }

    public void delete(Person person) {
        repository.delete(person);
    }
}

package ru.job4j.url;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Person> person = personService.findPersonByLogin(login);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        return new User(person.get().getLogin(), person.get().getPassword(), emptyList());
    }
}

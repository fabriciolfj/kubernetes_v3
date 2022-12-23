package com.github.fabriciolfj.personservice.service;

import com.github.fabriciolfj.personservice.entity.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {

    public List<Person> findAll() {
        int index = 0;
        var persons = new ArrayList<Person>();
        while (index < 10) {
            persons.add(Person.builder().code(UUID.randomUUID().toString())
                    .name("teste " + index).build());
            index++;
        }

        return persons;
    }
}

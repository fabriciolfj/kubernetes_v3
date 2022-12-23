package com.github.fabriciolfj.personservice.controller;

import com.github.fabriciolfj.personservice.entity.Person;
import com.github.fabriciolfj.personservice.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Person> getAll() {
        return personService.findAll();
    }
}

package com.dqmdz.service;

import com.dqmdz.model.Persona;
import com.dqmdz.repository.PersonaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonaService {

    @Inject
    PersonaRepository personaRepository;

    public List<Persona> findAll() {
        return personaRepository.listAll();
    }

    public Optional<Persona> findById(Long id) {
        return personaRepository.findByIdOptional(id);
    }

    public Persona create(Persona persona) {
        personaRepository.persist(persona);
        return persona;
    }

    public Persona update(Long id, Persona updatedPersona) {
        Optional<Persona> personaOptional = personaRepository.findByIdOptional(id);
        if (personaOptional.isPresent()) {
            Persona persona = personaOptional.get();
            persona.nombre = updatedPersona.nombre;
            persona.apellido = updatedPersona.apellido;
            persona.fechaNacimiento = updatedPersona.fechaNacimiento;
            personaRepository.persist(persona);
            return persona;
        }
        return null;
    }

    public boolean deleteById(Long id) {
        return personaRepository.deleteById(id);
    }

}

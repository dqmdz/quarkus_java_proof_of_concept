package com.dqmdz.repository;

import com.dqmdz.model.Persona;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonaRepository implements PanacheRepository<Persona> {
}

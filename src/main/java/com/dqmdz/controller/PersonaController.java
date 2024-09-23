package com.dqmdz.controller;

import com.dqmdz.model.Persona;
import com.dqmdz.service.PersonaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Path("/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonaController.class);

    @Inject
    PersonaService personaService;

    @GET
    public List<Persona> findAll() {
        LOGGER.debug("Obteniendo todas las personas");
        List<Persona> personas = personaService.findAll();
        try {
            LOGGER.debug("Se encontraron {} personas. Personas {}", personas.size(), JsonMapper.builder().findAndAddModules().build().writeValueAsString(personas));
        } catch (JsonProcessingException e) {
            LOGGER.debug("Personas error {}", e.getMessage());
        }
        return personas;
    }

    @GET
    @Path("/{id}")
    public Response getPersonaById(@PathParam("id") Long id) {
        LOGGER.debug("Buscando persona con id: {}", id);
        Optional<Persona> persona = personaService.findById(id);
        if (persona.isPresent()) {
            try {
                LOGGER.debug("Persona encontrada: {}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(persona.get()));
            } catch (JsonProcessingException e) {
                LOGGER.debug("Persona error {}", e.getMessage());
            }
            return Response.ok(persona.get()).build();
        } else {
            LOGGER.debug("No se encontró persona con id: {}", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Transactional
    public Response createPersona(Persona persona) {
        try {
            LOGGER.debug("Creando nueva persona: {}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(persona));
        } catch (JsonProcessingException e) {
            LOGGER.debug("Persona error {}", e.getMessage());
        }
        Persona createdPersona = personaService.create(persona);
        try {
            LOGGER.debug("Persona creada: {}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(createdPersona));
        } catch (JsonProcessingException e) {
            LOGGER.debug("Persona error {}", e.getMessage());
        }
        return Response.status(Response.Status.CREATED).entity(createdPersona).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePersona(@PathParam("id") Long id, Persona updatedPersona) {
        try {
            LOGGER.debug("Actualizando persona con id: {}. Nuevos datos: {}", id, JsonMapper.builder().findAndAddModules().build().writeValueAsString(updatedPersona));
        } catch (JsonProcessingException e) {
            LOGGER.debug("Persona error {}", e.getMessage());
        }
        Persona persona = personaService.update(id, updatedPersona);
        if (persona == null) {
            LOGGER.debug("No se encontró persona con id: {} para actualizar", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            LOGGER.debug("Persona actualizada: {}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(persona));
        } catch (JsonProcessingException e) {
            LOGGER.debug("Persona error {}", e.getMessage());
        }
        return Response.ok(persona).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePersona(@PathParam("id") Long id) {
        LOGGER.debug("Eliminando persona con id: {}", id);
        boolean deleted = personaService.deleteById(id);
        if (deleted) {
            LOGGER.debug("Persona con id: {} eliminada exitosamente", id);
            return Response.noContent().build();
        }
        LOGGER.debug("No se encontró persona con id: {} para eliminar", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
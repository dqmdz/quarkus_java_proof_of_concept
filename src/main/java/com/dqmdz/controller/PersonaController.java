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

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaController {

    private static final Logger LOGGER = Logger.getLogger(PersonaController.class.getName());

    @Inject
    PersonaService personaService;

    @GET
    public List<Persona> findAll() {
        LOGGER.log(Level.FINE, "Obteniendo todas las personas");
        List<Persona> personas = personaService.findAll();
        try {
            LOGGER.log(Level.FINE, "Se encontraron {0} personas. Personas {1}", new Object[]{personas.size(), JsonMapper.builder().findAndAddModules().build().writeValueAsString(personas)});
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Personas error {0}", e.getMessage());
        }
        return personas;
    }

    @GET
    @Path("/{id}")
    public Response getPersonaById(@PathParam("id") Long id) {
        LOGGER.log(Level.FINE, "Buscando persona con id: {0}", id);
        Optional<Persona> persona = personaService.findById(id);
        if (persona.isPresent()) {
            try {
                LOGGER.log(Level.FINE, "Persona encontrada: {0}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(persona.get()));
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "Persona error {0}", e.getMessage());
            }
            return Response.ok(persona.get()).build();
        } else {
            LOGGER.log(Level.FINE, "No se encontró persona con id: {0}", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Transactional
    public Response createPersona(Persona persona) {
        try {
            LOGGER.log(Level.FINE, "Creando nueva persona: {0}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(persona));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Persona error {0}", e.getMessage());
        }
        Persona createdPersona = personaService.create(persona);
        try {
            LOGGER.log(Level.FINE, "Persona creada: {0}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(createdPersona));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Persona error {0}", e.getMessage());
        }
        return Response.status(Response.Status.CREATED).entity(createdPersona).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePersona(@PathParam("id") Long id, Persona updatedPersona) {
        try {
            LOGGER.log(Level.FINE, "Actualizando persona con id: {0}. Nuevos datos: {1}", new Object[]{id, JsonMapper.builder().findAndAddModules().build().writeValueAsString(updatedPersona)});
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Persona error {0}", e.getMessage());
        }
        Persona persona = personaService.update(id, updatedPersona);
        if (persona == null) {
            LOGGER.log(Level.FINE, "No se encontró persona con id: {0} para actualizar", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            LOGGER.log(Level.FINE, "Persona actualizada: {0}", JsonMapper.builder().findAndAddModules().build().writeValueAsString(persona));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Persona error {0}", e.getMessage());
        }
        return Response.ok(persona).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePersona(@PathParam("id") Long id) {
        LOGGER.log(Level.FINE, "Eliminando persona con id: {0}", id);
        boolean deleted = personaService.deleteById(id);
        if (deleted) {
            LOGGER.log(Level.FINE, "Persona con id: {0} eliminada exitosamente", id);
            return Response.noContent().build();
        }
        LOGGER.log(Level.FINE, "No se encontró persona con id: {0} para eliminar", id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

package com.dqmdz.controller;

import com.dqmdz.model.Persona;
import com.dqmdz.service.PersonaService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/personas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonaController {

    @Inject
    PersonaService personaService;

    @GET
    public List<Persona> findAll() {
        return personaService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getPersonaById(@PathParam("id") Long id) {
        Optional<Persona> persona = personaService.findById(id);
        return persona.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Transactional
    public Response createPersona(Persona persona) {
        Persona createdPersona = personaService.create(persona);
        return Response.status(Response.Status.CREATED).entity(createdPersona).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePersona(@PathParam("id") Long id, Persona updatedPersona) {
        Persona persona = personaService.update(id, updatedPersona);
        if (persona == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(persona).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePersona(@PathParam("id") Long id) {
        boolean deleted = personaService.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}

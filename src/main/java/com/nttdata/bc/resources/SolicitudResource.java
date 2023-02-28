package com.nttdata.bc.resources;

import org.bson.types.ObjectId;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse.StatusCode;

import com.nttdata.bc.documents.Solicitud;
import com.nttdata.bc.services.ISolicitudService;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/solicitudes")
public class SolicitudResource {
    @Inject
    Logger LOGGER;

    @Inject
    ISolicitudService service;

    @POST
    @ResponseStatus(StatusCode.OK)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Solicitud> insert(@Valid Solicitud obj) {
        return this.service.insert(obj);
    }

    @PUT
    @Path("/{id}")
    @ResponseStatus(StatusCode.CREATED)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Solicitud> update(@PathParam("id") ObjectId id, @Valid Solicitud obj) {
        obj.setId(id);
        return this.service.update(obj);
    }

    @POST
    @Path("/{id}/usuario-acepta/{idUsuarioAceptante}")
    @ResponseStatus(StatusCode.OK)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Solicitud> acceptRequest(ObjectId id, ObjectId idUsuarioAceptante) {
        return this.service.acceptRequest(id, idUsuarioAceptante);
    }

    @GET
    @Path("/")
    @ResponseStatus(StatusCode.OK)
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Solicitud> listAll() {
        return this.service.listAll();
    }

    @GET
    @Path("/{id}")
    @ResponseStatus(StatusCode.OK)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Solicitud> findById(@PathParam("id") ObjectId id) {
        return this.service.findById(id);
    }

    @DELETE
    @Path("/{id}")
    @ResponseStatus(StatusCode.NO_CONTENT)
    public Uni<Void> deleteById(@PathParam("id") ObjectId id) {
        return this.service.deleteById(id);
    }
}

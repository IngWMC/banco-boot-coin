package com.nttdata.bc.clients;

import java.time.LocalDate;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.nttdata.bc.models.TipoCambio;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/tipos-cambio")
@RegisterRestClient
public interface ITipoCambioRestClient {

    @GET
    @Path("/fecha/{fecha}")
    Uni<TipoCambio> findByDate(@PathParam("fecha") LocalDate fecha);

}

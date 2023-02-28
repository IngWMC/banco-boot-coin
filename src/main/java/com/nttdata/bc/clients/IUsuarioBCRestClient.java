package com.nttdata.bc.clients;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.nttdata.bc.models.Usuario;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/usuarios")
@RegisterRestClient
public interface IUsuarioBCRestClient {

    @GET
    @Path("/{id}")
    Uni<Usuario> findById(@PathParam("id") ObjectId id);

}

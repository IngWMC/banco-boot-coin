package com.nttdata.bc.exceptions;

import java.time.LocalDateTime;

import com.mongodb.MongoSocketReadException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InternalServerMongoIPMapper implements ExceptionMapper<MongoSocketReadException> {

    @Override
    public Response toResponse(MongoSocketReadException ex) {
        ExceptionResponse er = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(er)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}

package com.nttdata.bc.repositories;

import com.nttdata.bc.documents.Solicitud;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SolicitudRepository implements ReactivePanacheMongoRepository<Solicitud> {

}

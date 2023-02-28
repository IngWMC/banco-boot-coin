package com.nttdata.bc.services;

import org.bson.types.ObjectId;

import com.nttdata.bc.documents.Solicitud;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface ISolicitudService {
    Uni<Solicitud> insert(Solicitud obj);

    Uni<Solicitud> update(Solicitud obj);

    Uni<Solicitud> acceptRequest(ObjectId id, ObjectId idUsuarioAceptante);

    Multi<Solicitud> listAll();

    Uni<Solicitud> findById(ObjectId id);

    Uni<Void> deleteById(ObjectId id);
}

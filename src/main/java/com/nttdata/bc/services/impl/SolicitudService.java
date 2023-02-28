package com.nttdata.bc.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.nttdata.bc.clients.ITipoCambioRestClient;
import com.nttdata.bc.clients.IUsuarioBCRestClient;
import com.nttdata.bc.documents.Solicitud;
import com.nttdata.bc.exceptions.BadRequestException;
import com.nttdata.bc.exceptions.NotFoundException;
import com.nttdata.bc.repositories.SolicitudRepository;
import com.nttdata.bc.services.ISolicitudService;

import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SolicitudService implements ISolicitudService {
    @Inject
    Logger LOGGER;

    @Inject
    SolicitudRepository repository;

    @Inject
    @RestClient
    IUsuarioBCRestClient usuarioBCRestClient;

    @Inject
    @RestClient
    ITipoCambioRestClient tipoCambioRestClient;

    @Override
    public Uni<Solicitud> insert(Solicitud obj) {
        return this.usuarioBCRestClient.findById(obj.getIdUsuarioSolicitante())
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("El usuario con el id: " + obj.getId() + ", no existe."))
                .flatMap((usuario) -> this.tipoCambioRestClient.findByDate(LocalDate.now())
                        .onItem()
                        .ifNull()
                        .failWith(() -> new BadRequestException("No existe tipo de cambio para la fecha de hoy.")))
                .flatMap((tipoCambio) -> {
                    BigDecimal monto = tipoCambio.getVenta().multiply(new BigDecimal(obj.getCantidadBootCoin()));

                    obj.setMonto(monto);
                    obj.setEsAceptado(false);
                    obj.setEsActivo(true);
                    obj.setFechaCreacion(LocalDateTime.now());

                    return this.repository.persist(obj);
                });
    }

    @Override
    public Uni<Solicitud> update(Solicitud obj) {
        return this.repository.list("id", obj.getId())
                .onItem()
                .<Solicitud>disjoint()
                .toUni()
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("La solicitud con el id: " + obj.getId() + ", no existe."))
                .toMulti()
                .select()
                .where(solicitud -> solicitud.getEsAceptado().equals(false))
                .toUni()
                .onItem()
                .ifNull()
                .failWith(
                        () -> new BadRequestException("La solicitud con el id: " + obj.getId() + ", ya está aceptado."))
                .flatMap((solicitud) -> {
                    obj.setFechaModificacion(LocalDateTime.now());
                    return this.repository.update(obj);
                });
    }

    @Override
    public Uni<Solicitud> acceptRequest(ObjectId id, ObjectId idUsuarioAceptante) {
        return this.usuarioBCRestClient.findById(idUsuarioAceptante)
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("El usuario con el id: " + idUsuarioAceptante + ", no existe."))
                .flatMap((usuario) -> this.repository.findById(id)
                        .onItem()
                        .ifNull()
                        .failWith(() -> new NotFoundException("La solicitud con el id: " + id + ", no existe."))
                        .toMulti()
                        .select()
                        .where(solicitud -> solicitud.getIdUsuarioSolicitante().equals(idUsuarioAceptante))
                        .toUni()
                        .onItem()
                        .ifNotNull()
                        .failWith(() -> new BadRequestException("La solicitud te pertenece, no puedes aceptar."))
                        .toMulti()
                        .select()
                        .where(solicitud -> solicitud.getEsAceptado().equals(true))
                        .toUni()
                        .onItem()
                        .ifNotNull()
                        .failWith(
                                () -> new BadRequestException("La solicitud con el id: " + id + ", ya está aceptado."))
                        .flatMap((solicitud) -> this.repository.findById(id)))
                .flatMap((solicitud) -> {
                    LOGGER.info("solicitud ::: " + id.toString() + " ::: " + solicitud);
                    solicitud.setNumeroTrasaccion(this.generarNumeroTransaccion());
                    solicitud.setIdUsuarioAceptante(idUsuarioAceptante);
                    solicitud.setEsAceptado(true);
                    solicitud.setFechaModificacion(LocalDateTime.now());

                    return this.repository.update(solicitud);
                });
    }

    @Override
    public Multi<Solicitud> listAll() {
        return this.repository.listAll(Sort.descending("fechaCreacion"))
                .onItem()
                .<Solicitud>disjoint()
                .map(solicitud -> solicitud);
    }

    @Override
    public Uni<Solicitud> findById(ObjectId id) {
        return this.repository.findById(id);
    }

    @Override
    public Uni<Void> deleteById(ObjectId id) {
        return this.repository.list("id", id)
                .onItem()
                .<Solicitud>disjoint()
                .toUni()
                .onItem()
                .ifNull()
                .failWith(() -> new NotFoundException("La solicitud con el id: " + id + ", no existe."))
                .toMulti()
                .select()
                .where(solicitud -> solicitud.getEsAceptado().equals(false))
                .toUni()
                .onItem()
                .ifNull()
                .failWith(() -> new BadRequestException("La solicitud con el id: " + id + ", ya está aceptado."))
                .flatMap((solicitud) -> {
                    solicitud.setEsActivo(false);
                    solicitud.setFechaModificacion(LocalDateTime.now());
                    return this.repository.update(solicitud);
                })
                .replaceWithVoid();

    }

    private String generarNumeroTransaccion() {
        return UUID.randomUUID().toString();
    }

}

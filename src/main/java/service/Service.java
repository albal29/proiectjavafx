package service;

import domain.Entity;

public interface Service<ID,E> {


    E findOne(ID id);

    Iterable<E> findAll();

    E save(E entity);

    E delete(ID id );

    E update(E entity);



}

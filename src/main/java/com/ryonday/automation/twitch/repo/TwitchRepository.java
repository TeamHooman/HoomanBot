package com.ryonday.automation.twitch.repo;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
interface TwitchRepository<T, ID extends Serializable> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> save(Iterable<S> entities);

    Optional<T> findOne(ID id);

    boolean exists(ID id);

    Optional<Iterable<T>> findAll();

    Optional<Iterable<T>> findAll(Iterable<ID> ids);

    long count();

    void delete(ID id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll();
}

package com.teamhooman.hoomanbot.twitch.repo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
@Cacheable(cacheNames = "twitch")
interface TwitchRepository<T, ID extends Serializable> extends Repository<T, ID> {

    @CacheEvict(cacheNames = "twitch", allEntries = true)
    <S extends T> S save(S entity);

    @CacheEvict(cacheNames = "twitch", allEntries = true)
    <S extends T> Iterable<S> save(Iterable<S> entities);

    @Cacheable(cacheNames = "twitch")
    Optional<T> findOne(ID id);

    boolean exists(ID id);

    @Cacheable(cacheNames = "twitch")
    Optional<Iterable<T>> findAll();

    @Cacheable(cacheNames = "twitch")
    Optional<Iterable<T>> findAll(Iterable<ID> ids);

    long count();

    @CacheEvict(cacheNames = "twitch", allEntries = true)
    void delete(ID id);

    @CacheEvict(cacheNames = "twitch", allEntries = true)
    void delete(T entity);

    @CacheEvict(cacheNames = "twitch", allEntries = true)
    void delete(Iterable<? extends T> entities);

    @CacheEvict(cacheNames = "twitch", allEntries = true)
    void deleteAll();
}

package org.example.ejb.repositories.impl;

import org.example.ejb.repositories.EntityRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

abstract class AbstractJPARepository<TypeOfEntity, TypeOfFieldId>
        implements EntityRepository<TypeOfEntity, TypeOfFieldId> {

    @PersistenceContext(unitName = "org.example.myphotos.pu")
    protected EntityManager entityManager;

    protected abstract Class<TypeOfEntity> getEntityClass();

    @Override
    public TypeOfEntity getProxyInstance(TypeOfFieldId id) {
        return entityManager.getReference(this.getEntityClass(), id);
    }

    @Override
    public Optional<TypeOfEntity> findById(TypeOfFieldId id) {
        return Optional.ofNullable(entityManager.find(this.getEntityClass(), id));
    }

    @Override
    public void create(TypeOfEntity entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(TypeOfEntity entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(TypeOfEntity entity) {
        entityManager.remove(entity);
    }

    @Override
    public void flash() {
        entityManager.flush();
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}

package org.example.ejb.repositories;

import java.util.Optional;

public interface EntityRepository<TypeOfEntity, TypeOfFieldId> {

    TypeOfEntity getProxyInstance(TypeOfFieldId id);

    Optional<TypeOfEntity> findById(TypeOfFieldId id);

    void create(TypeOfEntity entity);

    void update(TypeOfEntity entity);

    void delete(TypeOfEntity entity);

    void flash();
}

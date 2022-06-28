package org.lemanoman.minhascontas.service;

import org.lemanoman.minhascontas.model.DefaultModel;

import java.util.List;

public interface GenericServiceInterface<T extends DefaultModel> {
   List<T> listAll();

   T getById(Long id);


   T merge(T databaseObject, T editedObject);

   T saveOrUpdate(T domainObject);

   void delete(Long id);
}

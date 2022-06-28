package org.lemanoman.minhascontas.service;

import org.lemanoman.minhascontas.model.DefaultModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class GenericCrudService<T extends JpaRepository, Y extends DefaultModel> implements GenericServiceInterface<Y>{

   private T repository;
   public GenericCrudService(T repository){
      this.repository = repository;
   }

   @Override
   public List<Y> listAll() {
      return repository.findAll();
   }

   @Override
   public Y getById(Long id) {
      return (Y)repository.getOne(id);
   }

   @Override
   public Y merge(Y rootObject, Y anotherObject) {
      return rootObject;
   }


   @Override
   public Y saveOrUpdate(Y domainObject) {
      if(domainObject.getId()==null){
         return (Y)repository.saveAndFlush(domainObject);
      }
      Optional<Y> optionalObject = repository.findById(domainObject.getId());
      if(optionalObject.isPresent()){
         final Y tmpObject = merge(optionalObject.get(), domainObject);
         return (Y)repository.saveAndFlush(tmpObject);
      }
      domainObject.setId(null);
      return (Y)repository.saveAndFlush(domainObject);
   }

   @Override
   public void delete(Long id) {
      Optional<Y> object = repository.findById(id);
      if(object.isEmpty()){return;}
      repository.deleteById(id);
   }
}

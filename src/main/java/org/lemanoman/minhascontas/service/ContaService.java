package org.lemanoman.minhascontas.service;

import org.lemanoman.minhascontas.model.Conta;
import org.lemanoman.minhascontas.repository.ContaRepository;
import org.lemanoman.minhascontas.security.model.User;
import org.lemanoman.minhascontas.security.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ContaService extends GenericCrudService<ContaRepository, Conta> {

   final private UserService userService;
   final private ContaRepository contaRepository;

   public ContaService(ContaRepository contaRepository, UserService userService) {
      super(contaRepository);
      this.userService = userService;
      this.contaRepository = contaRepository;
   }

   public Conta addConta(Conta conta){
      Optional<User> user = userService.getUserWithAuthorities();
      if(user.isEmpty()){return null;}
      conta.setUserId(user.get().getId());
      return this.saveOrUpdate(conta);
   }


   @Override
   public Conta merge(Conta rootObject, Conta anotherObject) {
      rootObject.setDescricao(anotherObject.getDescricao());
      rootObject.setPago(anotherObject.isPago());
      rootObject.setValor(anotherObject.getValor());
      rootObject.setVencimento(anotherObject.getVencimento());
      rootObject.setUserId(anotherObject.getUserId());
      return rootObject;
   }
}

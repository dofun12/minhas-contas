package org.lemanoman.minhascontas.rest;

import org.lemanoman.minhascontas.StaticSession;
import org.lemanoman.minhascontas.dto.JsonResponse;
import org.lemanoman.minhascontas.model.Conta;
import org.lemanoman.minhascontas.security.rest.dto.UserDto;
import org.lemanoman.minhascontas.security.service.UserService;
import org.lemanoman.minhascontas.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas")
public class ContaRestController {

   private final ContaService contaService;
   public ContaRestController(ContaService contaService){
      this.contaService = contaService;
   }


   @GetMapping({"/",""})
   public ResponseEntity<JsonResponse> getListConta() {
      return ResponseEntity.ok(JsonResponse.ok(contaService.listAll()));
   }

   @PostMapping({"/",""})
   public ResponseEntity<JsonResponse> addConta(@RequestBody Conta conta) {
      return ResponseEntity.ok(JsonResponse.ok(contaService.addConta(conta)));
   }

}

package org.lemanoman.minhascontas.rest;

import org.lemanoman.minhascontas.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.lemanoman.minhascontas.StaticSession;
import org.lemanoman.minhascontas.security.rest.dto.UserDto;

@RestController
@RequestMapping("/api/initialUser")
public class InitialUserRestController {

   private final UserService userService;
   public InitialUserRestController(UserService userService){
      this.userService = userService;
   }

   @GetMapping("")
   public ResponseEntity<UserDto> initialUser() {
      if(StaticSession.HAS_USERS){
         return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(userService.generateRootUser().get());
   }
}

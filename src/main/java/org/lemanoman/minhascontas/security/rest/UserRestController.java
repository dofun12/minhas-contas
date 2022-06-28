package org.lemanoman.minhascontas.security.rest;

import org.lemanoman.minhascontas.security.model.User;
import org.lemanoman.minhascontas.security.rest.dto.UserDto;
import org.lemanoman.minhascontas.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

   private final UserService userService;

   public UserRestController(UserService userService) {
      this.userService = userService;
   }

   @GetMapping("")
   public ResponseEntity<User> getActualUser() {
      return ResponseEntity.ok(userService.getUserWithAuthorities().get());
   }

   @PostMapping("")
   public ResponseEntity<User> postNewUser(@RequestBody UserDto user) {
      return ResponseEntity.ok(userService.addDefaultUser(user).orElse(new User()));
   }

   @PostMapping("/admin")
   public ResponseEntity<User> postNewAdminUser(@RequestBody UserDto user) {
      return ResponseEntity.ok(userService.addRootUser(user).orElse(new User()));
   }

   @GetMapping("/{id}")
   public ResponseEntity<User> getById(@PathVariable Integer id) {
      return ResponseEntity.ok(userService.getById(id).orElse(new User()));
   }

   @PostMapping("/{id}")
   public ResponseEntity<User> postEditUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
      userDto.setId(id.longValue());
      return ResponseEntity.ok(userService.editUser(userDto).orElse(new User()));
   }


   @GetMapping("/list")
   public ResponseEntity<List<User>> getListUsers() {
      return ResponseEntity.ok(userService.getListUsers());
   }


}

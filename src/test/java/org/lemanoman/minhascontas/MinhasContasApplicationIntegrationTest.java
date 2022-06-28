package org.lemanoman.minhascontas;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.lemanoman.minhascontas.rest.InitialUserRestController;
import org.lemanoman.minhascontas.security.model.User;
import org.lemanoman.minhascontas.security.rest.dto.LoginDto;
import org.lemanoman.minhascontas.security.rest.dto.UserDto;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
   MinhasContasApplication.class,
   InitialUserRestController.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MinhasContasApplicationIntegrationTest {

   final private ObjectMapper mapper = new ObjectMapper();

   @LocalServerPort
   String port;

   @Autowired
   private WebTestClient webTestClient;

   private UserDto getInitialUser() {
      FluxExchangeResult<String> getInitialUser =
         webTestClient
            .mutate()
            .build()
            .get()
            .uri("/api/initialUser")
            .exchange()
            .expectStatus()
            .isOk().returnResult(String.class);
      final byte[] content = getInitialUser.getResponseBodyContent();
      Assert.notNull(content);


      String data = new String(content);

      UserDto userDto = null;
      try {
         userDto = mapper.convertValue(mapper.readTree(data), UserDto.class);
      } catch (IOException e) {
         e.printStackTrace();
      }
      Assert.notNull(userDto);
      return userDto;
   }

   private String login(LoginDto loginDto) {


      FluxExchangeResult<String> postLoginAuthenticate =
         webTestClient
            .mutate()
            .build()
            .post()
            .uri("/api/authenticate")
            .body(BodyInserters.fromObject(loginDto))
            .exchange()
            .expectStatus()
            .isOk().expectHeader().exists("Authorization").returnResult(String.class);
      List<String> authValues = postLoginAuthenticate.getResponseHeaders().get("Authorization");
      if (authValues == null) {
         return "";
      }
      return authValues.toString().substring(1);
   }

   @Test
   public void testLogin() {
      UserDto userDto = getInitialUser();
      LoginDto loginDto = new LoginDto();
      loginDto.setUsername(userDto.getUsername());
      loginDto.setPassword(userDto.getPassword());
      Assert.notNull(login(loginDto));
   }

   @Test
   public void testAddUser() {
      UserDto initialUserDTO = getInitialUser();
      LoginDto loginDto = new LoginDto();
      loginDto.setUsername(initialUserDTO.getUsername());
      loginDto.setPassword(initialUserDTO.getPassword());
      Assert.notNull(login(loginDto));

      String authLogin = login(loginDto);

      UserDto userDto = new UserDto();
      userDto.setEmail("mail@meumail.com");
      userDto.setActivated(true);
      userDto.setUsername("login");
      userDto.setFirstname("Nome");
      userDto.setLastname("Sobrenome");
      userDto.setPassword("senha1234");
      FluxExchangeResult<String> postNewUser =
         webTestClient
            .mutate()
            .build()
            .post()
            .uri("/api/user/admin")
            .body(BodyInserters.fromObject(userDto))
            .header("Authorization", authLogin)
            .exchange()
            .expectStatus()
            .isOk().returnResult(String.class);
      final byte[] content = postNewUser.getResponseBodyContent();
      Assert.notNull(content);
      User userInserted = null;
      try {
         userInserted = mapper.readValue(content, User.class);
      } catch (IOException e) {
         e.printStackTrace();
      }
      Assert.notNull(userInserted);

      final LoginDto newUserLogin = new LoginDto();
      newUserLogin.setUsername(userDto.getUsername());
      newUserLogin.setPassword(userDto.getPassword());
      String newTokenLogin = login(newUserLogin);
      Assert.notNull(newTokenLogin);

   }

}

package org.lemanoman.minhascontas.config;


import org.lemanoman.minhascontas.security.rest.dto.UserDto;
import org.lemanoman.minhascontas.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.lemanoman.minhascontas.StaticSession;

@Component
public class ApplicationStartup
implements ApplicationListener<ApplicationReadyEvent> {

  /**
   * This event is executed as late as conceivably possible to indicate that
   * the application is ready to service requests.
   */
  final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
  final private UserService userService;

  @Value("${custom.create-root-user}")
  boolean createRootUser;

  public ApplicationStartup(UserService userService){
     this.userService = userService;
  }

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    StaticSession.HAS_USERS = userService.hasUsers();
    if(StaticSession.HAS_USERS){
       return;
    }
     userService.createBaseAuthoritys();
    if(createRootUser){
       UserDto userDto = new UserDto();
       userDto.setEmail("mail");
       userDto.setActivated(true);
       userDto.setLastname("admin");
       userDto.setFirstname("admin");
       userDto.setUsername("admin");
       userDto.setPassword("admin");
       userService.addRootUser(userDto);
       logger.info("Create user {} with passwd {}", userDto.getUsername(), userDto.getPassword());
    }

    logger.info("First generated user is available on {}", "/api/user/initialUser");
  }
}

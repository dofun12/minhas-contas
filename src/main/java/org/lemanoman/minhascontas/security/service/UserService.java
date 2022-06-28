package org.lemanoman.minhascontas.security.service;

import org.lemanoman.minhascontas.security.SecurityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.lemanoman.minhascontas.StaticSession;
import org.lemanoman.minhascontas.security.model.Authority;
import org.lemanoman.minhascontas.security.model.User;
import org.lemanoman.minhascontas.security.repository.AuthorityRepository;
import org.lemanoman.minhascontas.security.repository.UserRepository;
import org.lemanoman.minhascontas.security.rest.dto.UserDto;
import org.lemanoman.minhascontas.utils.PasswordGenerator;

import java.util.*;

@Service
@Transactional
public class UserService {

   private final UserRepository userRepository;
   private final AuthorityRepository authorityRepository;


   public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
      this.userRepository = userRepository;
      this.authorityRepository = authorityRepository;
   }

   @Transactional(readOnly = true)
   public boolean hasUsers() {
      return userRepository.count() > 0;
   }

   @Transactional
   public void createBaseAuthoritys() {
      String[] baseAuths = new String[]{StaticSession.ROLE_ADMIN, StaticSession.ROLE_USER};
      for(String auth: baseAuths){
         if(authorityRepository.findAuthorityByName(auth).isEmpty()){
            authorityRepository.save(new Authority(auth));
         }
      }
   }

   @Transactional(readOnly = true)
   public List<User> getListUsers() {
      return userRepository.findAll();
   }

   @Transactional(readOnly = true)
   public  Optional<User> getById(Integer id) {
      return Optional.of(userRepository.getOne(id.longValue()));
   }

   @Transactional(readOnly = true)
   public Optional<User> getUserWithAuthorities() {
      return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
   }

   public Optional<UserDto> generateRootUser(){
      UserDto userDto = new UserDto();
      userDto.setActivated(true);
      userDto.setUsername("super");
      PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
         .useDigits(true)
         .useLower(true)
         .useUpper(true)
         .build();
      userDto.setEmail("email@qualquer.com");
      userDto.setPassword(passwordGenerator.generate(8));
      userDto.setFirstname("Senhor");
      userDto.setLastname("Administrador");
      var rootUser = addRootUser(userDto);
      if(rootUser.isEmpty()){
         return Optional.empty();
      }
      userDto.setId(rootUser.get().getId());
      return Optional.of(userDto);
   }

   public Optional<User> addRootUser(UserDto userDto){
      return addNewUser(userDto, StaticSession.ROLE_ADMIN);
   }

   public Optional<User> addDefaultUser(UserDto userDto){
      return addNewUser(userDto, StaticSession.ROLE_USER);
   }

   @Transactional
   public Optional<User> editUser(UserDto userDto) {
      if(userDto.getId()==null){
         return addDefaultUser(userDto);
      }
      return Optional.of(userRepository.save(userDtoToUser(userDto)));
   }

   private User userDtoToUser(UserDto userDto){
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      User user = userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(new User());
      user.setUsername(userDto.getUsername());
      user.setPassword(encoder.encode(userDto.getPassword()));
      user.setActivated(userDto.isActivated());
      user.setEmail(userDto.getEmail());
      user.setLastname(userDto.getLastname());
      user.setFirstname(userDto.getFirstname());
      return user;
   }

   @Transactional
   public Optional<User> addNewUser(UserDto userDto, String authorityRole) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

      Optional<Authority> authority = authorityRepository.findAuthorityByName(authorityRole);
      User user = userDtoToUser(userDto);
      Set<Authority> authoritySet = new HashSet<>(1);
      authoritySet.add(authority.get());
      user.setAuthorities(authoritySet);

      return Optional.of(userRepository.save(user));
   }

}

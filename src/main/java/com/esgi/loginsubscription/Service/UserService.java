package com.esgi.loginsubscription.Service;

import com.esgi.loginsubscription.config.CodeException;
import com.esgi.loginsubscription.exception.BadRequestException;
import com.esgi.loginsubscription.exception.EntityNotFoundException;
import com.esgi.loginsubscription.model.Role;
import com.esgi.loginsubscription.model.User;
import com.esgi.loginsubscription.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class UserService implements  UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Méthode utilisée par le système de connexion pour récupérer un utilisateur existant en base de données
     * @param username de l'utilisateur se connectant à l'application
     * @return la ressource User trouvée en base de données
     */
    @Transactional(readOnly = true)
    public User findByUserName(@NotNull String username) {
        User user = userRepository.findFirstByUsername(username);

        if(user == null) {
            throw new EntityNotFoundException(CodeException.ENTITY_NOT_FOUND);
        }
        return user;
    }
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if(user==null){
            log.error("User {} not found in the database ",username);
            throw new UsernameNotFoundException(CodeException.ENTITY_NOT_FOUND);
        }else{
            log.info("User found in the database: {}",username);

        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
    /**
     * Méthode permettant de mettre à jour une ressource User existante
     * @param user DTO contenant les informations modifiées pour la ressource Demande
     * @return la ressource Demande modifiée
     */
    public User updateUser(@Valid User user) {
        Optional<User> userModifie;
        if(user.getId() != null) {
            userModifie = userRepository.findById(user.getId());
        } else {
            throw  new IllegalStateException("UPDATE_ENTITY_IMPOSSIBLE_NOT_ALREADY_PERSISTED");
        }

        if(userModifie.isEmpty()) {
            throw new IllegalStateException("ENTITY_NOT_FOUND");
        }
        return userRepository.save(user);
    }
    public User saveUser(User user) {
log.info(user.toString());
        if(userRepository.findFirstByUsername(user.getUsername())==null)
        {
            log.info("Saving new user {} to the database",user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.ROLE_USER);
            return userRepository.save(user);
        }
        log.error("User found in the database: {}",user);
        throw new BadRequestException("username is already taken!");
    }
//test 2


    @Transactional
    public void addRoleToUser(Long id, String roleName) {
        log.info("Adding role {} to user {}",roleName,id);
        User user = userRepository.findFirstById(id);
        if(user.getRole().toString() != roleName){
            Role roleEnum = Role.valueOf(roleName);
            if(roleEnum==null){
                throw new IllegalStateException("The role does not exist");
            }
            user.setRole(roleEnum);
            userRepository.save(user);
        }
        else {
            log.info("{} is already has the role {}",id,roleName);
        }
    }

    public User getUser(String username) {
        log.info("Fetching user {}",username);
        return userRepository.findFirstByUsername(username);
    }


    public List<User> getUsers() {
        log.info("Fetching all user ");
        return userRepository.findAll();


    }


    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException());;
        if(user!= null){
            userRepository.delete(user);
        }
    }
}

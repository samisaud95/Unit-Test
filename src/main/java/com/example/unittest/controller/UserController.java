package com.example.unittest.controller;

import com.example.unittest.entity.User;
import com.example.unittest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/add")
    public User add(@RequestBody User user){
        return userRepository.save(user);
    }
    @GetMapping("/userlist")
    public List<User> getUserList(){
        return userRepository.findAll();
    }
    @GetMapping("/getuser/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        if(userRepository.findById(id).isPresent()){
            return userRepository.findById(id);
        }else{
            return Optional.empty();
        }
    }
    @PutMapping("/updateuser/{id}")
    public Optional<User> updateUserById(@RequestBody User user,@PathVariable Long id){
        Optional<User> userOptional= userRepository.findById(id);
        if(userOptional.isEmpty()){
            userOptional.get().setNome(user.getNome());
            userOptional.get().setCognome(user.getCognome());
            userRepository.save(userOptional.get());
            return userOptional;
        }else{
        return  Optional.empty();
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestParam Long id){
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}


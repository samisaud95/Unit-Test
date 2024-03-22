package com.example.unittest.service;

import com.example.unittest.entity.User;
import com.example.unittest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User createUser(User user){
        User savedUser= userRepository.save(user);
        return savedUser;
    }
    public List<User> getListUser(){
        List<User> userList= (List<User>) userRepository.findAll();
        return userList;
    }
    public Optional<User> getUserById(Long id){
        Optional<User> userOptional= userRepository.findById(id);
        return userOptional;
    }
    public Optional<User> getUserUpdate(Long id,User user){
        Optional<User> userUpdate= userRepository.findById(id);
        if (userUpdate.isPresent()){
            userUpdate.get().setNome(user.getNome());
            userUpdate.get().setCognome(user.getCognome());
            userRepository.save(userUpdate.get());
        }else{
            return Optional.empty();
        }
        return userUpdate;
    }
    public User deleteUser(Long id,User user){
        userRepository.deleteById(id);
        return user;
    }
}

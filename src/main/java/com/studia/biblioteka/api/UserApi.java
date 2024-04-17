package com.studia.biblioteka.api;

import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserApi {
    private UserManager users;

    @Autowired
    public UserApi(UserManager users) {
        this.users = users;
    }

    @GetMapping
    public Optional<User> getById(@RequestParam long id){
        return users.findById(id);
    }

    @GetMapping("/all") Iterable<User> getAll(){
        return users.findAll();
    }

    @PostMapping
    public User addUser(@RequestBody User user){
        return users.save(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User doctor){
        return users.save(doctor);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam long id){
        users.delete(id);
    }
}

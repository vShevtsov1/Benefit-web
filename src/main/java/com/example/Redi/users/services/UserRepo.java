package com.example.Redi.users.services;

import com.example.Redi.users.data.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String > {

    boolean existsByEmail(String email);
    User findByEmail(String email);


}

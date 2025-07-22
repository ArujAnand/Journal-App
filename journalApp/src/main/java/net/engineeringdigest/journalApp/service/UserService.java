package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(UserEntity newUser) {
        userRepository.save(newUser);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public boolean findById(ObjectId id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

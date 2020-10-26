package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.User;
import com.zhouning.animalsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isCorrect(String user_id,String password) {
        Integer res=userRepository.pr_check_password(user_id, password);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public User getUser(String userid) {
        return userRepository.findByUserId(userid);
    }

}

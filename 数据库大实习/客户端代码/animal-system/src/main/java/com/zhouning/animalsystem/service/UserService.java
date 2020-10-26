package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public boolean isCorrect(String user_id,String password);
    public User getUser(String userid);
}

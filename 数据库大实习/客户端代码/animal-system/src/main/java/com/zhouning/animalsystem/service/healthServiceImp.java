package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.health;
import com.zhouning.animalsystem.repository.healthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class healthServiceImp implements healthService {

    @Autowired
    healthRepository repository;

    @Override
    public List<health> get_all_health() {
        return repository.findAll();
    }

    @Override
    public boolean insertHealth(String P_ANIMAL_ID, String P_USER_ID, String p_CHECK_DATE, String P_HEALTH_INFORMATION, String P_REMARKS) {
        int res= repository.pr_insert_into_health( P_ANIMAL_ID,  P_USER_ID,  p_CHECK_DATE,  P_HEALTH_INFORMATION,  P_REMARKS);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public boolean updateHealth(String P_ANIMAL_ID, String P_USER_ID, String p_CHECK_DATE, String P_HEALTH_INFORMATION, String P_REMARKS) {
        int res= repository.pr_update_health( P_ANIMAL_ID,  P_USER_ID,  p_CHECK_DATE,  P_HEALTH_INFORMATION,  P_REMARKS);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public boolean deleteHealth(String P_ANIMAL_ID, String P_USER_ID, String p_CHECK_DATE) {
        int res= repository.pr_delete_health( P_ANIMAL_ID,  P_USER_ID,  p_CHECK_DATE);
        if (res==1)
            return true;
        return false;
    }
}

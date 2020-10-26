package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.vaccine;
import com.zhouning.animalsystem.repository.vaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class vaccineServiceImp implements vaccineService {

    @Autowired
    vaccineRepository repository;
    @Override
    public List<vaccine> getVaccines() {
        return repository.findAll();
    }

    @Override
    public boolean insertVaccine(String vaccine_ID, String vaccine_type, String vaccine_name) {
        int res = repository.pr_insert_into_animal(vaccine_ID,vaccine_type,vaccine_name);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public boolean updateVaccine(String vaccine_ID, String vaccine_type, String vaccine_name) {
        int res = repository.pr_update_shelter(vaccine_ID,vaccine_type,vaccine_name);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public boolean deleteVaccine(String vaccine_ID) {
        int res = repository.pr_delete_vaccine(vaccine_ID);
        if (res==1)
            return true;
        return false;
    }
}

package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.animal;
import com.zhouning.animalsystem.repository.animalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class animalServiceImp implements animalService {

    @Autowired
    animalRepository repository;

    @Override
    public List<animal> getanimals() {
        return repository.findAll();
    }

    @Override
    public boolean insertAnimal(String P_ANIMAL_ID, String P_ANIMAL_NAME, String p_ANIMAL_SPECIES, Integer P_ANIMAL_AGE, String P_SHELTER_ID) {

        int res = repository.pr_insert_into_animal(P_ANIMAL_ID,P_ANIMAL_NAME,p_ANIMAL_SPECIES,P_ANIMAL_AGE,P_SHELTER_ID);
        if(res==1)
            return true;

        return false;
    }

    @Override
    public boolean updateAnimal(String P_ANIMAL_ID, String P_ANIMAL_NAME, String p_ANIMAL_SPECIES, Integer P_ANIMAL_AGE, String P_SHELTER_ID) {
        int res = repository.pr_update_animal(P_ANIMAL_ID,P_ANIMAL_NAME,p_ANIMAL_SPECIES,P_ANIMAL_AGE,P_SHELTER_ID);
        if(res==1)
            return true;

        return false;
    }

    @Override
    public boolean deleteAnimal(String P_ANIMAL_ID) {
        int res = repository.pr_delete_animal(P_ANIMAL_ID);
        if(res==1)
            return true;
        return false;
    }
}

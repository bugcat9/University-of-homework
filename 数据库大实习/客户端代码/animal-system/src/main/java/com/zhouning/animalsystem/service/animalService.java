package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.animal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface animalService {

    //
    public List<animal> getanimals();

    //插入
    public boolean insertAnimal( String P_ANIMAL_ID,
                                 String P_ANIMAL_NAME,
                                 String p_ANIMAL_SPECIES,
                                 Integer P_ANIMAL_AGE,
                                 String P_SHELTER_ID
                                );
    //更新
    public boolean updateAnimal( String P_ANIMAL_ID,
                                 String P_ANIMAL_NAME,
                                 String p_ANIMAL_SPECIES,
                                 Integer P_ANIMAL_AGE,
                                 String P_SHELTER_ID
                                );
    //删除
    public boolean deleteAnimal(String P_ANIMAL_ID);
}

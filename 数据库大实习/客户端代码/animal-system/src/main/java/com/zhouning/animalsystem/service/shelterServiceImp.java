package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.shelter;
import com.zhouning.animalsystem.repository.shelterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class shelterServiceImp implements shelterService {

    @Autowired
    shelterRepository repository;

    @Override
    public List<shelter> getshelters() {
        List<shelter> shelters= repository.findAll();
        return shelters;
    }

    @Override
    public boolean insertShelter(String P_SHELTER_ID, String P_SHELTER_NAME, String p_SHELTER_ADDRESS,
                                 String P_POSTCODE, Integer P_SUM_ROOMS, Integer P_REMAIN_ROOMS, String P_SHELTER_COMMENT) {
        int res = repository.pr_insert_into_shelter(P_SHELTER_ID,P_SHELTER_NAME,p_SHELTER_ADDRESS,
                                                P_POSTCODE,P_SUM_ROOMS, P_REMAIN_ROOMS, P_SHELTER_COMMENT);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public boolean updateShelter(String P_SHELTER_ID, String P_SHELTER_NAME, String p_SHELTER_ADDRESS,
                                 String P_POSTCODE, Integer P_SUM_ROOMS, Integer P_REMAIN_ROOMS, String P_SHELTER_COMMENT) {
        int res = repository.pr_update_shelter(P_SHELTER_ID,P_SHELTER_NAME,p_SHELTER_ADDRESS,
                P_POSTCODE,P_SUM_ROOMS, P_REMAIN_ROOMS, P_SHELTER_COMMENT);
        if (res==1)
            return true;
        return false;
    }

    @Override
    public boolean deleteShelter(String P_SHELTER_ID) {
        int res = repository.pr_delete_shelter(P_SHELTER_ID);
        if (res==1)
            return true;
        return false;
    }
}

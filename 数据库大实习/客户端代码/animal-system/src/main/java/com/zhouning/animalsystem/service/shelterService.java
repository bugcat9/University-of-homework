package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.shelter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface shelterService {

    public List<shelter> getshelters();

    public boolean insertShelter(
            String P_SHELTER_ID,
            String P_SHELTER_NAME,
            String p_SHELTER_ADDRESS,
            String P_POSTCODE,
            Integer P_SUM_ROOMS,
            Integer P_REMAIN_ROOMS,
            String P_SHELTER_COMMENT
    );

    public boolean updateShelter(
            String P_SHELTER_ID,
            String P_SHELTER_NAME,
            String p_SHELTER_ADDRESS,
            String P_POSTCODE,
            Integer P_SUM_ROOMS,
            Integer P_REMAIN_ROOMS,
            String P_SHELTER_COMMENT
    );

    public boolean deleteShelter( String P_SHELTER_ID);


}

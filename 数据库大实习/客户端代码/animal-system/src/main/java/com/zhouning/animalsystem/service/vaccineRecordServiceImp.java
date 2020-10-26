package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.vaccineRecord;
import com.zhouning.animalsystem.repository.animalRepository;
import com.zhouning.animalsystem.repository.vaccineRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class vaccineRecordServiceImp implements vaccineRecordService {

    @Autowired
    vaccineRecordRepository repository;

    @Override
    public List<vaccineRecord> getvaccineRecords() {
        return repository.findAll();
    }

    @Override
    public boolean insertvaccineRecord(String P_VACCINE_ID, String P_ANIMAL_ID, String p_USER_ID, String P_VACCINATION_TIME, String P_REMARKS) {
        int res = repository.pr_insert_into_VACCINE_RECORD(P_VACCINE_ID,P_ANIMAL_ID,p_USER_ID,P_VACCINATION_TIME,P_REMARKS);
        if(res==1)
            return true;
        return false;
    }

    @Override
    public boolean updatevaccineRecord(String P_VACCINE_ID, String P_ANIMAL_ID, String p_USER_ID, String P_VACCINATION_TIME, String P_REMARKS) {
        int res = repository.pr_update_VACCINE_RECORD(P_VACCINE_ID,P_ANIMAL_ID,p_USER_ID,P_VACCINATION_TIME,P_REMARKS);
        if(res==1)
            return true;
        return false;
    }

    @Override
    public boolean deletevaccineRecord(String P_VACCINE_ID, String P_ANIMAL_ID, String p_USER_ID, String P_VACCINATION_TIME) {
        int res = repository.pr_delete_VACCINE_RECORD(P_VACCINE_ID,P_ANIMAL_ID,p_USER_ID,P_VACCINATION_TIME);
        if(res==1)
            return true;
        return false;
    }
}

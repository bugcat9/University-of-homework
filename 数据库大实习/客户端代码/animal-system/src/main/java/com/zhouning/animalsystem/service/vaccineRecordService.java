package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.vaccineRecord;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface vaccineRecordService {
    //
    //
    public List<vaccineRecord> getvaccineRecords();

    //插入
    public boolean insertvaccineRecord(String P_VACCINE_ID,
                                       String P_ANIMAL_ID,
                                       String p_USER_ID,
                                       String P_VACCINATION_TIME,
                                       String P_REMARKS
    );
    //更新
    public boolean updatevaccineRecord( String P_VACCINE_ID,
                                        String P_ANIMAL_ID,
                                        String p_USER_ID,
                                        String P_VACCINATION_TIME,
                                        String P_REMARKS
    );
    //删除
    public boolean deletevaccineRecord(String P_VACCINE_ID,
                                       String P_ANIMAL_ID,
                                       String p_USER_ID,
                                       String P_VACCINATION_TIME);
}

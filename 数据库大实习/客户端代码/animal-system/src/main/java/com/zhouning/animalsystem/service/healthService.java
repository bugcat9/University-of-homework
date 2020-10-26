package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.animal;
import com.zhouning.animalsystem.entity.health;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface healthService {
    public List<health> get_all_health();

    //插入
    public boolean insertHealth( String P_ANIMAL_ID,
                                 String P_USER_ID,
                                 String p_CHECK_DATE,
                                 String P_HEALTH_INFORMATION,
                                 String P_REMARKS
    );
    //更新
    public boolean updateHealth( String P_ANIMAL_ID,
                                 String P_USER_ID,
                                 String p_CHECK_DATE,
                                 String P_HEALTH_INFORMATION,
                                 String P_REMARKS
    );
    //删除
    public boolean deleteHealth(String P_ANIMAL_ID,
                                String P_USER_ID,
                                String p_CHECK_DATE);
}

package com.zhouning.animalsystem.service;

import com.zhouning.animalsystem.entity.vaccine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface vaccineService {

    public List<vaccine> getVaccines();

    public boolean insertVaccine(String vaccine_ID,
                                 String vaccine_type,
                                 String vaccine_name);

    public boolean updateVaccine(String vaccine_ID,
                                 String vaccine_type,
                                 String vaccine_name);

    public boolean deleteVaccine(String vaccine_ID);

}

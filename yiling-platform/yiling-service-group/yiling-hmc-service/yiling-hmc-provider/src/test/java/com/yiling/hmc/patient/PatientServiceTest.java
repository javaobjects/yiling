package com.yiling.hmc.patient;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import com.yiling.hmc.patient.service.PatientService;

/**
 * @author: gxl
 * @date: 2022/4/8
 */
public class PatientServiceTest extends BaseTest {
    @DubboReference
    LocationApi locationApi;

    @Autowired
    PatientService patientService;

    @Test
    public void test1(){
        List<String> codeList = Lists.newArrayList();
        // codeList.add("130322199211053011".substring(0,6));
        // codeList.add("342225196705200500".substring(0,6));
        codeList.add("420683199209100516".substring(0,6));
        List<LocationDTO> parentByCodeList = locationApi.getParentByCodeList(codeList);
        System.out.println(parentByCodeList.toString());
    }
    @Test
    public void save(){
        SavePatientRequest request = new SavePatientRequest();
        request.setIdCard("110101198001010010").setMobile("15600720123").setPatientName("你er爷");
        patientService.savePatient(request);
    }
}
package com.yiling.hmc.patient.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.patient.dao.PatientMapper;
import com.yiling.hmc.patient.dto.PatientDTO;
import com.yiling.hmc.patient.dto.request.QueryPatientPageRequest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import com.yiling.hmc.patient.entity.PatientDO;
import com.yiling.hmc.patient.service.PatientService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 就诊人 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-04-07
 */
@Slf4j
@Service
public class PatientServiceImpl extends BaseServiceImpl<PatientMapper, PatientDO> implements PatientService {

    @DubboReference
    LocationApi locationApi;

    @Override
    public void savePatient(SavePatientRequest request) {
        LambdaQueryWrapper<PatientDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(PatientDO::getIdCard,request.getIdCard()).eq(PatientDO::getPatientName,request.getPatientName()).last("limit 1");
        PatientDO patientDO = this.getOne(wrapper);
        if(Objects.isNull(patientDO)){
            patientDO = new PatientDO();
            PojoUtils.map(request,patientDO);
            if(IdcardUtil.isValidCard(request.getIdCard())){
                int genderByIdCard = IdcardUtil.getGenderByIdCard(request.getIdCard());
                if(genderByIdCard==0){
                    patientDO.setGender(2);
                }else {
                    patientDO.setGender(genderByIdCard);
                }

                patientDO.setPatientAge(IdcardUtil.getAgeByIdCard(request.getIdCard()));
                List<String> codeList = Lists.newArrayList();
                String regionCode = request.getIdCard().substring(0,6);
                codeList.add(regionCode);
                List<LocationDTO> parentByCodeList = locationApi.getParentByCodeList(codeList);
                LocationDTO locationDTO = parentByCodeList.get(0);
                patientDO.setRegionCode(regionCode).setCityCode(locationDTO.getCode()).setProvinceCode(locationDTO.getParentCode());
                this.saveOrUpdate(patientDO);
            }else{
                log.info("身份格式不正确idcard={}",request.getIdCard());
            }

        }else{
            if(StrUtil.isNotEmpty(request.getMobile()) && StrUtil.isEmpty(patientDO.getMobile())){
                patientDO.setMobile(request.getMobile());
                this.saveOrUpdate(patientDO);
            }
        }

    }

    @Override
    public Page<PatientDTO> queryPatientPage(QueryPatientPageRequest request) {
        LambdaQueryWrapper<PatientDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotEmpty(request.getPatientName()),PatientDO::getPatientName,request.getPatientName());
        wrapper.eq(Objects.nonNull(request.getGender()),PatientDO::getGender,request.getGender());
        wrapper.eq(StringUtils.isNotEmpty(request.getCityCode()),PatientDO::getCityCode,request.getCityCode());
        wrapper.eq(StringUtils.isNotEmpty(request.getRegionCode()),PatientDO::getRegionCode,request.getRegionCode());
        wrapper.eq(StringUtils.isNotEmpty(request.getProvinceCode()),PatientDO::getProvinceCode,request.getProvinceCode());
        wrapper.ge(Objects.nonNull(request.getStartPatientAge()),PatientDO::getPatientAge,request.getStartPatientAge());
        wrapper.le(Objects.nonNull(request.getEndPatientAge()),PatientDO::getPatientAge,request.getEndPatientAge());
        if(Objects.nonNull(request.getEndTime())){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
            wrapper.le(PatientDO::getCreateTime,request.getEndTime());
        }
        wrapper.ge(Objects.nonNull(request.getStartTime()),PatientDO::getCreateTime,request.getStartTime());
        wrapper.orderByDesc(PatientDO::getId);
        Page<PatientDO> patientDOPage = this.page(request.getPage(), wrapper);
        Page<PatientDTO> patientDTOPage = PojoUtils.map(patientDOPage,PatientDTO.class);
        return patientDTOPage;
    }

    @Override
    public Map<Long, Long> queryPatientCountByUserId(List<Long> userIdList) {
        LambdaQueryWrapper<PatientDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(PatientDO::getFromUserId,userIdList);
        List<PatientDO> list = this.list(wrapper);
        if(CollUtil.isEmpty(list)){
            return Maps.newHashMap();
        }
        Map<Long, Long> collect = list.stream().collect(Collectors.groupingBy(PatientDO::getFromUserId, Collectors.counting()));
        return collect;
    }
}

package com.yiling.basic.tianyancha.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.basic.tianyancha.bo.TycEnterpriseInfoBO;
import com.yiling.basic.tianyancha.bo.TycResultBO;
import com.yiling.basic.tianyancha.dao.TycEnterpriseInfoMapper;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;
import com.yiling.basic.tianyancha.entity.TycEnterpriseInfoDO;
import com.yiling.basic.tianyancha.enums.TycErrorCode;
import com.yiling.basic.tianyancha.service.TycEnterpriseService;
import com.yiling.basic.tianyancha.util.TycHttpUtils;

import cn.hutool.core.util.CreditCodeUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @author shichen
 * @类名 TycEnterpriseServiceImpl
 * @描述
 * @创建时间 2022/1/11
 * @修改人 shichen
 * @修改时间 2022/1/11
 **/
@Service
@Slf4j
public class TycEnterpriseServiceImpl extends BaseServiceImpl<TycEnterpriseInfoMapper, TycEnterpriseInfoDO> implements TycEnterpriseService {
    @Value("${tyc.host}")
    private String tycHost;

    @Value("${tyc.token}")
    private String tycToken;

    @Value("${tyc.normalBaseInfoPath}")
    private String normalBaseInfoPath;

    @Value("${tyc.switch}")
    private Boolean tycSwitch;
    @Override
    public TycResultDTO<TycEnterpriseInfoDTO> findEnterpriseByKeyword(TycEnterpriseQueryRequest request) {
        TycResultDTO<TycEnterpriseInfoDTO> resultDTO = new TycResultDTO();
        TycEnterpriseInfoDTO localDto = null;
        //先从本地天眼查库查询企业
        if(CreditCodeUtil.isCreditCode(request.getKeyword())){
            localDto = this.findTycEnterpriseByNameAndCreditCode(null, request.getKeyword());
        }else {
            localDto = this.findTycEnterpriseByNameAndCreditCode(request.getKeyword(),null );
        }
        //本地库不存在的时候去掉天眼查接口查询
        if(null==localDto){
            HashMap<String, Object> paramMap = new HashMap<>(1);
            paramMap.put("keyword",request.getKeyword());
            TycResultBO<TycEnterpriseInfoBO> resultBO= TycHttpUtils.get(tycHost + normalBaseInfoPath,new TypeReference<TycResultBO<TycEnterpriseInfoBO>>(){}, paramMap, tycToken,tycSwitch);
            if(resultBO == null){
                throw new BusinessException(TycErrorCode.REQUEST_FAIL);
            }
            if(TycErrorCode.OK.getCode().equals(resultBO.getError_code())&&null!=resultBO.getResult()){
                TycEnterpriseInfoDO tycEnterpriseInfoDO = PojoUtils.map(resultBO.getResult(), TycEnterpriseInfoDO.class);
                if(null!=resultBO.getResult().getIndustryAll()){
                    tycEnterpriseInfoDO.setCategory(resultBO.getResult().getIndustryAll().getCategory());
                    tycEnterpriseInfoDO.setCategoryBig(resultBO.getResult().getIndustryAll().getCategoryBig());
                    tycEnterpriseInfoDO.setCategoryMiddle(resultBO.getResult().getIndustryAll().getCategoryMiddle());
                    tycEnterpriseInfoDO.setCategorySmall(resultBO.getResult().getIndustryAll().getCategorySmall());
                }
                tycEnterpriseInfoDO.setCreateTime(new Date());
                this.save(tycEnterpriseInfoDO);
                resultDTO.setError_code(TycErrorCode.OK.getCode());
                resultDTO.setResult(PojoUtils.map(tycEnterpriseInfoDO,TycEnterpriseInfoDTO.class));
            }else {
                resultDTO.setError_code(resultBO.getError_code());
                resultDTO.setReason(resultBO.getReason());
            }
        }else {
            resultDTO.setError_code(TycErrorCode.OK.getCode());
            resultDTO.setResult(localDto);
        }
        return resultDTO;
    }

    @Override
    public TycEnterpriseInfoDTO findTycEnterpriseByNameAndCreditCode(String name, String creditCode) {
        if(StringUtils.isAllBlank(name,creditCode)){
            throw new BusinessException(TycErrorCode.REQUEST_FAIL,"公司名字和社会信用代码至少一个不为空");
        }
        QueryWrapper<TycEnterpriseInfoDO> wrapper=new QueryWrapper<>();
        if(StringUtils.isNotBlank(name)){
            wrapper.lambda().eq(TycEnterpriseInfoDO::getName,name);
        }
        if(StringUtils.isNotBlank(creditCode)){
            wrapper.lambda().eq(TycEnterpriseInfoDO::getCreditCode,creditCode);
        }
        wrapper.lambda().last("limit 1");
        TycEnterpriseInfoDO tycEnterpriseInfoDO = this.getOne(wrapper);
        return PojoUtils.map(tycEnterpriseInfoDO,TycEnterpriseInfoDTO.class);
    }
}

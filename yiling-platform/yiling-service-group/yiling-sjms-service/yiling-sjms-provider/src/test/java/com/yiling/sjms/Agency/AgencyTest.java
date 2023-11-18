package com.yiling.sjms.Agency;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.sjms.BaseTest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationChangeFormRequest;
import com.yiling.sjms.agency.service.AgencyRelationShipChangeFormService;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.gb.api.GbStatisticApi;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.gb.service.GbFormService;

import lombok.extern.slf4j.Slf4j;

/**
 * DEMO TEST
 *
 * @author shixing.sun
 * @date 2023/3/04
 */
@Slf4j
public class AgencyTest extends BaseTest {

    @DubboReference
    GbStatisticApi gbStatisticApi;
    @Autowired
    GbFormService gbFormService;
    @Autowired
    AgencyRelationShipChangeFormService agencyRelationShipChangeFormService;


    @Test
    public void gBStatisticProgram() {
      gbStatisticApi.gBStatisticProgram();

    }

    @Test
    public void  updateGBFormById(){
        UpdateGBFormInfoRequest request = new UpdateGBFormInfoRequest();
        request.setId(332L);
        request.setOriginalStatus(FormStatusEnum.UNSUBMIT);
        request.setNewStatus(FormStatusEnum.AUDITING);
        gbFormService.updateStatusById(request);
    }
    @Test
    public void  updateGB1FormById(){
        //String ss="{\"name\":\"上饶市弋阳县湾里卫生院\",\"crmEnterpriseId\":27,\"licenseNumber\":\"165555555555566666\",\"provinceCode\":\"360000\",\"cityCode\":\"361000\",\"regionCode\":\"361003\",\"supplyChainRole\":2,\"nodes\":\"\",\"businessRemark\":\"\",\"relationShip\":[{\"id\":78587,\"representativeCode\":\"TYX08203-001\",\"representativeName\":\"王荣锦临时代表001\",\"customerCode\":\"JX001557\",\"customerName\":\"上饶市弋阳县湾里卫生院\",\"businessDepartment\":\"事业六部（城乡）\",\"businessProvince\":\"江西\",\"businessAreaCode\":\"003019027\",\"businessArea\":\"事业六部（城乡）上饶区\",\"select\":false,\"productGroup\":\"城乡部芪苈强心\",\"postCode\":0,\"postName\":\"\",\"srcRelationShipIp\":78587,\"crmEnterpriseId\":27}],\"representativeCode\":\"15570\",\"representativeName\":\"邓渊\",\"postCode\":27942,\"postName\":\"医药信息沟通专员03\",\"department\":\"事业六部（城乡）\",\"businessDepartment\":\"事业六部（城乡）\",\"provincialArea\":\"江西\",\"businessProvince\":\"江西\",\"businessArea\":\"事业六部（城乡）上饶区\",\"superiorSupervisorCode\":\"YX08203\",\"superiorSupervisorName\":\"王荣锦\",\"phone\":\"132133213213\",\"address\":\"asfasfd\",\"formId\":2118,\"id\":\"\",\"changeRelationTotal\":1,\"relationTotal\":5}";
        String ss="{\"name\":\"天津一级商001\",\"crmEnterpriseId\":908767,\"licenseNumber\":\"shangyegongsi0100\",\"provinceCode\":\"120000\",\"cityCode\":\"120100\",\"regionCode\":\"120101\",\"supplyChainRole\":1,\"nodes\":\"\",\"businessRemark\":\"\",\"relationShip\":[{\"id\":598655,\"representativeCode\":\"24216\",\"representativeName\":\"刁东瑞\",\"customerCode\":\"\",\"customerName\":\"天津一级商001\",\"businessDepartment\":\"线上分销部\",\"businessProvince\":\"河北\",\"businessAreaCode\":\"\",\"businessArea\":\"河北\",\"select\":false,\"productGroup\":\"数字化药店终端产品组\",\"postCode\":24891,\"postName\":\"销售代表\",\"srcRelationShipIp\":598655,\"crmEnterpriseId\":908767}],\"representativeCode\":\"YX07679\",\"representativeName\":\"刘婷\",\"postCode\":36110,\"postName\":\"分销专员04\",\"department\":\"线下分销部\",\"businessDepartment\":\"线下分销部\",\"provincialArea\":\"云南\",\"businessProvince\":\"云南\",\"businessArea\":\"分销商业云南区\",\"superiorSupervisorCode\":\"YX06401\",\"superiorSupervisorName\":\"蒲定芳\",\"phone\":\"13552355908\",\"address\":\"天津市22号\",\"remark\":\"dsasasfsdafsaddfa\",\"formId\":2118,\"id\":\"\",\"changeRelationTotal\":1,\"relationTotal\":4}";
        SaveAgencyRelationChangeFormRequest saveAgencyRelationChangeFormRequest = JSONObject.parseObject(ss, SaveAgencyRelationChangeFormRequest.class);
        agencyRelationShipChangeFormService.save(saveAgencyRelationChangeFormRequest);
    }


}

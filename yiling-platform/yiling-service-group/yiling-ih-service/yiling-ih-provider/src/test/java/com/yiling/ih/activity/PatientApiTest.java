package com.yiling.ih.activity;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.BaseTest;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.ih.patient.dto.HmcPatientDTO;
import com.yiling.ih.patient.dto.request.HmcQueryPatientPageRequest;
import com.yiling.ih.patient.dto.request.HmcSyncGoodsRequest;
import com.yiling.ih.patient.dto.request.SavePatientRequest;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.PatientDoctorRelDTO;
import com.yiling.ih.user.dto.QueryPatientDoctorRelRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Slf4j
public class PatientApiTest extends BaseTest {

    @DubboReference
    IHActivityPatientEducateApi ihActivityPatientEducateApi;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @Test
    public void syncHmcGoodsToIH() {
        String s="{\"adverseReaction\":\"尚不明确。\",\"licenseNo\":\"国药准字Z44023357\",\"productCompany\":\"广东南国药业有限公司\",\"specificationsUnit\":\"ping\",\"taboo\":\"尚不明确\",\"ypidCode\":\"\",\"storage\":\"2\",\"generalName\":\"野牡丹止痢片111\",\"drugType\":2,\"approvalNumber\":\"国药准字Z44023357\",\"hmcGoodsId\":52,\"spec\":\"10*20ml\",\"isPrescriptionDrug\":\"1\",\"composition\":\"本品主要成分为野牡丹。\",\"hmcSellSpecificationsId\":41088,\"preparationFormCode\":\"片剂(糖衣)\",\"effect\":\"清热利湿，收敛止血。用于腹泻、腹痛、痢疾、便血、消化不良等。\",\"name\":\"野牡丹止痢片111\",\"property\":\"\",\"attention\":\"尚不明确\",\"validity\":\"\",\"drugInteraction\":\"如与其他药物同时使用可能会发生药物相互作用，详情请咨询医师或药师。\",\"preparationForm\":\"片剂(糖衣)\",\"usageAndDosage\":\"口服，一次3片，一日3次。\"}";
        HmcSyncGoodsRequest request= JSONUtil.toBean(s, HmcSyncGoodsRequest.class);
        diagnosisApi.syncHmcGoodsToIH(request);
    }

    @Test
    public void queryPatientDoctorRel() {
        QueryPatientDoctorRelRequest request = new QueryPatientDoctorRelRequest();
        request.setUserId(17347);
        request.setDoctorId(209);
        request.setActivityId(7L);
        PatientDoctorRelDTO dto = ihActivityPatientEducateApi.queryPatientDoctorRel(request);
        System.out.println(dto);
    }

}

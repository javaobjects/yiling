package com.yiling.ih.patient;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.BaseTest;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.ih.patient.dto.HmcPatientDTO;
import com.yiling.ih.patient.dto.request.HmcQueryPatientPageRequest;
import com.yiling.ih.patient.dto.request.MessageNotifyRequest;
import com.yiling.ih.patient.dto.request.SavePatientRequest;
import com.yiling.ih.patient.feign.HmcDiagnosisFeignClient;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.IdCardFrontPhotoOcrDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

/**
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Slf4j
public class PatientApiTest extends BaseTest {

    @DubboReference
    HmcPatientApi hmcPatientApi;

    @DubboReference
    HmcDiagnosisApi hmcDiagnosisApi;

    @DubboReference
    IHActivityPatientEducateApi patientEducateApi;

    @Autowired
    private HmcDiagnosisFeignClient hmcDiagnosisFeignClient;

    @Test
    public void getImage() {
        String result = getImgBase("/Users/shenfan/Pictures/我的图片/身份证-小照片.jpeg");
        IdCardFrontPhotoOcrDTO ocrDTO = patientEducateApi.idCardFrontPhotoOcr(result);
        System.out.println(ocrDTO);
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片地址
     * @return
     */
    public static String getImgBase(String imgFile) {

        // 将图片文件转化为二进制流
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 图片头
        //String imghead = "data:image/jpeg;base64,";
        return Base64.encodeBase64String(data);
    }


    @Test
    public void queryPatientByUserIdList() {
        Map<Long, Long> map = hmcPatientApi.queryPatientByUserIdList(Arrays.asList(1L, 2L));

    }

    @Test
    public void savePatient() {
        String s = "{\n" +
                "\"patientName\": \"我二哥\",\n" +
                "\"fromUserId\": 17126,\n" +
                "\"idCard\": \"110101197303077152\",\n" +
                "\"mobile\": \"13161895316\",\n" +
                "\"opUserId\": 17126,\n" +
                "\"opTime\": 1662446599074\n" +
                "}";
        SavePatientRequest request = JSONUtil.toBean(s, SavePatientRequest.class);

    }

    @Test
    public void queryPatientPage() {
        String s = "{\n" +
                "\"patientName\": \"我二哥\",\n" +
                "\"fromUserId\": 17126,\n" +
                "\"idCard\": \"110101197303077152\",\n" +
                "\"mobile\": \"13161895316\",\n" +
                "\"opUserId\": 17126,\n" +
                "\"opTime\": 1662446599074\n" +
                "}";
        Boolean messageNotify = hmcDiagnosisApi.eventCallBack(s);
        System.out.println("========================================:"+messageNotify);
    }
}

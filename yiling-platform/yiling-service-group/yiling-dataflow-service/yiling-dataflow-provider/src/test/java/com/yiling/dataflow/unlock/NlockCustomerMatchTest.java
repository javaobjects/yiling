package com.yiling.dataflow.unlock;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/4/24
 */
@Slf4j
public class NlockCustomerMatchTest extends BaseTest {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Test
    public void matchTest() {
        String targetName = "阳朔郭盛杰卫生室";

        int current = 1;
        int size = 2000;

        System.out.println("startTime = " + DateUtil.format(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss"));
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

//        while (true) {
//            QueryCrmEnterprisePageRequest request = new QueryCrmEnterprisePageRequest();
//            request.setCurrent(current);
//            request.setSize(size);
//            Page<CrmEnterpriseDTO> pageResult = crmEnterpriseService.getCrmEnterprisePage(request);
//
//            if (CollUtil.isEmpty(pageResult.getRecords())) {
//                break;
//            }
//            current ++;
//
//            for (CrmEnterpriseDTO crmEnterpriseDTO : pageResult.getRecords()) {
//                if (StringUtils.isEmpty(crmEnterpriseDTO.getName())) {
//                    continue;
//                }
//                // TODO 计算相似度
//                double b2 = 1 - jaroWinklerDistance.apply(targetName, crmEnterpriseDTO.getName());
//                if (b2 > 0.9) {
//                    System.out.println(crmEnterpriseDTO.getName() + " " + b2);
//                }
//            }
//            System.out.println("current = " + current);
//        }

        System.out.println("endTime = " + DateUtil.format(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss"));



    }

    public static double findSimilarity(String x, String y) {

        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            // 如果需要，可以选择忽略大小写
            return (maxLength - StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

    public static void main(String[] args) {
        String x = "淄博福烁堂医药有限公司";
        String y = "淄博福烁堂医药股份有限公司";

        double maxLength = Double.max(x.length(), y.length());
        System.out.println(maxLength);

        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        int a = levenshteinDistance.apply(x, y);
        double similarity = findSimilarity(x, y);
        System.out.println("levenshtein = " + similarity);


        double b1 = StringUtils.getJaroWinklerDistance(x, y);
        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();
        double b2 = 1 - jaroWinklerDistance.apply(x, y);
        System.out.println("jaroWinkler = " + b1 + ", " + b2);



    }
}

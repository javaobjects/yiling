package com.yiling.basic.contract.service;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.BaseTest;
import com.yiling.basic.contract.api.ContractApi;
import com.yiling.basic.contract.dto.request.ContractCancelRequest;
import com.yiling.basic.contract.dto.request.ContractCreateRequest;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

/**
 * @author fucheng.bai
 * @date 2022/11/21
 */
public class ContractServiceTest extends BaseTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private ContractApi contractApi;

    @Test
    public void createByCategory() {

        String reqStr = "{\"contractParam\":{\"qysTemplateId\":\"3027845618970211273\",\"qysCategoryId\":\"3028133107798716508\",\"subject\":\"零售部战略协议\",\"expireTime\":\"\",\"endTime\":\"2023-12-31\",\"tenantName\":\"石家庄以岭药业股份有限公司\",\"initiatorName\":\"石家庄以岭药业股份有限公司\",\"initiatorOperator\":\"白富诚\",\"initiatorContact\":\"18986091490\",\"receiverName\":\"福建恒生大药房有限公司\",\"receiverOperator\":\"王丹\",\"receiverContact\":\"13521384881\"},\"documentParam\":{\"contractNo\":\"\",\"partybName\":\"福建恒生大药房有限公司\",\"partybAdress\":\"石家庄高新区天山大街238号\",\"partybPhone\":\"0311-8590\",\"partybFax\":\"0311-8590\",\"partybPostCode\":\"0311-8590\",\"saleTotalTarget\":3081000,\"growUpGoodsTarget\":\"153000.000000\",\"newGoodsTarget\":\"261000.000000\",\"lhTarget\":\"1030000.000000\",\"otcTarget\":\"1637000.000000\",\"purchaseEname1\":\"\",\"purchaseEname2\":\"\",\"purchaseEname3\":\"\",\"otherPayMode\":\"\",\"paymentFlag\":\"单谈单签\",\"partybOperatorName\":\"药经理\",\"partybOperatorJob\":\"签章员\",\"partybOperatorPhone\":\"13521384882\",\"partybOperatorMail\":\"130101@130101.com\",\"partybPayeeName\":\"130101000016132\",\"partybBankName\":\"农业银行\",\"partybAccountNum\":\"130101000016132\",\"partybSignature\":\"\",\"growUpTable\":[{\"goodsSpec\":\"连花清瘟24粒\",\"quarter1\":50000,\"quarter2\":40000,\"quarter3\":50000,\"quarter4\":50000,\"quarterTotal\":200000},{\"goodsSpec\":\"连花清瘟36粒\",\"quarter1\":300000,\"quarter2\":100000,\"quarter3\":100000,\"quarter4\":300000,\"quarterTotal\":1200000},{\"goodsSpec\":\"连花清咳24片\",\"quarter1\":80000,\"quarter2\":43000,\"quarter3\":53000,\"quarter4\":85000,\"quarterTotal\":320000},{\"goodsSpec\":\"连花清咳36片\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"双花22片\",\"quarter1\":18000,\"quarter2\":6000,\"quarter3\":6000,\"quarter4\":13000,\"quarterTotal\":72000},{\"goodsSpec\":\"连花消杀防护\",\"quarter1\":10000,\"quarter2\":10000,\"quarter3\":10000,\"quarter4\":10000,\"quarterTotal\":40000},{\"goodsSpec\":\"通心络30粒\",\"quarter1\":1000,\"quarter2\":1000,\"quarter3\":1000,\"quarter4\":1000,\"quarterTotal\":4000},{\"goodsSpec\":\"通心络40粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"通心络90粒\",\"quarter1\":10000,\"quarter2\":5000,\"quarter3\":5000,\"quarter4\":5000,\"quarterTotal\":40000},{\"goodsSpec\":\"参松养心36粒\",\"quarter1\":1000,\"quarter2\":1000,\"quarter3\":1000,\"quarter4\":1000,\"quarterTotal\":4000},{\"goodsSpec\":\"参松养心48粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"参松养心84粒\",\"quarter1\":2000,\"quarter2\":2000,\"quarter3\":2000,\"quarter4\":2000,\"quarterTotal\":8000},{\"goodsSpec\":\"芪苈强心36粒\",\"quarter1\":2000,\"quarter2\":2000,\"quarter3\":2000,\"quarter4\":2000,\"quarterTotal\":8000},{\"goodsSpec\":\"芪苈强心180粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"夏荔芪24粒\",\"quarter1\":10000,\"quarter2\":5000,\"quarter3\":15000,\"quarter4\":10000,\"quarterTotal\":40000},{\"goodsSpec\":\"夏荔芪36粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"津力达9袋\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"津力达15袋\",\"quarter1\":5000,\"quarter2\":5000,\"quarter3\":5000,\"quarter4\":5000,\"quarterTotal\":20000},{\"goodsSpec\":\"养正消积36粒\",\"quarter1\":1000,\"quarter2\":1000,\"quarter3\":1000,\"quarter4\":1000,\"quarterTotal\":4000},{\"goodsSpec\":\"参灵蓝30粒\",\"quarter1\":3000,\"quarter2\":2000,\"quarter3\":2000,\"quarter4\":3000,\"quarterTotal\":12000},{\"goodsSpec\":\"解郁除烦24粒\",\"quarter1\":3000,\"quarter2\":3000,\"quarter3\":3000,\"quarter4\":3000,\"quarterTotal\":12000},{\"goodsSpec\":\"益肾养心24片\",\"quarter1\":1000,\"quarter2\":1000,\"quarter3\":1000,\"quarter4\":1000,\"quarterTotal\":4000},{\"goodsSpec\":\"乳结泰24粒\",\"quarter1\":4000,\"quarter2\":3000,\"quarter3\":3000,\"quarter4\":4000,\"quarterTotal\":16000},{\"goodsSpec\":\"枣椹安神6支\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"枣椹安神10支\",\"quarter1\":140000,\"quarter2\":60000,\"quarter3\":60000,\"quarter4\":120000,\"quarterTotal\":560000},{\"goodsSpec\":\"酸枣仁油软胶囊36粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"酸枣仁油软胶囊60粒\",\"quarter1\":4000,\"quarter2\":3000,\"quarter3\":3000,\"quarter4\":4000,\"quarterTotal\":16000},{\"goodsSpec\":\"八子补肾60粒\",\"quarter1\":400000,\"quarter2\":200000,\"quarter3\":200000,\"quarter4\":400000,\"quarterTotal\":1600000},{\"goodsSpec\":\"津力达18袋\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"连花清瘟36片\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"连花清瘟10袋\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"连花清瘟72粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0},{\"goodsSpec\":\"连花清瘟48粒\",\"quarter1\":0,\"quarter2\":0,\"quarter3\":0,\"quarter4\":0,\"quarterTotal\":0}]}}";
        ContractCreateRequest request = JSONObject.parseObject(reqStr, ContractCreateRequest.class);
        Long contractId = contractApi.createByCategory(request);
        System.out.println(contractId);
    }

    @Test
    public void viewUrl() {
        // TODO 返回的是内网ip
        Long contractId = 3032863450533995066L;
        String url = contractApi.viewUrl(contractId);
        System.out.println(url);
    }

    @Test
    public void sendContract() {
        Long contractId = 3032863450533995066L;
        contractApi.sendContract(contractId);
        System.out.println();
    }

    @Test
    public void recallContract() {
        Long contractId = null;
        String reason = "撤回测试";
        contractApi.recallContract(contractId, reason);
        System.out.println();
    }

    @Test
    public void cancelContract() {
        ContractCancelRequest request = new ContractCancelRequest();
        request.setContractId(null);
        request.setSealId(null);
        request.setReason("作废合同测试");
        contractApi.cancelContract(request);
        System.out.println();
    }

    @Test
    public void deleteContract() {
        Long contractId = null;
        contractApi.deleteContract(contractId);
        System.out.println();
    }

    @Test
    public void downloadContract() {
        Long contractId = 3028230875525948377L;
        String url = contractApi.downloadContract(contractId);
        System.out.println(url);
    }


    @Test
    public void uploadFile() {
        String fileName = "/Users/baifc/Downloads/测试合同文档.pdf";

        FileInfo fileInfo = null;
        try {
            fileInfo = fileService.upload(new File(fileName), FileTypeEnum.QIYUESUO_CONTRACT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("fileInfo = " + JSONObject.toJSONString(fileInfo));
    }

    public static void main(String[] args) {

    }


}

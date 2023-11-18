package com.yiling.dataflow.flow.util;

import java.util.List;

import com.yiling.dataflow.flow.dto.FlowJudgeTypeConfigDTO;
import com.yiling.dataflow.flow.entity.FlowJudgeTypeConfigDO;
import com.yiling.dataflow.flow.service.FlowJudgeTypeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowJudgeTypeUtil {

    private boolean init = false;

    private Trie trie1 = new Trie();
    private Trie trie2 = new Trie();
    private Trie trie3 = new Trie();
    private Trie trie4 = new Trie();
    private Trie trie5 = new Trie();

    @Autowired
    private FlowJudgeTypeConfigService flowJudgeTypeConfigService;

    //加载数据
    public String flowJudgeType(String name) {
        if (!init) {
            List<FlowJudgeTypeConfigDTO> flowJudgeTypeConfigDOList = flowJudgeTypeConfigService.getAllFlowJudgeTypeConfig();
            for (FlowJudgeTypeConfigDTO flowJudgeTypeConfigDO : flowJudgeTypeConfigDOList) {
                if (flowJudgeTypeConfigDO.getOrderType() == 1) {
                    trie1.insert(new StringBuffer(flowJudgeTypeConfigDO.getContainKey()).reverse().toString(), flowJudgeTypeConfigDO.getType(), flowJudgeTypeConfigDO.getEndKey());
                } else if (flowJudgeTypeConfigDO.getOrderType() == 2) {
                    trie2.insert(flowJudgeTypeConfigDO.getContainKey(), flowJudgeTypeConfigDO.getType(), flowJudgeTypeConfigDO.getEndKey());
                } else if (flowJudgeTypeConfigDO.getOrderType() == 3) {
                    trie3.insert(flowJudgeTypeConfigDO.getContainKey(), flowJudgeTypeConfigDO.getType(), flowJudgeTypeConfigDO.getEndKey());
                }else if (flowJudgeTypeConfigDO.getOrderType() == 4) {
                    trie4.insert(new StringBuffer(flowJudgeTypeConfigDO.getContainKey()).reverse().toString(), flowJudgeTypeConfigDO.getType(), flowJudgeTypeConfigDO.getEndKey());
                }else if (flowJudgeTypeConfigDO.getOrderType() == 5) {
                    trie5.insert(flowJudgeTypeConfigDO.getContainKey(), flowJudgeTypeConfigDO.getType(), flowJudgeTypeConfigDO.getEndKey());
                }
            }
            init = true;
        }


        String search = name;
        //首字符匹配
        Node node1 = trie1.getStartData(new StringBuffer(search).reverse().toString());
        if (node1 != null) {
            return node1.type;
        }

        //包含+结尾
        Node node2 = trie2.getMData(search);
        if (node2 != null && search.endsWith(node2.endStr)) {
            return node2.type;
        }
        //包含
        Node node3 = trie3.getMData(search);
        if (node3 != null) {
            return node3.type;
        }
        //结尾
        Node node4 = trie4.getStartData(new StringBuffer(search).reverse().toString());
        if (node4 != null) {
            return node4.type;
        }
        //包含
        Node node5 = trie5.getMData(search);
        if (node5 != null) {
            return node5.type;
        }
        return null;
    }
}

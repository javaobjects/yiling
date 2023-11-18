package com.yiling.workflow.workflow.util;

import java.util.Iterator;
import java.util.List;

import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;

/**
 * @author gxl
 */
public class ModelUtils {

    /**
     * 获取流程元素信息
     *
     * @param model bpmnModel对象
     * @param flowElementId 元素ID
     * @return 元素信息
     */
    public static FlowElement getFlowElementById(BpmnModel model, String flowElementId) {
        Process process = model.getMainProcess();
        return process.getFlowElement(flowElementId);
    }
    /**
     * 获取元素属性值
     * @param baseElement 流程元素
     * @param name 属性名
     * @return 属性值
     */
    public static String getElementAttributeValue(BaseElement baseElement, String name) {
        if (baseElement != null) {
            List<ExtensionAttribute> attributes = baseElement.getAttributes().get(name);
            if (attributes != null && !attributes.isEmpty()) {
                attributes.iterator().next().getValue();
                Iterator<ExtensionAttribute> attrIterator = attributes.iterator();
                if(attrIterator.hasNext()) {
                    ExtensionAttribute attribute = attrIterator.next();
                    return attribute.getValue();
                }
            }
        }
        return null;
    }

}

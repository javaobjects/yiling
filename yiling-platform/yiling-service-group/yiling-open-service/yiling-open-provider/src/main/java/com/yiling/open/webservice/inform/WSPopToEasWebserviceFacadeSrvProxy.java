/**
 * WSPopToEasWebserviceFacadeSrvProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.inform;

public interface WSPopToEasWebserviceFacadeSrvProxy extends java.rmi.Remote {
    public String addSaleOrder(String popsalenum) throws java.rmi.RemoteException, com.yiling.open.webservice.inform.WSInvokeException;
    public String receivemessage(String type, String json) throws java.rmi.RemoteException, com.yiling.open.webservice.inform.WSInvokeException;
    public String saleIssueToOtherBill(String salelssueNum) throws java.rmi.RemoteException, com.yiling.open.webservice.inform.WSInvokeException;
    public String saleOrderToSaleReturns(String saleOrderNum) throws java.rmi.RemoteException, com.yiling.open.webservice.inform.WSInvokeException;
}

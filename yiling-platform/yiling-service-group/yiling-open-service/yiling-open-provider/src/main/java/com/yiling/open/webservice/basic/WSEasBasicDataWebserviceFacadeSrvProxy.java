/**
 * WSEasBasicDataWebserviceFacadeSrvProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.basic;

public interface WSEasBasicDataWebserviceFacadeSrvProxy extends java.rmi.Remote {
    public String selectOrgs(String date) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
    public String selectLocations(String orgNum, String warehouseNum, String date) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
    public String selectWarehouses(String orgNum, String date) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
    public String inventoryQuery(String orgNum, String date, String materialNum) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
    public String selectMaterials(String date, String orgNum) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
    public String pricePolicydetils(String orgNo, String custNo, String matNo, String date) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
    public String selectCustomers(String customerNames, String taxRegistration, String date) throws java.rmi.RemoteException, com.yiling.open.webservice.basic.WSInvokeException;
}

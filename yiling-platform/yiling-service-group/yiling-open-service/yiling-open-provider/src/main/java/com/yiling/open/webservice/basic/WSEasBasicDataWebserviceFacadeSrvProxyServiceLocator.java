/**
 * WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.basic;

import org.springframework.stereotype.Service;

@Service
public class WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSrvProxyService {

//    @Value("${eas.basic.webservice.url}")
    private String basicUrl;

    public WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator() {
    }


    public WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSEasBasicDataWebserviceFacadeSrvProxyServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

//    // Use to get a proxy class for WSEasBasicDataWebserviceFacade
//    private String WSEasBasicDataWebserviceFacade_address = "http://110.249.133.136:6890/ormrpc/services/WSEasBasicDataWebserviceFacade";

    public String getWSEasBasicDataWebserviceFacadeAddress() {
        return this.basicUrl;
    }

    // The WSDD service name defaults to the port name.
    private String WSEasBasicDataWebserviceFacadeWSDDServiceName = "WSEasBasicDataWebserviceFacade";

    public String getWSEasBasicDataWebserviceFacadeWSDDServiceName() {
        return WSEasBasicDataWebserviceFacadeWSDDServiceName;
    }

    public void setWSEasBasicDataWebserviceFacadeWSDDServiceName(String name) {
        WSEasBasicDataWebserviceFacadeWSDDServiceName = name;
    }

    public com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSrvProxy getWSEasBasicDataWebserviceFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(basicUrl);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSEasBasicDataWebserviceFacade(endpoint);
    }

    public com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSrvProxy getWSEasBasicDataWebserviceFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSoapBindingStub _stub = new com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSEasBasicDataWebserviceFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSEasBasicDataWebserviceFacadeEndpointAddress(String address) {
        basicUrl = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSoapBindingStub _stub = new com.yiling.open.webservice.basic.WSEasBasicDataWebserviceFacadeSoapBindingStub(new java.net.URL(basicUrl), this);
                _stub.setPortName(getWSEasBasicDataWebserviceFacadeWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("WSEasBasicDataWebserviceFacade".equals(inputPortName)) {
            return getWSEasBasicDataWebserviceFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://110.249.133.136:6890/ormrpc/services/WSEasBasicDataWebserviceFacade", "WSEasBasicDataWebserviceFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://110.249.133.136:6890/ormrpc/services/WSEasBasicDataWebserviceFacade", "WSEasBasicDataWebserviceFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("WSEasBasicDataWebserviceFacade".equals(portName)) {
            setWSEasBasicDataWebserviceFacadeEndpointAddress(address);
        }
        else
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

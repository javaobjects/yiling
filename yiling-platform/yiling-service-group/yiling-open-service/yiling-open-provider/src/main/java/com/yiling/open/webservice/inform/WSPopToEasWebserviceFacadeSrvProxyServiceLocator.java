/**
 * WSPopToEasWebserviceFacadeSrvProxyServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yiling.open.webservice.inform;

import org.springframework.stereotype.Service;

/**
 * @author shuan
 */
@Service("wsPopToEasWebserviceFacadeSrvProxyServiceLocator")
public class WSPopToEasWebserviceFacadeSrvProxyServiceLocator extends org.apache.axis.client.Service implements com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSrvProxyService {

//    @Value("${eas.inform.webservice.url}")
    private String informUrl;

    public WSPopToEasWebserviceFacadeSrvProxyServiceLocator() {
    }


    public WSPopToEasWebserviceFacadeSrvProxyServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSPopToEasWebserviceFacadeSrvProxyServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSPopToEasWebserviceFacade
//    private String WSPopToEasWebserviceFacade_address = "http://110.249.133.136:6890/ormrpc/services/WSPopToEasWebserviceFacade";

    public String getWSPopToEasWebserviceFacadeAddress() {
        return informUrl;
    }

    // The WSDD service name defaults to the port name.
    private String WSPopToEasWebserviceFacadeWSDDServiceName = "WSPopToEasWebserviceFacade";

    public String getWSPopToEasWebserviceFacadeWSDDServiceName() {
        return WSPopToEasWebserviceFacadeWSDDServiceName;
    }

    public void setWSPopToEasWebserviceFacadeWSDDServiceName(String name) {
        WSPopToEasWebserviceFacadeWSDDServiceName = name;
    }

    public com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSrvProxy getWSPopToEasWebserviceFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(informUrl);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSPopToEasWebserviceFacade(endpoint);
    }

    public com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSrvProxy getWSPopToEasWebserviceFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSoapBindingStub _stub = new com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSPopToEasWebserviceFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSPopToEasWebserviceFacadeEndpointAddress(String address) {
        informUrl = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSrvProxy.class.isAssignableFrom(serviceEndpointInterface)) {
                com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSoapBindingStub _stub = new com.yiling.open.webservice.inform.WSPopToEasWebserviceFacadeSoapBindingStub(new java.net.URL(informUrl), this);
                _stub.setPortName(getWSPopToEasWebserviceFacadeWSDDServiceName());
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
        if ("WSPopToEasWebserviceFacade".equals(inputPortName)) {
            return getWSPopToEasWebserviceFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://110.249.133.136:6890/ormrpc/services/WSPopToEasWebserviceFacade", "WSPopToEasWebserviceFacadeSrvProxyService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://110.249.133.136:6890/ormrpc/services/WSPopToEasWebserviceFacade", "WSPopToEasWebserviceFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

if ("WSPopToEasWebserviceFacade".equals(portName)) {
            setWSPopToEasWebserviceFacadeEndpointAddress(address);
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

package com.headout.vendor.plugins.utils

import javax.xml.ws.BindingProvider
import javax.xml.ws.Service


abstract class WebServicesApi<WSInterface, WSImplementation : Service>(
    protected val traceWebService: Boolean = false,
    protected val endpoint: String
) {
    abstract fun createWSImplementation() : WSImplementation
    abstract fun getWSInterface() : Class<WSInterface>

    protected val service by lazy { createWSImplementation() }

    protected val port by lazy { getSoap() }

    /**
     * Call this method to trace the SOAP communication
     */
    private fun traceWebServiceCalls() {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true")
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true")
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true")
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true")
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dumpTreshold", "999999")
    }

    open fun getBasicAuthenticationUserName() : String? = null
    open fun getBasicAuthenticationPassword() : String? = null

    private fun getSoap(): WSInterface {
        if (traceWebService) traceWebServiceCalls()
        var port: WSInterface? = null
        for (portName in service.getPorts()) {
            port = service.getPort(portName, getWSInterface())
            val requestContext = (port as BindingProvider).requestContext
            requestContext[BindingProvider.ENDPOINT_ADDRESS_PROPERTY] = endpoint
            val userName = getBasicAuthenticationUserName()
            if (userName != null) requestContext[BindingProvider.USERNAME_PROPERTY] = userName
            val password = getBasicAuthenticationPassword()
            if (password != null) requestContext[BindingProvider.PASSWORD_PROPERTY] = password
        }
        return port ?: throw Error("No ports found in the WSDL")
    }
}
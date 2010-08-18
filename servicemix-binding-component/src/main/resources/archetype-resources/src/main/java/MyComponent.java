package ${packageName};

import java.util.List;

import javax.jbi.servicedesc.ServiceEndpoint;

import org.apache.servicemix.common.DefaultComponent;
import org.apache.servicemix.common.Endpoint;

/**
 * @org.apache.xbean.XBean element="component"
 */
public class MyComponent extends DefaultComponent {

    private MyEndpointType[] endpoints;
    
    public MyEndpointType[] getEndpoints() {
        return endpoints;
    }
    
    public void setEndpoints(MyEndpointType[] endpoints) {
        this.endpoints = endpoints;
    }

    protected List getConfiguredEndpoints() {
        return asList(getEndpoints());
    }

    protected Class[] getEndpointClasses() {
        return new Class[] {MyConsumerEndpoint.class, MyProviderEndpoint.class};
    }

    protected Endpoint getResolvedEPR(ServiceEndpoint ep) throws Exception {
        MyProviderEndpoint endpoint = new MyProviderEndpoint(this, ep);
        // TODO: initialize endpoint here
        endpoint.activate();
        return endpoint;
    }

}

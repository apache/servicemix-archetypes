package ${packageName};

import javax.jbi.management.DeploymentException;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.NormalizedMessage;
import javax.jbi.servicedesc.ServiceEndpoint;
import javax.xml.namespace.QName;

import org.apache.servicemix.common.DefaultComponent;
import org.apache.servicemix.common.ServiceUnit;
import org.apache.servicemix.common.endpoints.ProviderEndpoint;

/**
 * @org.apache.xbean.XBean element="provider"
 */
public class MyProviderEndpoint extends ProviderEndpoint implements MyEndpointType {

    public MyProviderEndpoint() {
    }

    public MyProviderEndpoint(ServiceUnit serviceUnit, QName service, String endpoint) {
        super(serviceUnit, service, endpoint);
    }

    public MyProviderEndpoint(DefaultComponent component, ServiceEndpoint endpoint) {
        super(component, endpoint);
    }

    public void validate() throws DeploymentException {
        super.validate();
    }
    
    protected void processInOnly(MessageExchange exchange, NormalizedMessage in) throws Exception {
        throw new UnsupportedOperationException("Unsupported MEP: " + exchange.getPattern());
    }

    protected void processInOut(MessageExchange exchange, NormalizedMessage in, NormalizedMessage out) throws Exception {
        throw new UnsupportedOperationException("Unsupported MEP: " + exchange.getPattern());
    }

}

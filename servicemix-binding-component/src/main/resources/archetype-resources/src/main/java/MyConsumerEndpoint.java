package ${packageName};

import javax.jbi.management.DeploymentException;
import javax.jbi.messaging.MessageExchange;

import org.apache.servicemix.common.endpoints.ConsumerEndpoint;

/**
 * @org.apache.xbean.XBean element="consumer"
 */
public class MyConsumerEndpoint extends ConsumerEndpoint implements MyEndpointType {

    public void validate() throws DeploymentException {
        super.validate();
    }
    
    public String getLocationURI() {
        // TODO: return a URI that unique identify this endpoint
        return getService() +  "#" + getEndpoint(); 
    }
    
    public void start() throws Exception {
        super.start();
        // TODO: Add code to start listening to incoming requests 
    }
    
    public void stop() throws Exception {
        // TODO: stop listenening
        super.stop();
    }

    public void process(MessageExchange exchange) throws Exception {
        // TODO: As we act as a consumer (we just send JBI exchanges)
        // we will receive responses or DONE / ERROR status here
    }

}

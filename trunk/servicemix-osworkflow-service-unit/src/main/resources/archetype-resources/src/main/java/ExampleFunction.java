package ${packageName};

import java.util.Map;

import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.xml.transform.dom.DOMSource;

import org.apache.servicemix.jbi.jaxp.SourceTransformer;
import org.apache.servicemix.osworkflow.OSWorkflow;
import org.apache.servicemix.osworkflow.OSWorkflowEndpoint;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Node;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

public class ExampleFunction implements FunctionProvider {
    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.workflow.FunctionProvider#execute(java.util.Map,
     *      java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    public void execute(Map transientVars, Map args, PropertySet propertySet) throws WorkflowException
        {
        NormalizedMessage msg = null;
        OSWorkflowEndpoint ep = null;
        boolean isAsynchron = false;
        MessageExchange exchange = null;
        
        if (transientVars.containsKey(OSWorkflow.KEY_ASYNC_PROCESSING))
            {
            isAsynchron = (Boolean)transientVars.get(OSWorkflow.KEY_ASYNC_PROCESSING);
            }
        else
            {
            throw new WorkflowException("ExampleFunction: Missing transient variable for async object. (" + OSWorkflow.KEY_ASYNC_PROCESSING + ")");
            }

        if (transientVars.containsKey(OSWorkflow.KEY_EXCHANGE))
            {
            exchange = (MessageExchange)transientVars.get(OSWorkflow.KEY_EXCHANGE);
            }
        else
            {
            throw new WorkflowException("ExampleFunction: Missing transient variable for exchange object. (" + OSWorkflow.KEY_EXCHANGE + ")");
            }
        
        if (transientVars.containsKey(OSWorkflow.KEY_IN_MESSAGE))
            {
            msg = (NormalizedMessage)transientVars.get(OSWorkflow.KEY_IN_MESSAGE);
            }
        else
            {
            throw new WorkflowException("ExampleFunction: Missing transient variable for message object. (" + OSWorkflow.KEY_IN_MESSAGE + ")");
            }
        
        if (transientVars.containsKey(OSWorkflow.KEY_ENDPOINT))
            {
            ep = (OSWorkflowEndpoint)transientVars.get(OSWorkflow.KEY_ENDPOINT);
            }
        else
            {
            throw new WorkflowException("ExampleFunction: Missing transient variable for endpoint object. (" + OSWorkflow.KEY_ENDPOINT + ")");
            }
        
        SourceTransformer sourceTransformer = new SourceTransformer();

        try
            {
            DOMSource inMessageXml = (DOMSource)sourceTransformer.toDOMSource(msg.getContent());

            // Parse out the actual message content
            CachedXPathAPI xpath = new CachedXPathAPI();
            
            Node exampleNode = xpath.selectSingleNode(inMessageXml.getNode(), "/example");
            
            if (exampleNode != null)
                {
                String value = exampleNode.getTextContent();
                propertySet.setString("ExampleKey", value);
                }
            else
                {
                throw new WorkflowException("ExampleFunction: Missing tag: example");
                }
            }
        catch (Exception ex)
            {
            throw new WorkflowException(ex);
            }
        finally
            {
            // if the exchange came as asynchronous inOnly or RobustInOnly
            // we will now send a receipt to the sender
            try
                {
                if (isAsynchron)
                    {
                    ep.done(exchange);
                    }
                }
            catch (MessagingException mex)
                {
                throw new WorkflowException(mex);
                }
            }
        }
}

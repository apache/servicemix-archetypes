package ${packageName};

import java.util.Map;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;

public class ExampleCondition implements Condition {
    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.workflow.Condition#passesCondition(java.util.Map,
     *      java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    public boolean passesCondition(Map transientVars, Map args, PropertySet propertySet) throws WorkflowException
        {
        boolean result = false;
        
        if (propertySet.exists("ExampleKey"))
            {
            if (propertySet.getString("ExampleKey").equals("ExampleValue"))
                {
                result = true;
                }
            else
                {
                result = false;
                }
            }
        else
            {
            result = false;
            }
        
        return result;
        }
}

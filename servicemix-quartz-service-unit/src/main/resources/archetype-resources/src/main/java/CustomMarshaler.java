import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;

import org.apache.servicemix.quartz.support.DefaultQuartzMarshaler;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CustomMarshaler extends DefaultQuartzMarshaler {

    @Override
    public void populateNormalizedMessage(NormalizedMessage message, JobExecutionContext context) throws JobExecutionException, MessagingException {
        super.populateNormalizedMessage(message, context);
        message.setContent(new StringSource((String) context.getJobDetail(), getJobDataMap().get("xml")));
    }

}
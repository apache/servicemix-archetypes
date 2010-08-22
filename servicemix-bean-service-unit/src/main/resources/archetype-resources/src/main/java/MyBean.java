package ${packageName};

import org.apache.servicemix.MessageExchangeListener;

import javax.annotation.Resource;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;

public class MyBean implements MessageExchangeListener {

    @Resource
    private DeliveryChannel channel;

    public void onMessageExchange(MessageExchange exchange) throws MessagingException {
        System.out.println("Received exchange: " + exchange);
        exchange.setStatus(ExchangeStatus.DONE);
        channel.send(exchange);
    }

}

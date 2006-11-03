/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${packageName};

import javax.jbi.component.ComponentContext;
import javax.jbi.management.DeploymentException;
import javax.jbi.messaging.DeliveryChannel;
import javax.jbi.messaging.ExchangeStatus;
import javax.jbi.messaging.InOut;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessageExchangeFactory;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.MessageExchange.Role;
import javax.jbi.messaging.NormalizedMessage;
import javax.jbi.servicedesc.ServiceEndpoint;

import org.apache.servicemix.common.BaseLifeCycle;
import org.apache.servicemix.common.Endpoint;
import org.apache.servicemix.common.ExchangeProcessor;

/**
 * @org.apache.xbean.XBean element="endpoint"
 */
public class MyEndpoint extends Endpoint implements ExchangeProcessor {

    private ServiceEndpoint activated;
    private DeliveryChannel channel;
    private MessageExchangeFactory exchangeFactory;
    
    /* (non-Javadoc)
     * @see org.apache.servicemix.common.Endpoint#getRole()
     */
    public Role getRole() {
        return Role.PROVIDER;
    }

    public void activate() throws Exception {
        logger = this.serviceUnit.getComponent().getLogger();
        ComponentContext ctx = getServiceUnit().getComponent().getComponentContext();
        channel = ctx.getDeliveryChannel();
        exchangeFactory = channel.createExchangeFactory();
        activated = ctx.activateEndpoint(service, endpoint);
        start();
    }

    public void deactivate() throws Exception {
        stop();
        ServiceEndpoint ep = activated;
        activated = null;
        ComponentContext ctx = getServiceUnit().getComponent().getComponentContext();
        ctx.deactivateEndpoint(ep);
    }

    public ExchangeProcessor getProcessor() {
        return this;
    }
    
    public void validate() throws DeploymentException {
    }
    
    protected void send(MessageExchange me) throws MessagingException {
        if (me.getRole() == MessageExchange.Role.CONSUMER &&
            me.getStatus() == ExchangeStatus.ACTIVE) {
            BaseLifeCycle lf = (BaseLifeCycle) getServiceUnit().getComponent().getLifeCycle();
            lf.sendConsumerExchange(me, (Endpoint) this);
        } else {
            channel.send(me);
        }
    }
    
    protected void done(MessageExchange me) throws MessagingException {
        me.setStatus(ExchangeStatus.DONE);
        send(me);
    }
    
    protected void fail(MessageExchange me, Exception error) throws MessagingException {
        me.setError(error);
        send(me);
    }
    
    public void start() throws Exception {
    }
    
    public void stop() {
    }

    public void process(MessageExchange exchange) throws Exception {
        // The component acts as a provider, this means that another component has requested our service
        // As this exchange is active, this is either an in or a fault (out are send by this component)
        if (exchange.getRole() == MessageExchange.Role.PROVIDER) {
            // Check here if the mep is supported by this component
            if (exchange instanceof InOut == false) {
               throw new UnsupportedOperationException("Unsupported MEP: " + exchange.getPattern());
            }
            // In message
            if (exchange.getMessage("in") != null) {
                NormalizedMessage in = exchange.getMessage("in");
                // TODO ... handle the in message
                // If the MEP is an InOnly, RobustInOnly, you have to set the exchange to DONE status
                // else, you have to create an Out message and populate it
                // For now, just echo back
                NormalizedMessage out = exchange.createMessage();
                out.setContent(in.getContent());
                exchange.setMessage(out, "out");
                channel.send(exchange);
            // Fault message
            } else if (exchange.getFault() != null) {
                // TODO ... handle the fault
                exchange.setStatus(ExchangeStatus.DONE);
                channel.send(exchange);
            // This is not compliant with the default MEPs
            } else {
                throw new IllegalStateException("Provider exchange is ACTIVE, but no in or fault is provided");
            }
        // The component acts as a consumer, this means this exchange is received because
        // we sent it to another component.  As it is active, this is either an out or a fault
        // If this component does not create / send exchanges, you may just throw an UnsupportedOperationException
        } else if (exchange.getRole() == MessageExchange.Role.CONSUMER) {
            // Exchange is finished
            if (exchange.getStatus() == ExchangeStatus.DONE) {
                return;
            // Exchange has been aborted with an exception
            } else if (exchange.getStatus() == ExchangeStatus.ERROR) {
                return;
            // Exchange is active
            } else {
                // Out message
                if (exchange.getMessage("out") != null) {
                    // TODO ... handle the response
                    exchange.setStatus(ExchangeStatus.DONE);
                    channel.send(exchange);
                // Fault message
                } else if (exchange.getFault() != null) {
                    // TODO ... handle the fault
                    exchange.setStatus(ExchangeStatus.DONE);
                    channel.send(exchange);
                // This is not compliant with the default MEPs
                } else {
                    throw new IllegalStateException("Consumer exchange is ACTIVE, but no out or fault is provided");
                }
            }
        // Unknown role
        } else {
            throw new IllegalStateException("Unkown role: " + exchange.getRole());
        }
    }

}

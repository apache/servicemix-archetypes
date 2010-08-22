package ${packageName};

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Router
 */
public class MyRouteBuilder extends RouteBuilder {

    public void configure() {

        // TODO create Camel routes here. For example:-
        //
        // from("activemq:test.MyQueue").to("file://test");

    }
}

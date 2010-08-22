package ${packageName};

import javax.jbi.messaging.InOut;
import javax.xml.namespace.QName;

import org.apache.servicemix.client.DefaultServiceMixClient;
import org.apache.servicemix.jbi.jaxp.StringSource;
import org.apache.servicemix.tck.SpringTestSupport;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;

public class MySpringComponentTest extends SpringTestSupport {

    public MySpringComponentTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        DefaultServiceMixClient client = new DefaultServiceMixClient(jbi);
        InOut me = client.createInOutExchange();
        me.setService(new QName("urn:test", "service"));
        me.getInMessage().setContent(new StringSource("<hello>world</hello>"));
        client.sendSync(me);
        // By default, the endpoint throws an UnsupportedOperationException
        // so do not test anything here
    }
    
    protected AbstractXmlApplicationContext createBeanFactory() {
        return new ClassPathXmlApplicationContext("spring.xml");
    }
    
}

package ${packageName};

import org.apache.servicemix.jbi.container.SpringJBIContainer;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // This is a very simple example of how you might embed ServiceMix
        try {
            final ApplicationContext context = new ClassPathXmlApplicationContext("servicemix.xml");

            SpringJBIContainer container = (SpringJBIContainer) context.getBean("jbi");
            container.onShutDown(new Runnable() {
                public void run() {
                    if (context instanceof DisposableBean) {
                        try {
                            ((DisposableBean) context).destroy();
                        } catch (Exception e) {
                            System.out.println("Caught: " + e);
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }

    }

}

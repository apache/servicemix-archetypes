package com.logicblaze.sample;

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
			ApplicationContext context = null;

			context = new ClassPathXmlApplicationContext("servicemix.xml");

			SpringJBIContainer container = (SpringJBIContainer) context
					.getBean("jbi");
			Object lock = new Object();
			container.setShutdownLock(lock);

			// lets wait until we're killed.
			synchronized (lock) {
				lock.wait();
			}
			if (context instanceof DisposableBean) {
				((DisposableBean) context).destroy();
			}
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}

	}

}

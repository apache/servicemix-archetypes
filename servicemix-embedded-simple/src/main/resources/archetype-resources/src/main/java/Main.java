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

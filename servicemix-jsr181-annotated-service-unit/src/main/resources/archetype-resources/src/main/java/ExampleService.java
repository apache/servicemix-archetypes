package ${packageName};

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "ExampleService", targetNamespace = "http://example.com/exampleService")
public class ExampleService {

	@WebMethod
	public String sayHello(String name) {
		return "Hello "+name;
	}
}

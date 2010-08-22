package ${packageName};

import javax.jws.WebService;
import javax.xml.ws.Holder;
import org.apache.servicemix.samples.wsdl_first.Person;
import org.apache.servicemix.samples.wsdl_first.UnknownPersonFault;
import org.apache.servicemix.samples.wsdl_first.types.GetPerson;
import org.apache.servicemix.samples.wsdl_first.types.GetPersonResponse;

@WebService(serviceName = "PersonService", targetNamespace = "http://servicemix.apache.org/samples/wsdl-first", endpointInterface = "org.apache.servicemix.samples.wsdl_first.Person")

public class PersonImpl implements Person {

    public void getPerson(Holder<String> personId, Holder<String> ssn, Holder<String> name)
        throws UnknownPersonFault
    {
        if (personId.value == null || personId.value.length() == 0) {
            org.apache.servicemix.samples.wsdl_first.types.UnknownPersonFault fault = new org.apache.servicemix.samples.wsdl_first.types.UnknownPersonFault();
            fault.setPersonId(personId.value);
            throw new UnknownPersonFault(null,fault);
        }
        name.value = "Guillaume";
        ssn.value = "000-000-0000";
    }

}

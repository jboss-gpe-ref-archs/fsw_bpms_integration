package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.TestCase;

public class JaxbTest extends TestCase{

    public void testJaxb() throws Exception {
        Policy policyObj = new Policy(1, "MyPolicy");
        JAXBContext jaxbContext = JAXBContext.newInstance(Policy.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(policyObj, sw);
        System.out.println("main() policy = "+sw.toString());
    }
}

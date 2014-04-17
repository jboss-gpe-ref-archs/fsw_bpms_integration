package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="policy")
public class Policy implements java.io.Serializable{

    private int policyId;

    public Policy() {}

    public Policy(int policyId){
        this.policyId = policyId;
    }

    public String toString(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("policyId = ");
        sBuilder.append(policyId);
        return sBuilder.toString();
    }

    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int x) {
        policyId = x;
    }
}

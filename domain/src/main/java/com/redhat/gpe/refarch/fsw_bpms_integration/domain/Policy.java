package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="policy")
public class Policy implements java.io.Serializable{

    private int policyId;
    private String policyName;

    public Policy() {}

    public Policy(int policyId, String policyName){
        this.policyId = policyId;
        this.policyName = policyName;
    }

    public String toString(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("policyId = ");
        sBuilder.append(policyId);
        sBuilder.append("policyName = ");
        sBuilder.append(policyName);
        return sBuilder.toString();
    }

    public int getPolicyId() {
        return policyId;
    }
    public void setPolicyId(int x) {
        policyId = x;
    }
    public String getPolicyName() {
        return policyName;
    }
    public void setPolicyName(String x) {
        policyName = x;
    }
}

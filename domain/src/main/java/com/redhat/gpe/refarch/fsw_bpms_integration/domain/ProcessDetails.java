package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

public class ProcessDetails {

    private String deploymentId;
    private String processId;
    private String policyJaxb;
    private boolean executeTaskLifecycle;

    public ProcessDetails() {}

    public ProcessDetails(String deploymentId, String processId, String policyJaxb, boolean executeTaskLifecycle){
        this.deploymentId = deploymentId;
        this.processId = processId;
        this.policyJaxb = policyJaxb;
        this.executeTaskLifecycle = executeTaskLifecycle;
    }

    public String toString(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("\n\tdeploymentId = ");
        sBuilder.append(deploymentId);
        sBuilder.append("\n\tprocessId = ");
        sBuilder.append(processId);
        sBuilder.append("\n\tpolicyJaxb = ");
        sBuilder.append(policyJaxb);
        sBuilder.append("\n\texecuteTaskLifecycle = ");
        sBuilder.append(executeTaskLifecycle);
        return sBuilder.toString();
    }

    public String getDeploymentId() {
        return deploymentId;
    }
    public void setDeploymentId(String x) {
        deploymentId = x;
    }
    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String x) {
        processId = x;
    }
    public String getPolicyJaxb() {
        return policyJaxb;
    }
    public void setPolicyJaxb(String x) {
        policyJaxb = x;
    }
    public boolean getExecuteTaskLifecycle() {
        return executeTaskLifecycle;
    }
    public void setExecuteTaskLifecycle(boolean x) {
        executeTaskLifecycle = x;
    }
}

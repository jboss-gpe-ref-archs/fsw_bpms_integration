package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

public class ProcessDetails {

    private String deploymentId;
    private String processId;
    private boolean executeTaskLifecycle;
    private String payload;

    public ProcessDetails() {}

    public ProcessDetails(String deploymentId, String processId, boolean executeTaskLifecycle){
        this.deploymentId = deploymentId;
        this.processId = processId;
        this.executeTaskLifecycle = executeTaskLifecycle;
    }

    public String toString(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("\n\tdeploymentId = ");
        sBuilder.append(deploymentId);
        sBuilder.append("\n\tprocessId = ");
        sBuilder.append(processId);
        sBuilder.append("\n\texecuteTaskLifecycle = ");
        sBuilder.append(executeTaskLifecycle);
        sBuilder.append("\n\tpayload = ");
        sBuilder.append(payload);
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
    public boolean getExecuteTaskLifecycle() {
        return executeTaskLifecycle;
    }
    public void setExecuteTaskLifecycle(boolean x) {
        executeTaskLifecycle = x;
    }
    public String getPayload() {
        return payload;
    }
    public void setPayload(String x) {
        payload = x;
    }
}

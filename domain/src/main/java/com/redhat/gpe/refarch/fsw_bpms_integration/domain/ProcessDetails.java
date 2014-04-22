package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

public class ProcessDetails {

    private String deploymentId;
    private String processId;

    public ProcessDetails() {}

    public ProcessDetails(String deploymentId, String processId){
        this.deploymentId = deploymentId;
        this.processId = processId;
    }

    public String toString(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("\n\tdeploymentId = ");
        sBuilder.append(deploymentId);
        sBuilder.append("\n\tprocessId = ");
        sBuilder.append(processId);
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
}

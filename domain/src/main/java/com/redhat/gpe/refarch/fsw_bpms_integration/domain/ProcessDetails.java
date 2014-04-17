package com.redhat.gpe.refarch.fsw_bpms_integration.domain;

public class ProcessDetails {

    private String processId;

    public ProcessDetails() {}

    public ProcessDetails(String processId){
        this.processId = processId;
    }

    public String toString(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("processId = ");
        sBuilder.append(processId);
        return sBuilder.toString();
    }

    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String x) {
        processId = x;
    }
}

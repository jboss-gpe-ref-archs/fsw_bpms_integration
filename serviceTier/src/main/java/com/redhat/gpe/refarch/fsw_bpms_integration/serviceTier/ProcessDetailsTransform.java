package com.redhat.gpe.refarch.fsw_bpms_integration.serviceTier;

import java.io.IOException;

import com.redhat.gpe.refarch.fsw_bpms_integration.domain.ProcessDetails;
import org.codehaus.jackson.map.ObjectMapper;
import org.switchyard.annotations.Transformer;

/*
 * purpose:  transform JSON representation of ProcessDetails to equivalent Object
 */
public final class ProcessDetailsTransform {
    
    private ObjectMapper jsonMapper = new ObjectMapper();

    @Transformer
    public ProcessDetails transformStringToPolicyBinding(String from) throws IOException {
        return jsonMapper.readValue(from, ProcessDetails.class);
    }

}

package com.surveine.memberservice.config.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {
    private String type;
    private boolean optional;
    private String field;
}

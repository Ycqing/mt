package com.merchantanalysis.dto.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class CompareRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String merchantIds;
    private String compareDimensions;
    private String summary;
    private String createdBy;
}

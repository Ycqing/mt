package com.merchantanalysis.common.enums;

public enum DataStatus {
    PENDING(0, "待采集"),
    FETCHING(1, "采集中"),
    COMPLETED(2, "已完成"),
    FAILED(3, "失败");

    private final Integer code;
    private final String desc;

    DataStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DataStatus fromCode(Integer code) {
        for (DataStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return PENDING;
    }
}

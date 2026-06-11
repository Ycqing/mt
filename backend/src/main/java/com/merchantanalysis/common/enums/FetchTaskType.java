package com.merchantanalysis.common.enums;

public enum FetchTaskType {
    WEBSITE("website", "官网采集"),
    FINANCIAL("financial", "财报采集"),
    REVIEW("review", "评价采集"),
    PRODUCT("product", "产品采集"),
    SOCIAL("social", "社交账号采集"),
    OPERATING("operating", "经营数据采集");

    private final String code;
    private final String desc;

    FetchTaskType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

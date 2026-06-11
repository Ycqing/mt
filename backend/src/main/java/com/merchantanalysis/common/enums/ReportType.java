package com.merchantanalysis.common.enums;

public enum ReportType {
    FULL("full", "完整报告"),
    MARKETING("marketing", "营销报告"),
    FINANCIAL("financial", "财务报告"),
    COMPARISON("comparison", "对比报告"),
    PRODUCT("product", "产品报告");

    private final String code;
    private final String desc;

    ReportType(String code, String desc) {
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

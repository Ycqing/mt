package com.merchantanalysis.common.enums;

public enum Platform {
    DOUYIN("douyin", "抖音"),
    XIAOHONGSHU("xiaohongshu", "小红书"),
    WEIBO("weibo", "微博"),
    WEIXIN("weixin", "微信"),
    REDBOOK("redbook", "小红书"),
    TAOBAO("taobao", "淘宝"),
    JD("jd", "京东"),
    FEIGUA("feigua", "飞瓜"),
    CHANMAMA("chanmama", "蝉妈妈"),
    FAXIAN("faxian", "发现报告"),
    MANUAL("manual", "手动录入");

    private final String code;
    private final String desc;

    Platform(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static Platform fromCode(String code) {
        for (Platform platform : values()) {
            if (platform.code.equalsIgnoreCase(code)) {
                return platform;
            }
        }
        return MANUAL;
    }
}

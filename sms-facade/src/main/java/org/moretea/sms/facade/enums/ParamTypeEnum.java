package org.moretea.sms.facade.enums;

public enum ParamTypeEnum {
    LIST("List"),
    MAP("Map");

    private final String type;

    ParamTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    // 获取枚举实例
    public static ParamTypeEnum fromString(String text) {
        for (ParamTypeEnum e : ParamTypeEnum.values()) {
            if (e.type.equalsIgnoreCase(text)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
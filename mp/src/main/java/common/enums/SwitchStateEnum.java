package common.enums;

public enum SwitchStateEnum {
    OPEN(1,"开启"),
    CLOSE(2,"关闭");

    private Integer type;
    private String des;
    SwitchStateEnum(Integer type, String des){
        this.type=type;
        this.des=des;
    }

    public Integer getType() {
        return type;
    }
    public String getDes() {
        return des;
    }

    public static String getDesc(Integer type) {
        SwitchStateEnum[] CardUseEnums = values();
        for (SwitchStateEnum cardUseEnum : CardUseEnums) {
            if (cardUseEnum.getType().equals(type)) {
                return cardUseEnum.getDes();
            }
        }
        return null;
    }
    public static Integer getTypeByDesc(String desc) {
        SwitchStateEnum[] CardUseEnums = values();
        for (SwitchStateEnum cardUseEnum : CardUseEnums) {
            if (cardUseEnum.getDes().equals(desc)) {
                return cardUseEnum.getType();
            }
        }
        return null;
    }

}

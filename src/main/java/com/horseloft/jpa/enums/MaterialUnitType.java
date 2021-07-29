package com.horseloft.jpa.enums;

/**
 * Date: 2020/2/9 下午1:25
 * User: YHC
 * Desc: 物料单位类型
 */
public enum MaterialUnitType {
    //3体积，，
    NUMBER(1, "数量"),
    LENGTH(2, "长度"),
    VOLUME(3, "体积"),
    WEIGHT(4, "重量"),
    MONEY(5, "金钱");

    private final String name;

    MaterialUnitType(Integer value, String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static String getName(Integer typeId) {
        String name;
        switch (typeId) {
            case 1:
                name = MaterialUnitType.NUMBER.toString();
                break;
            case 2:
                name = MaterialUnitType.LENGTH.toString();
               break;
            case 3:
                name = MaterialUnitType.VOLUME.toString();
                break;
            case 4:
                name = MaterialUnitType.WEIGHT.toString();
                break;
            case 5:
                name = MaterialUnitType.MONEY.toString();
                break;
            default:
                name = "";
                break;
        }
        return name;
    }
}

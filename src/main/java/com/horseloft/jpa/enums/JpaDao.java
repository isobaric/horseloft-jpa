package com.horseloft.jpa.enums;

/**
 * Date: 2020/2/4 下午6:31
 * User: YHC
 * Desc:
 */
public enum JpaDao {
    DEFAULT(""),
    USER("userDao"),
    SHELF("shelfDao"),
    DEVICE("deviceDao"),
    STATION("stationDao"),
    ;

    private final String name;

    JpaDao(String dao) {
        this.name = dao;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

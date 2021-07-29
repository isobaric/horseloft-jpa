package com.horseloft.jpa.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Date: 2020/1/7 15:46
 * User: YHC
 * Desc: 行政区
 */
@Getter
@Setter
@Entity
@Table(name = "jpa_district")
public class District implements Serializable {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pid;

    private String name;

    private String shortName;

    private String mergeName;

    private Integer rank;
}

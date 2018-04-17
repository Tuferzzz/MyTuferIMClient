package com.tufer.factory.model;

/**
 * 基础用户接口
 *
 * @author Tufer
 * @version 1.0.0
 */
public interface Author {
    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getPortrait();

    void setPortrait(String portrait);
}

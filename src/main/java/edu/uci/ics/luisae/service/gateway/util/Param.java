package edu.uci.ics.luisae.service.gateway.util;

public class Param {
    private Integer type;
    private Object  param;

    private Param(Integer type, Object param)
    {
        this.type = type;
        this.param = param;
    }

    public static Param create(Integer type, Object param)
    {
        return new Param(type, param);
    }

    Integer getType()
    {
        return type;
    }

    Object getParam()
    {
        return param;
    }
}

package com.tufer.mvptest;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class User implements IUser{
    @Override
    public String search(String s) {
        return "User:" + s.hashCode();
    }
}

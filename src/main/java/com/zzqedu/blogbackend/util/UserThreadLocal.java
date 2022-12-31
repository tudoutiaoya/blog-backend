package com.zzqedu.blogbackend.util;

import com.zzqedu.blogbackend.dao.pojo.SysUser;

public class UserThreadLocal {

    public UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}

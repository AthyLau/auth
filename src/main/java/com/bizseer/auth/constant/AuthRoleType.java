package com.bizseer.auth.constant;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 11:06 AM
 * @since JDK 1.8
 */
public enum  AuthRoleType {
    SUPER_ADMIN("superadmin",10),ADMIN("admin",5),VIEWER("viewer",1);

    private String name;
    private Integer power;

    AuthRoleType(String name,Integer power){
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public Integer getPower() {
        return power;
    }

    public static AuthRoleType getAuthRoleByName(String name){
        switch (name){
            case "superadmin":
                return SUPER_ADMIN;
            case "admin":
                return ADMIN;
            case "viewer":
                return VIEWER;
            default:
                return null;
        }
    }
}

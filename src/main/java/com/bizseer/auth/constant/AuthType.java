package com.bizseer.auth.constant;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 10:53 AM
 * @since JDK 1.8
 */
public enum AuthType {
    NONE("none"), SELF("self"), OAUTH2("oauth2"), CAS("cas"), LDAP("ldap");

    private final String name;
    AuthType(String name) {
        this.name = name;
    }

    public static AuthType from(String str) {
        switch (str) {
            case "self": return SELF;
            case "oauth2": return OAUTH2;
            case "cas": return CAS;
            case "ldap": return LDAP;
            default: return NONE;
        }
    }

    @Override
    public String toString() {
        return name;
    }

}

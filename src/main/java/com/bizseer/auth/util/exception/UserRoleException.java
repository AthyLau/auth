package com.bizseer.auth.util.exception;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 4:07 PM
 * @since JDK 1.8
 */
public class UserRoleException extends AuthException{
    public UserRoleException() {
        super("User roles exception！");
    }
    public UserRoleException(String message) {
        super(message);
    }
}

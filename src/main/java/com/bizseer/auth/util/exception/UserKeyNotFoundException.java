package com.bizseer.auth.util.exception;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 2:13 PM
 * @since JDK 1.8
 */
public class UserKeyNotFoundException extends AuthException{

    public UserKeyNotFoundException() {
        super("The key for the user information was not found！");
    }
    public UserKeyNotFoundException(String message) {
        super(message);
    }
}

package com.bizseer.auth.util.exception;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 4:10 PM
 * @since JDK 1.8
 */
public class UserExistException extends AuthException {

    public UserExistException() {
        super("The user exists exception!");
    }

    public UserExistException(String message) {
        super(message);
    }
}

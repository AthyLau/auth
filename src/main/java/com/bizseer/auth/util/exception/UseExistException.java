package com.bizseer.auth.util.exception;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 4:10 PM
 * @since JDK 1.8
 */
public class UseExistException extends AuthException {
    public UseExistException() {
        super("The user exists exception!");
    }
    public UseExistException(String message) {
        super(message);
    }
}

package com.bizseer.auth.util.exception;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 10:59 AM
 * @since JDK 1.8
 */
public class UnauthorizedException extends AuthException {

    public UnauthorizedException() {
        super("The user has not been authenticated!");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}

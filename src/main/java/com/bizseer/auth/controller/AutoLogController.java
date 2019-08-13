package com.bizseer.auth.controller;

import com.bizseer.auth.util.http.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class AutoLogController {

    private Logger logger;

    private Logger logger(){
        if (logger == null)
            logger = LoggerFactory.getLogger(this.getClass());
        return logger;
    }

    @ExceptionHandler(Exception.class)
    public JsonResponse handleException(HttpServletRequest req, Exception e) {
        if (e.getMessage().equals("Broken pipe") || e.getMessage().equals("Connection reset by peer")) {
            logger().info("client socket is closed");
            return null;
        }
        if (e instanceof MethodArgumentTypeMismatchException || e instanceof MissingServletRequestParameterException||
            e.getMessage().contains("error parsing query: overflowed duration"))
            logger().debug(req.toString(), e);
        else {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append(req.toString());
            errorMessage.append("\nQueryString: ");
            errorMessage.append(req.getQueryString());
            if (req.getMethod().equals("POST") || req.getMethod().equals("PUT")) {
                try {
                    errorMessage.append("\nRequest Body: ");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
                    String line;
                    while ( (line = reader.readLine()) != null )
                        errorMessage.append(line);
                } catch (IOException ioe) {
                    logger().error(req.toString(), ioe);
                }
            }
            logger().error(errorMessage.toString(), e);
        }
        return new JsonResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}

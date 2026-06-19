package it.intesys.codylab.rookie.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Optional;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ExceptionTranslator.class);

    private static final String MESSAGE_KEY      = "message";

    @ExceptionHandler(ServiceException.class)
    ProblemDetail handle(ServiceException e) {
        logger.info("An error has occurred", e);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String title = e.getMessage();
        logger.info("An error has occurred: {}", e.getMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now());

        String messageValue = (String) Optional.of(problemDetail).map(ProblemDetail::getProperties).map(p -> p.get(MESSAGE_KEY)).orElse(null);
        if (StringUtils.isBlank(messageValue) && problemDetail.getStatus() != 0 && !problemDetail.getProperties().containsKey(MESSAGE_KEY)) {
            problemDetail.setProperty(MESSAGE_KEY, "error.http." + problemDetail.getStatus());
        }
        return problemDetail;
    }


}

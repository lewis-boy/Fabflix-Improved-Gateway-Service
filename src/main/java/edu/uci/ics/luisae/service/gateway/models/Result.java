package edu.uci.ics.luisae.service.gateway.models;

import javax.ws.rs.core.Response.Status;

public enum Result {

    PLEVEL_OUT_OF_RANGE  (-14, "Privilege level out of valid range.", Status.BAD_REQUEST),
    TOKEN_INVALID_LENGTH (-13, "Token has invalid length.",Status.BAD_REQUEST),
    PASSWORD_INVALID_LENGTH (-12, "Password has invalid length",Status.BAD_REQUEST),
    EMAIL_INVALID_FORMAT (-11, "Email address has invalid format",Status.BAD_REQUEST),
    EMAIL_INVALID_LENGTH (-10, "Email address has invalid length",Status.BAD_REQUEST),
    JSON_PARSE_EXCEPTION   (-3, "JSON parse exception",   Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Status.BAD_REQUEST),
    PASSWORD_LENGTH_UNSATISFIED (12, "Password does not meet length requirements.", Status.BAD_REQUEST),
    PASSWORD_CHARS_UNSATISFIED(13, "Password does not meet character requirements.", Status.OK),

    INTERNAL_SERVER_ERROR  (-1, "Internal Server Error.",  Status.INTERNAL_SERVER_ERROR),
    COULD_NOT_FIND_RESULTCODE (0, "Could not find resultCode in constructor.", Status.OK),

    PASSWORD_DONT_MATCH             (11, "Passwords do not match.",Status.OK),
    USER_NOT_FOUND(14,"User not found.",Status.OK),
    EMAIL_ALREADY_IN_USE(16,"Email already in use.", Status.OK),
    REGISTER_SUCCESS(110, "User registered successfully.", Status.OK),
    LOGGED_IN_SUCCESSFULLY(120, "User logged in successfully",Status.OK),
    SESSION_ACTIVE(130, "Session is active.", Status.OK),
    SESSION_EXPIRED(131, "Session is expired.", Status.OK),
    SESSION_CLOSED(132, "Session is closed.",Status.OK),
    SESSION_REVOKED(133, "Session is revoked.",Status.OK),
    SESSION_NOT_FOUND(134, "Session not found.",Status.OK),
    SUFFICIENT_PLEVEL(140,"User has sufficient privilege level.",Status.OK),
    INSUFFICIENT_PLEVEL(141,"User has insufficient privilege level.",Status.OK);





    private final int    resultCode;
    private final String message;
    private final Status status;

    Result(int resultCode, String message, Status status){
        this.resultCode = resultCode;
        this.message = message;
        this.status = status;
    }

    Result(int resultCode, String message)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.status = Status.OK;
    }



    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getResultCode() {
        return resultCode;
    }
}

package com.wag.challenge.network;

/**
 * Created by PGomez on 9/21/2017.
 */

public class ChallengeResponse<T> {
    private final Class<T> responseBodyClass;
    private int code;
    private String message;
    private T responseBody;

    protected ChallengeResponse(final Class<T> responseBodyClass) {
        this.responseBodyClass = responseBodyClass;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public Class<T> getResponseBodyClass() {
        return responseBodyClass;
    }
}

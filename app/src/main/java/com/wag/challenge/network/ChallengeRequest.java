package com.wag.challenge.network;

/**
 * Created by PGomez on 9/21/2017.
 */

public class ChallengeRequest {
    final String url;
    final String method;
    final Object body;


    public ChallengeRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Object getBody() {
        return body;
    }

    public static class Builder{
        String url;
        String method;
        Object body;

        public Builder() {
            this.method = "GET";
        }

        public UserListRequest.Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public ChallengeRequest.Builder body (Object body) {
            if(this.method.equalsIgnoreCase("GET")) {
                throw new UnsupportedOperationException("Method GET it is incompatible with a body, please set method before body");
            }
            this.body = body;
            return this;
        }

        public ChallengeRequest build() {
            if (url == null) throw new IllegalStateException("url == null");
            return new ChallengeRequest(this);
        }
    }
}

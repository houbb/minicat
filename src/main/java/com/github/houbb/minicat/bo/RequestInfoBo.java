package com.github.houbb.minicat.bo;

public class RequestInfoBo {

    private String url;

    private String method;

    public RequestInfoBo(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "RequestInfoBo{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

}

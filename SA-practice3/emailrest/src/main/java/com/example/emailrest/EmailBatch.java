package com.example.emailrest;

public class EmailBatch {
    private String [] urls;
    private String _payload;

    public String get_payload() {
        return _payload;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public void set_payload(String _payload) {
        this._payload = _payload;
    }
}

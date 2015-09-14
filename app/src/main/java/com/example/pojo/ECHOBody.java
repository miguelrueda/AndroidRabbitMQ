package com.example.pojo;

public class ECHOBody implements java.io.Serializable {

    private static final long serialVersionUID = -1L;
    private String resourceKey;
    private long resourceStatusKey;

    public ECHOBody() {

    }

    public ECHOBody(String resourceKey, long resourceStatusKey) {
        this.resourceKey = resourceKey;
        this.resourceStatusKey = resourceStatusKey;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public long getResourceStatusKey() {
        return resourceStatusKey;
    }

    public void setResourceStatusKey(long resourceStatusKey) {
        this.resourceStatusKey = resourceStatusKey;
    }

    @Override
    public String toString() {
        return "ECHOBody{" +
                "resourceKey='" + resourceKey + '\'' +
                ", resourceStatusKey=" + resourceStatusKey +
                '}';
    }
}

package com.example.pojo;

public class ECHOHeader implements java.io.Serializable {

    private static final long serialVersionUID = -1L;
    private String operatorName;
    private String terminalURI;

    public ECHOHeader() {

    }

    public ECHOHeader(String operatorName, String terminalURI) {
        this.operatorName = operatorName;
        this.terminalURI = terminalURI;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getTerminalURI() {
        return terminalURI;
    }

    public void setTerminalURI(String terminalURI) {
        this.terminalURI = terminalURI;
    }

    @Override
    public String toString() {
        return "ECHOHeader{" +
                "operatorName='" + operatorName + '\'' +
                ", terminalURI='" + terminalURI + '\'' +
                '}';
    }
}

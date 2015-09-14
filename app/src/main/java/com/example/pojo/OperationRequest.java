package com.example.pojo;

public class OperationRequest implements java.io.Serializable {

    private static final long serialVersionUID = -1L;
    private OperationType operationType;
    private ECHOHeader echoHeader;
    private ECHOBody echoBody;

    public OperationRequest() {

    }

    public OperationRequest(OperationType operationType, ECHOHeader echoHeader, ECHOBody echoBody) {
        this.operationType = operationType;
        this.echoHeader = echoHeader;
        this.echoBody = echoBody;
    }

    public ECHOBody getEchoBody() {
        return echoBody;
    }

    public void setEchoBody(ECHOBody echoBody) {
        this.echoBody = echoBody;
    }

    public ECHOHeader getEchoHeader() {
        return echoHeader;
    }

    public void setEchoHeader(ECHOHeader echoHeader) {
        this.echoHeader = echoHeader;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "OperationRequest{" +
                "echoBody=" + echoBody +
                ", operationType=" + operationType +
                ", echoHeader=" + echoHeader +
                '}';
    }
}

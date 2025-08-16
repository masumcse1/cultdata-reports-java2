package com.reports.CultDataReports.responsedto;

public class ResultRdo<T> {

    private T data;

    public ResultRdo(T data) {
        this.data = data;
    }

    public ResultRdo() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
package com.example.greenstamp.Models;

public class ApiAnalysisBody {
    String appName;
    String packageName;
    long version;
    Boolean forceTest;
    Boolean ranking;

    public ApiAnalysisBody(String appName, String packageName, long version) {
        this.appName = appName;
        this.packageName = packageName;
        this.version = version;
        this.forceTest = false;
        this.ranking = true;
    }
}

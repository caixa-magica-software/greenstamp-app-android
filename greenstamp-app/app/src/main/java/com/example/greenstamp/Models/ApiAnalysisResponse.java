package com.example.greenstamp.Models;

import java.util.List;

public class ApiAnalysisResponse {
    public Data data;
    public String error;

    public static class Data {
        public String name;
        public String url;
        public String entity;
        public String timeStamp;
        public List<Result> results;

        public static class Result {
            public String name;
            public String parameters;
            public String result;
        }
    }
}
package com.example.greenstamp.Models;

import java.util.ArrayList;
import java.util.List;

public class ApiAnalysisResponse {
    public Data data;
    public String error;

    public static class Data {
        public String appName;
        public String packageName;
        public String version;
        public String timeStamp;
        public ArrayList<String> categories;
        public List<Result> results;

        public static class Result {
            public String name;
            public String parameters;
            public String result;
            public String unit;
        }
    }
}
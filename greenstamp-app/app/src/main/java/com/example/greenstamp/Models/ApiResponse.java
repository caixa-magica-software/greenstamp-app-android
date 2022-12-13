package com.example.greenstamp.Models;

import java.util.List;

public class ApiResponse {
    public Info info;
    public Nodes nodes;
    public List<Error> errors;

    public static class Info {
        public String status;
        public Time time;
    }

    public static class Nodes {
        public Meta meta;
        public Versions versions;

        public static class Meta {
            public Info info;
            public AppDetails data;
        }

        public static class Versions {
            public Info info;
            public List<AppDetails> list;
        }
    }

    public static class Time {
        public double seconds;
        public String human;
    }

    public static class Error {
        public String code;
        public Time description;
        public Object details;
    }
}
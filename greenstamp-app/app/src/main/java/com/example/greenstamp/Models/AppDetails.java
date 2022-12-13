package com.example.greenstamp.Models;

import java.util.ArrayList;
import java.util.List;

public class AppDetails {
    public int id;
    public String name;
    public String packageName;
    public String uname;
    public int size;
    public String icon;
    public String graphic;
    public String added;
    public String modified;
    public String updated;
    public String uptype;
    public String main_package;
    public Age age;
    public Developer developer;
    public Store store;
    public File file;
    public Media media;
    public URLS urls;
    public Stats stats;
    public String aab;
    public String obb;
    public String pay;
    public AppCoins appcoins;

    public static class Age {
        public String name;
        public String title;
        public String pegi;
        public int rating;
    }

    public static class Developer {
        public int id;
        public String name;
        public String website;
        public String email;
        public String privacy;
    }

    public static class Store {
        public int id;
        public String name;
        public String avatar;
        public Appearance appearance;
        public Stats stats;

        public static class Appearance {
            public String theme;
            public String description;
        }

        public static class Stats {
            public long apps;
            public long subscribers;
            public long downloads;
        }
    }

    public static class File {
        public String vername;
        public int vercode;
        public String md5sum;
        public long filesize;
        public String added;
        public String path;
        public String path_alt;
        public Signature signature;
        public Hardware hardware;
        public Malware malware;
        public Flags flags;
        public List<String> used_features;
        public List<String> used_permissions;
        public ArrayList<String> tags;

        public static class Signature {
            public String sha1;
            public String owner;
        }

        public static class Hardware {
            public int sdk;
            public String screen;
            public int gles;
            public ArrayList<?> cpus;
            public ArrayList<?> densities;
            public ArrayList<?> dependencies;
        }

        public static class Malware {
            public String rank;
            public Reason reason;
            public String added;
            public String modified;

            public static class Reason {
                public SignatureValidated signature_validated;
                public Scanned scanned;

                public static class SignatureValidated {
                    public String date;
                    public String status;
                    public String signature_from;
                }

                public static class Scanned {
                    public String status;
                    public String date;
                    public List<AVInfo> av_info;

                    public static class AVInfo {
                        public ArrayList<?> infections;
                        public String name;
                    }
                }
            }
        }

        public static class Flags {
            public List<Vote> votes;

            public static class Vote {
                public String type;
                public int count;
            }
        }
    }

    public static class Media {
        public List<String> keywords;
        public String description;
        public String summary;
        public String news;
        public ArrayList<?> videos;
        public List<Screenshot> screenshots;

        public static class Screenshot {
            public String url;
            public int height;
            public int width;
        }
    }

    public static class URLS {
        public String w;
        public String m;
    }

    public static class Stats {
        public Rating rating;
        public Prating prating;
        public int downloads;
        public int pdownloads;

        public static class Rating {
            public Double avg;
            public int total;
            public List<Vote> votes;
        }

        public static class Prating {
            public Double avg;
            public int total;
            public List<Vote> votes;
        }

        public static class Vote {
            public int value;
            public int count;
        }
    }

    public static class AppCoins {
        public String advertising;
        public String billing;
        public ArrayList<?> flags;
    }
}
package com.workshop.Entity;

public class Url {
    private String loc;
    private String lastmod;
    private String changefreq;
    private String priority;

    public Url(String loc, String lastmod, String changefreq, String priority) {
        this.loc = loc;
        this.lastmod = lastmod;
        this.changefreq = changefreq;
        this.priority = priority;
    }

    public String getLoc() {
        return loc;
    }

    public String getLastmod() {
        return lastmod;
    }

    public String getChangefreq() {
        return changefreq;
    }

    public String getPriority() {
        return priority;
    }
}

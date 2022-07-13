package com.nadhholy.tikdownloader.video.data;

import java.io.Serializable;

public class Config implements Serializable {

    private boolean rated;
    private boolean premium;
    private int openCount;


    public Config(){}

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public int getOpenCount() {
        return openCount;
    }

    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }

}

package com.cango.palmcartreasure.model;

/**
 * Created by cango on 2017/5/9.
 */

public class SelectPhoto {
    public SelectPhoto(String photoUrl, boolean isShowSelect) {
        this.photoUrl = photoUrl;
        this.isShowSelect = isShowSelect;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isShowSelect() {
        return isShowSelect;
    }

    public void setShowSelect(boolean showSelect) {
        isShowSelect = showSelect;
    }

    private String photoUrl;
    private boolean isShowSelect;
}

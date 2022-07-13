package com.nadhholy.tikdownloader.video.models;

import android.text.TextUtils;

import com.nadhholy.tikdownloader.video.dialogs.VideoSheet;
import com.nadhholy.tikdownloader.video.utils.Utils;

import java.io.Serializable;

public class Aweme implements Serializable {

    private long id;
    private String tiktokUrl;
    private String url;
    private String urlW;
    private String urlHd;
    private String cover;
    private String coverAnim;
    private String music;
    private String title;

    private String username;
    private String nickname;
    private String avatar;

    private String musicTitle;
    private String musicUrl;
    private String musicCover;
    private String musicAuthor;

    private String localPath;


    public Aweme(){
        id = System.currentTimeMillis();
    }

    public void genId(){
        id = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTiktokUrl() {
        return tiktokUrl;
    }

    public void setTiktokUrl(String tiktokUrl) {
        this.tiktokUrl = tiktokUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlHd() {
        return urlHd;
    }

    public void setUrlHd(String urlHd) {
        this.urlHd = urlHd;
    }

    public String getUrlW() {
        return urlW;
    }

    public void setUrlW(String urlW) {
        this.urlW = urlW;
    }

    public String getCoverAnim() {
        return coverAnim;
    }

    public void setCoverAnim(String coverAnim) {
        this.coverAnim = coverAnim;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicCover() {
        return musicCover;
    }

    public void setMusicCover(String musicCover) {
        this.musicCover = musicCover;
    }

    public String getMusicAuthor() {
        return musicAuthor;
    }

    public void setMusicAuthor(String musicAuthor) {
        this.musicAuthor = musicAuthor;
    }

    public String getUsername() {
        if (username == null) return "Name";
        return username;
    }

    public void setUsername(String username) {
        this.username = Utils.cleanTextContent(username);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusicTitle() {
//        if (username == null) return "🎵 Music";
//        if (!musicTitle.startsWith("🎵"))
//        return "🎵 "+ musicTitle;
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }


    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getVideoExt(String url){
        String ext = "";

        if (!TextUtils.isEmpty(url)){
            ext = url.substring(url.lastIndexOf("."));
        }

        return ext;
    }

    private String reformatUrl(String url){
        if (url == null)
            return null;

        if (url.startsWith("//"))
            return "https:"+url;

        if (url.startsWith("/"))
            return "https:/"+url;

        return url;
    }

    public String getVideo() {
        return getUrl();
    }


    public String getDownloadUrl(int type){
        if (type == VideoSheet.MP3){
            return musicUrl;
        } else if (type == VideoSheet.WM){
            return urlW;
        } else {
            return url;
        }
    }

    public String getDownloadExt(int type){
        if (type == VideoSheet.MP3){
            return ".MP3";
        } else {
            return ".MP4";
        }
    }

    public String getFiletype(){
        if (localPath.endsWith(".MP3")){
            return "Music";
        } else {
            return "Video";
        }
    }

    public boolean isMusic(){
        return localPath.endsWith(".MP3");
    }

//    @Override
//    public String toString() {
//        return "VideoData{" +
//                "id=" + id +
//                ", url='" + url + '\'' +
//                ", url_hd='" + urlHd + '\'' +
//                ", username='" + username + '\'' +
//                ", avatar='" + avatar + '\'' +
//                ", imageUrl='" + cover + '\'' +
//                ", text='" + title + '\'' +
//                ", musicName='" + musicTitle + '\'' +
//                ", localPath='" + localPath + '\'' +
//                '}';
//    }
}

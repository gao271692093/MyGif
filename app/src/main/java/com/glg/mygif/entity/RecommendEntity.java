package com.glg.mygif.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

/**
 * 作者: 任建祥
 * 时间: 2020/4/27
 * 功能:
 */
public class RecommendEntity {
    /**
     * title : 趣味配音
     * desc : 教材话题同步
     * type : 2
     * data :
     * refreshurl : https://mapi.ekwing.com/teacher/index/recommend?module=dub
     * list : [{"video_time":"03:30","diff":1,"id":1,"key":"513_1_0_1","grade":"人教七上unit1","order":1,"name":"配音标题","coverurl":"https://cdn-resource.ekwing.com/acpf/data/upload/tk/2016/03/30/56fb6eae5d14c.jpg","videourl":"https://cdn-resource.ekwing.com/acpf/data/upload/expand/2020/02/13/5e44c68b422c2.mp4","data":""}]
     */

    private String module;
    private String unit_id;
    private String grade;
    private String title;
    private String desc;
    private int type;
    private String data;
    private String icon;
    private String refreshurl;
    private List<ListBean> list;
    private boolean isShow;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getIcon() {
        return icon == null ? "" : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRefreshurl() {
        return refreshurl;
    }

    public void setRefreshurl(String refreshurl) {
        this.refreshurl = refreshurl;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public static class ListBean extends BaseObservable {
        /**
         * video_time : 03:30
         * diff : 1
         * id : 1
         * key : 513_1_0_1
         * grade : 人教七上unit1
         * order : 1
         * name : 配音标题
         * coverurl : https://cdn-resource.ekwing.com/acpf/data/upload/tk/2016/03/30/56fb6eae5d14c.jpg
         * videourl : https://cdn-resource.ekwing.com/acpf/data/upload/expand/2020/02/13/5e44c68b422c2.mp4
         * data :
         */

        private String video_time;
        private int diff;
        private String id;
        private String key;
        private String grade;
        private int order;
        private String name;
        private String coverurl;
        private String videourl;
        private String data;
        private boolean isLike = false;
        private int likeCount = 0;

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public String getVideo_time() {
            return video_time;
        }

        public void setVideo_time(String video_time) {
            this.video_time = video_time;
        }

        public int getDiff() {
            return diff;
        }

        public void setDiff(int diff) {
            this.diff = diff;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        @Bindable
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoverurl() {
            return coverurl;
        }

        public void setCoverurl(String coverurl) {
            this.coverurl = coverurl;
        }

        public String getVideourl() {
            return videourl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public String getModule() {
        return module == null ? "" : module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUnit_id() {
        return unit_id == null ? "" : unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getGrade() {
        return grade == null ? "" : grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

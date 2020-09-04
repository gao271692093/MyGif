package com.glg.mygif.entity;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: Data
 * @author: gao
 * @date: 2020/8/6 18:20
 */
public class Data {

    private RecommendBean data;

    private String status;

    public RecommendBean getData() {
        return data;
    }

    public void setData(RecommendBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

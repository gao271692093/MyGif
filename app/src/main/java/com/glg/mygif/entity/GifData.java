package com.glg.mygif.entity;

import java.util.List;

/**
 * Description:
 *
 * @package: com.glg.mygif
 * @className: GifData
 * @author: gao
 * @date: 2020/8/6 23:16
 */
class GifData {

    private List<GifItem> data;

    private String msg;

    private String status;

    public List<GifItem> getData() {
        return data;
    }

    public void setData(List<GifItem> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

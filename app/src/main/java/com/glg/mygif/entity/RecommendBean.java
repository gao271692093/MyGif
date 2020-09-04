package com.glg.mygif.entity;

import java.util.List;

/**
 * Description:
 *
 * @package: com.ekwing.intelligence.teachers.entity
 * @className: RecommendBean
 * @author: gao
 * @date: 2020/8/6 11:42
 */
public class RecommendBean {

    private String defaultGrade;

    private List<RecommendEntity> list;

    public String getDefaultGrade() {
        return defaultGrade == null ? "" : defaultGrade;
    }

    public void setDefaultGrade(String defaultGrade) {
        this.defaultGrade = defaultGrade;
    }

    public List<RecommendEntity> getList() {
        return list;
    }

    public void setList(List<RecommendEntity> list) {
        this.list = list;
    }
}

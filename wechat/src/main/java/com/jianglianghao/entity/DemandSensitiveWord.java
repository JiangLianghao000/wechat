package com.jianglianghao.entity;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1022:50
 */

public class DemandSensitiveWord {
    private int id;
    private String badword;

    public DemandSensitiveWord() {
    }

    public DemandSensitiveWord(int id, String badword) {
        this.id = id;
        this.badword = badword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBadword() {
        return badword;
    }

    public void setBadword(String badword) {
        this.badword = badword;
    }

    @Override
    public String toString() {
        return "DemandSensitiveWord{" +
                "id=" + id +
                ", badword='" + badword + '\'' +
                '}';
    }
}

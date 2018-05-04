package cn.e3.commom.easyUIGridResult;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable {

    //异步树的节点。具体参考easyui的异步树文档

    private Long id;
    private String text;
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

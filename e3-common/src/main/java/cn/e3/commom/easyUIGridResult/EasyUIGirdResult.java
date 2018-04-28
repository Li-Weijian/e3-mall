package cn.e3.commom.easyUIGridResult;

import java.io.Serializable;
import java.util.List;

public class EasyUIGirdResult implements Serializable {

    private Integer total;
    private List rows;

    public EasyUIGirdResult() {
    }

    public EasyUIGirdResult(Integer total, List rows) {
        this.total = total;
        this.rows = rows;
    }
    public EasyUIGirdResult(Long total, List rows) {
        this.total = total.intValue();
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}

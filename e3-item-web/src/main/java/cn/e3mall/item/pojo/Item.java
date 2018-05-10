package cn.e3mall.item.pojo;

import cn.e3mall.pojo.TbItem;

import java.util.Date;

public class Item extends TbItem {

    public String [] getImages(){
        String image = this.getImage();
        if (null != image && !"".equals(image)){
            String[] images = image.split(",");
            return images;
        }
        return null;
    }

    public Item() {
    }

    public Item(TbItem tbItem) {
        setId(tbItem.getId());
        setTitle(tbItem.getTitle());
        setSellPoint(tbItem.getSellPoint());
        setPrice(tbItem.getPrice());
        setNum(tbItem.getNum());
        setBarcode(tbItem.getBarcode());
        setImage(tbItem.getImage());
        setCid(tbItem.getCid());
        setStatus(tbItem.getStatus());
        setCreated(tbItem.getCreated());
        setUpdated(tbItem.getUpdated());
    }
}

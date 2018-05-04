package cn.e3mall.content.service;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;

public interface ContentService {

    public EasyUIGirdResult getContentList(Long categoryId, int page, int rows);


}


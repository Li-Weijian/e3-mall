package cn.e3mall.content.service.impl;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;


    /**
     * 获取内容列表
     * */
    @Override
    public EasyUIGirdResult getContentList(Long categoryId, int page, int rows) {
        //设置分页
        PageHelper.startPage(page,rows);
        //添加查询条件
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //查询，包括大文本
        List<TbContent> contentList = contentMapper.selectByExampleWithBLOBs(contentExample);
        //将列表放入PageInfo
        PageInfo<TbContent> info = new PageInfo<>(contentList);
        EasyUIGirdResult easyUIGirdResult = new EasyUIGirdResult(info.getTotal(),contentList);

        return easyUIGirdResult;
    }
}

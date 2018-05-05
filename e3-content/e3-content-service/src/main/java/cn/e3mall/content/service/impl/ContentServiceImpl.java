package cn.e3mall.content.service.impl;

import cn.e3.commom.easyUIGridResult.EasyUIGirdResult;
import cn.e3.commom.jedis.JedisClient;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;

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

    /**
     * 添加内容
     * */
    @Override
    public E3Result addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        //redis缓存同步，添加的时候将对应cid的缓存删除，下次用户访问时将自动缓存到redis中
        try {
            jedisClient.hdel(CONTENT_KEY,content.getCategoryId()+"");
        }catch (Exception e){
            e.printStackTrace();
        }
        return E3Result.ok();
    }

    @Override
    public List<TbContent> getContentByCid(Long cid) {

        try {
            //使用redis进行缓存
            //1. 判断在缓存中是否存在该值
            String content_key = jedisClient.hget(CONTENT_KEY, cid + "");
            if (StringUtils.isNotBlank(content_key)){
                //如果存在，则取出并返回
                return JsonUtils.jsonToList(content_key,TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            //如果不存在，则从数据库中查询
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExample(contentExample);

        try {
            // 添加到缓存
            jedisClient.hset(CONTENT_KEY,cid+"",JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


}

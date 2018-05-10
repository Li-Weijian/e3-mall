package cn.e3mall.service.impl;

import cn.e3.commom.jedis.JedisClient;
import cn.e3.commom.pojo.EasyUIGirdResult;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.IDUtils;
import cn.e3.commom.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private JedisClient jedisClient;
    @Resource
    private Destination topicDestination;   //根据配置文件中id进行命名

    @Value("${ITEM_CACHE_PRE}")
    private String ITEM_CACHE_PRE;          //  商品缓存的key前缀
    @Value("${CACHE_TIMEOUT}")
    private int CACHE_TIMEOUT;              //缓存过期时间

    /**
     * 根据id查询商品
     * */
    @Override
    public TbItem getItemById(long itemId) {
        try {
            //查询缓存
            String tbItem = jedisClient.get(ITEM_CACHE_PRE+":"+itemId+":BASE");
            if (StringUtils.isNotBlank(tbItem)){
                TbItem item = JsonUtils.jsonToPojo(tbItem,TbItem.class);
                return item;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //查询商品
        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        try {
            //添加到缓存
            String json = JsonUtils.objectToJson(item);
            jedisClient.set(ITEM_CACHE_PRE+":"+itemId+":BASE",json);
            jedisClient.expire(ITEM_CACHE_PRE+":"+itemId+":BASE",CACHE_TIMEOUT);        //设置缓存过期时间
        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 获取分页商品列表
     * */
    @Override
    public EasyUIGirdResult getItemList(int page, int rows) {
        //设置分页
        PageHelper.startPage(page,rows);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(new TbItemExample());
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIGirdResult result = new EasyUIGirdResult(pageInfo.getTotal(),list);
        return result;
    }

    /**
     * 商品添加
     * */
    @Override
    public E3Result addItem(TbItem item, String desc) {
//        1、生成商品id
            final long id = IDUtils.genItemId();
//        2、补全TbItem对象的属性
            item.setId(id);
            item.setStatus((byte) 1);
            item.setCreated(new Date());
            item.setUpdated(new Date());
//        3、向商品表插入数据
            itemMapper.insert(item);
//        4、创建一个TbItemDesc对象
            TbItemDesc itemDesc = new TbItemDesc();
//        5、补全TbItemDesc的属性
            itemDesc.setItemDesc(desc);
            itemDesc.setItemId(id);
            itemDesc.setUpdated(new Date());
            itemDesc.setCreated(new Date());
//        6、向商品描述表插入数据
            itemDescMapper.insert(itemDesc);
            //6.1 向ActiveMQ消息队列发送消息
            jmsTemplate.send(topicDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(id + "");
                    return textMessage;
                }
            });

//        7、E3Result.ok()
            return E3Result.ok();
    }

    /**
     * 更新商品
     * */
    @Override
    public E3Result updateItem(TbItem item, String desc) {
        TbItem tbItem = itemMapper.selectByPrimaryKey(item.getId());
        item.setCreated(tbItem.getCreated());
        item.setUpdated(new Date());
        item.setStatus((byte) 1);
        itemMapper.updateByPrimaryKey(item);

        if (null != desc && !"".equals(desc)){
            TbItemDesc itemDesc = new TbItemDesc();
            itemDesc.setItemId(item.getId());
            itemDesc.setItemDesc(desc);
            itemDesc.setUpdated(new Date());
            itemDescMapper.updateByPrimaryKey(itemDesc);
        }
        return E3Result.ok();
    }

    /**
     * 删除商品
     * */
    @Override
    public E3Result deleteItem(String ids) {
        if (!ids.contains(",")){
            itemMapper.deleteByPrimaryKey(Long.parseLong(ids));
            return E3Result.ok();
        }else {
            String[] id = ids.split(",");
            for (String i: id) {
                itemMapper.deleteByPrimaryKey(Long.parseLong(i));
            }
            return E3Result.ok();
        }
    }


    /**
     * 根据商品id查询商品描述
     * */
    @Override
    public TbItemDesc getItemDescById(long itemId) {
        try {
            //查询缓存
            String itemDesc = jedisClient.get(ITEM_CACHE_PRE+":"+itemId+":DESC");
            if (StringUtils.isNotBlank(itemDesc)){
                TbItemDesc desc = JsonUtils.jsonToPojo(itemDesc, TbItemDesc.class);
                return desc;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            //添加到缓存
            String json = JsonUtils.objectToJson(itemDesc);
            jedisClient.set(ITEM_CACHE_PRE+":"+itemId+":DESC",json);
            jedisClient.expire(ITEM_CACHE_PRE+":"+itemId+":DESC",CACHE_TIMEOUT);        //设置缓存过期时间
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemDesc;
    }



}

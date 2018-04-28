package cn.e3mall.pagehelper;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class PageHelperTest {


    @Test
    public void testPageHelper(){

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-dao.xml");
        TbItemMapper itemMapper = context.getBean(TbItemMapper.class);

        //设置分页信息 -- 必须要放在查询的前面
        PageHelper.startPage(1,10);
        //执行查询
        List<TbItem> itemList = itemMapper.selectByExample(new TbItemExample());

        //获取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemList);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getSize());


    }



}

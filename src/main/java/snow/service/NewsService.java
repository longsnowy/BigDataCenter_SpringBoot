package snow.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snow.entity.DataSource;
import snow.entity.Keywords;
import snow.entity.News;
import snow.mapper.NewsMapper;
import snow.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;

@Service("newsService")
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;



    public List<HashMap<String,Object>> query(String tablename, int num, int size) {
        PageHelper.startPage(num, size);
        PageInfo<HashMap<String,Object>> pageInfo = new PageInfo<>(newsMapper.queryNews(tablename));
        return pageInfo.getList();
    }


    public List<Keywords> getKeywords(String tablename) {
        return newsMapper.getKeywords(tablename);
    }

    public Integer getTotals(String tablename) {
        return newsMapper.getTotals(tablename);
    }
}

package snow.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snow.entity.DataSource;
import snow.entity.Dictionary;
import snow.mapper.DataSourceMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class DataSourceService {
    @Autowired
    private DataSourceMapper dataSourceMapper;

    //创建表
    public int createTable(String path, Integer nameIndex, Integer unitIndex, Integer meansIndex, String tablename) {
        return dataSourceMapper.createTable(path, nameIndex, unitIndex, meansIndex, tablename);
    }

    //从Excel添加
    public int addFromExcel(String path, String tablename, Integer beginIndex) {
        return dataSourceMapper.addFromExcel(path, tablename, beginIndex);
    }

    //从Excel添加
    public int addOneByOne(String path, String tablename, Integer beginIndex) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<Cell>> rowList = new ArrayList<>();
        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
            Row row = sheet.getRow(i);
            List<Cell> cellList = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                cellList.add(cell);
            }
            rowList.add(cellList);
        }
        for (List<Cell> cells : rowList) {
            System.out.println(cells);
        }

        List<Cell> headers = rowList.get(0);

        for (int i = beginIndex - 1; i < rowList.size(); i++) {
            Map<String, Object> cellMap = new LinkedHashMap<>();
            List<Cell> cells = rowList.get(i);

            for (int j = 0; j < cells.size(); j++) {
                Cell cell = cells.get(j);
                Cell header = headers.get(j);
                cellMap.put(header.toString(), cell.toString().replace("'", "*"));
            }

            dataSourceMapper.addOneData(cellMap, tablename);
        }


        return 0;
    }

    //添加数据源
    public int addDataSource(DataSource dataSource) {
        return dataSourceMapper.insert(dataSource);
    }

    //得到所有数据源
    public List<DataSource> getAll(int num, int size) {
        PageHelper.startPage(num, size);
        PageInfo<DataSource> pageInfo = new PageInfo<>(dataSourceMapper.selectList(null));
        return pageInfo.getList();
    }

    //查找数据源
    public List<DataSource> query(DataSource dataSource, int num, int size) {
        QueryWrapper<DataSource> queryWrapper = new QueryWrapper<>(dataSource);
        PageHelper.startPage(num, size);
        PageInfo<DataSource> pageInfo = new PageInfo<>(dataSourceMapper.selectList(queryWrapper));
        return pageInfo.getList();
    }

    //根据id删除数据源
    public int deleteById(Integer id) {
        dataSourceMapper.dropTableByName(dataSourceMapper.selectById(id).getTableName());
        return dataSourceMapper.deleteById(id);
    }

    //根据id修改
    public int updateById(DataSource dataSource) {
        return dataSourceMapper.updateById(dataSource);
    }

    //得到一个数据源
    public DataSource getOneById(Integer id) {
        return dataSourceMapper.selectById(id);
    }

    public List<Dictionary> getDictionary(Integer id) {
        return dataSourceMapper.getDictionary(dataSourceMapper.selectById(id).getTableName());
    }

    public String getFileUrl(Integer id) {
        return dataSourceMapper.selectById(id).getFileUrl();
    }

    public int addOneData(Map<String, Object> map, Integer id) {
        return dataSourceMapper.addOneData(map, dataSourceMapper.selectById(id).getTableName());
    }

    public List<Dictionary> getDicByName(String tablename) {
        return dataSourceMapper.getDictionary(tablename);
    }

    //查询表详情
    public List<HashMap<String, Object>> queryTableDetails(Integer id, Integer num, Integer size) {
        num -= 1;
        num = num * size;
        String tablename = dataSourceMapper.selectById(id).getTableName();
        List<HashMap<String, Object>> list = new ArrayList<>(dataSourceMapper.queryTableDetails(tablename, num, size));
        return list;
    }

    //获取总条数
    public Integer getTableTotal(Integer id) {
        String tablename = dataSourceMapper.selectById(id).getTableName();
        Integer total = dataSourceMapper.getTableTotal(tablename);
        return total;
    }

    //修改表字典
    public int updateDic(String tablename, String field, String type, String comment) {
        return dataSourceMapper.updateDic(tablename, field, type, comment);
    }

    //修改表字典
    public int changeDic(String tablename, String oldfield, String field, String oldtype, String type, String comment) {
        return dataSourceMapper.changeDic(tablename, oldfield, field, oldtype, type, comment);
    }

    //修改表字典
    public int addField(String tablename, String field, String type, String comment) {
        return dataSourceMapper.addField(tablename, field, type, comment);
    }

    //修改表字典
    public int deleteField(String tablename, String field) {
        return dataSourceMapper.deleteField(tablename, field);
    }

    //空值检测
    public List<HashMap<String, Object>> nullSearch(String tablename, String field) {
        return dataSourceMapper.nullSearch(tablename, field);
    }

    //空值补全
    public int nullUpdate(String tablename, String field, String value, String id) {
        return dataSourceMapper.nullUpdate(tablename, field, value, id);
    }

    public String jsonToStr(String jsonStr) {
        List<Object> list = null;
        try {
            list = JSON.parseArray(jsonStr);
        } catch (Exception e) {
            return jsonStr;
        }

        if (list == null || list.size() < 1){
            return "";
        }

        List<String> nameList = new ArrayList<>();
        for (Object object : list) {
            Map<String, Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
            nameList.add(ret.get("name").toString());
        }

        StringBuilder strTemp = new StringBuilder();
        for (int i = 0; i < nameList.size() - 1; i++) {
            strTemp.append(nameList.get(i).toString()).append(",");
        }
        if (nameList.size() > 0)
            strTemp.append(nameList.get(nameList.size() - 1).toString());

        return strTemp.toString();
    }

    public int deleteByNum(String tablename, String field, String value, String type) {
        return dataSourceMapper.deleteByNum(tablename, field, value, type);
    }
    public int deleteRepe(String tablename) {
        return dataSourceMapper.deleteRepe(tablename);
    }

}

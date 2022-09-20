package snow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import snow.entity.DataSource;
import snow.entity.Dictionary;
import snow.utils.SQLprovider;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
@Repository

public interface DataSourceMapper extends BaseMapper<DataSource> {
    @UpdateProvider(type = SQLprovider.class,method = "ExceltoSQLCommon")
    int createTable(@Param("path") String path,@Param("nameIndex") Integer nameIndex,@Param("unitIndex") Integer unitIndex,@Param("meansIndex") Integer meansIndex,@Param("tablename") String tablename);

    //删除表
    @Update("drop table bdctables.${tableName}")
    int dropTableByName(@Param("tableName") String tableName);

    //查询表字典
    @Select("show full columns from bdctables.${tableName}")
    List<Dictionary> getDictionary(@Param("tableName") String tableName);

    //添加一个
    @UpdateProvider(type = SQLprovider.class,method = "getAddOneSql")
    int addOneData(@Param("map") Map<String,Object> map,@Param("tablename") String tablename);


    //查询表数据
    @Select("select * from bdctables.${tableName} limit ${num},${size};")
    List<HashMap<String,Object>> queryTableDetails(@Param("tableName") String tableName, @Param("num") Integer num,@Param("size") Integer size);

    //查询表数据
    @Select("select count(*) from bdctables.${tableName};")
    Integer getTableTotal(@Param("tableName") String tableName);

    //从Excel参加
    @UpdateProvider(type = SQLprovider.class,method = "ExceltoInsertSQL")
    int addFromExcel(@Param("path") String path,@Param("tablename") String tablename,@Param("beginIndex") Integer beginIndex);

    //修改字典
    @Update("alter table bdctables.${tablename} modify ${field} ${type} comment '${comment}';")
    int updateDic(@Param("tablename") String tablename,@Param("field") String field,@Param("type") String type,@Param("comment") String comment);

    //修改字段,类型,注释
    @UpdateProvider(type = SQLprovider.class,method = "changeDic")
    int changeDic(@Param("tablename") String tablename, @Param("oldfield") String oldfield,@Param("field") String field,@Param("oldtype") String oldtype,@Param("type")  String type,@Param("comment") String comment);

    @Update("ALTER TABLE bdctables.${tablename} ADD COLUMN ${field} ${type} comment '${comment}';")
    int addField(@Param("tablename") String tablename,@Param("field") String field,@Param("type") String type,@Param("comment") String comment);

    @Update("ALTER TABLE bdctables.${tablename} DROP ${field};")
    int deleteField(@Param("tablename") String tablename,@Param("field") String field);

    @Select("select * from bdctables.${tablename} where ${field}='' or ${field}=null")
    List<HashMap<String,Object>> nullSearch(@Param("tablename") String tablename,@Param("field") String field);

    @Update("UPDATE bdctables.${tablename} SET ${field} = '${value}' WHERE id = '${id}';")
    int nullUpdate(@Param("tablename") String tablename,@Param("field") String field,@Param("value") String value,@Param("id") String id);

    @Update("delete from bdctables.${tablename} where ${field}+0.0 ${type} ${value};")
    int deleteByNum(@Param("tablename") String tablename,@Param("field") String field,@Param("value") String value,@Param("type") String type);

    @Delete("delete from bdctables.${tablename} where id in (select id from bdctables.${tablename}  group by id having count(id) > 1);")
    int deleteRepe(@Param("tablename") String tablename);
}

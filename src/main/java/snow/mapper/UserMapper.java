package snow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import snow.entity.User;

import java.util.HashMap;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from cloud.${tablename}")
    List<HashMap<String,Object>> queryNews(@Param("tablename") String tablename);
}

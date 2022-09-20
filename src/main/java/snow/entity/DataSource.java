package snow.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("datasource")
public class DataSource {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "type")
    private String type;
    @TableField(value = "fileurl")
    private String fileUrl;
    @TableField(value = "filepath")
    private String filePath;
    @TableField(value = "tablename")
    private String tableName;
    @TableField(value = "showname")
    private String showName;
}

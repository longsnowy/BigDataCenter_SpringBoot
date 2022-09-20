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
//@TableName("datasource")
public class Dictionary {
    @TableField(value = "Field")
    private String Field;
    @TableField(value = "Type")
    private String Type;
    @TableField(value = "Collation")
    private String Collation;
    @TableField(value = "Null")
    private String Null;
    @TableField(value = "Key")
    private String Key;
    @TableField(value = "Default")
    private String Default;
    @TableField(value = "Extra")
    private String Extra;
    @TableField(value = "Privileges")
    private String Privileges;
    @TableField(value = "Comment")
    private String Comment;
}

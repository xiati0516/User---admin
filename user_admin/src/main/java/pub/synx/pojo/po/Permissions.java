package pub.synx.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @description 权限实体类，映射到权限表
 * @version 2024
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("tbl_permissions")
public class Permissions extends BaseEntity {

    /**
     * 权限ID
     */
    @TableId
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;
}



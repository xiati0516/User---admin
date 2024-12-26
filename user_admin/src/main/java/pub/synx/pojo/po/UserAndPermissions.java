package pub.synx.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和权限关联实体类
 * 对应用户与权限关联表
 *
 * @author SynX
 * @version 2024
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_user_and_permissions")
public class UserAndPermissions {
    private String userId;       // 用户ID
    private String permissionId; // 权限ID
}

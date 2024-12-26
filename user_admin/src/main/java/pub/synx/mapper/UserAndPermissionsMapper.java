package pub.synx.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pub.synx.pojo.po.UserAndPermissions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

@Mapper
public interface UserAndPermissionsMapper extends BaseMapper<UserAndPermissions> {

    @Insert({
            "<script>",
            "INSERT INTO tbl_user_and_permissions (user_id, permission_id) ",
            "VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.userId}, #{item.permissionId})",
            "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("list") List<UserAndPermissions> userAndPermissionsList);
}



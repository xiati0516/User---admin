package pub.synx.mapper;

import org.apache.ibatis.annotations.Mapper;
import pub.synx.pojo.po.Permissions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @description 权限表Mapper接口
 * @version 2024
 **/
@Mapper
public interface PermissionsMapper extends BaseMapper<Permissions> {
}

package pub.synx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pub.synx.pojo.po.Permissions;
import pub.synx.pojo.po.User;

import java.util.List;

/**
 * @description 权限服务接口，定义权限的增删改查和用户权限绑定/解绑操作
 * @version 2024
 */
public interface PermissionsService extends IService<Permissions> {

    List<String> addPermissions(List<Permissions> permissions);

    boolean updatePermissions(List<Permissions> permissions);

    boolean deletePermissions(List<String> ids);

    List<Permissions> queryPermissions(Permissions permissions);

    boolean bindUsers(String permissionId, List<String> userIds);

    boolean unbindUsers(String permissionId, List<String> userIds);

    List<Permissions> getUsersByPermission(String userId);
}


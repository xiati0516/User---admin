package pub.synx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pub.synx.enums.ResultMessageEnum;
import pub.synx.mapper.PermissionsMapper;
import pub.synx.mapper.UserAndPermissionsMapper;
import pub.synx.mapper.UserMapper;
import pub.synx.pojo.po.Permissions;
import pub.synx.pojo.po.User;
import pub.synx.pojo.po.UserAndPermissions;
import pub.synx.service.PermissionsService;
import pub.synx.util.IdGenerator;
import pub.synx.util.exception.DataProcessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description 权限服务实现类，包含权限管理的具体逻辑
 * @version 2024
 */
@Slf4j
@Service
@Transactional
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements PermissionsService {
    private final PermissionsMapper permissionsMapper;
    private final UserMapper userMapper;
    private final UserAndPermissionsMapper userAndPermissionsMapper;

    public PermissionsServiceImpl(UserMapper userMapper, UserAndPermissionsMapper userAndPermissionsMapper,PermissionsMapper permissionsMapper) {
        this.userMapper = userMapper;
        this.userAndPermissionsMapper = userAndPermissionsMapper;
        this.permissionsMapper = permissionsMapper;
    }

    @Override
    public List<String> addPermissions(List<Permissions> permissions) {
        List<String> ids = IdGenerator.get(permissions.size());
        permissions.forEach(permission -> permission.setId(ids.get(permissions.indexOf(permission))));
        saveBatch(permissions);
        return ids;
    }

    @Override
    public boolean updatePermissions(List<Permissions> permissions) {
        permissions.forEach(permission -> {
            if (Objects.isNull(permission.getId())) {
                throw new DataProcessException(ResultMessageEnum.PERMISSIONID_NULL.getMessage());
            }
        });

        List<String> permissionIds = permissions.stream().map(Permissions::getId).toList();
        Long count = this.lambdaQuery().in(Permissions::getId, permissionIds).count();

        if (count != permissions.size()) {
            throw new DataProcessException(ResultMessageEnum.ID_NOTEXIST.getMessage());
        }
        return updateBatchById(permissions);
    }

    @Override
    public boolean deletePermissions(List<String> ids) {
        LambdaQueryWrapper<UserAndPermissions> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserAndPermissions::getPermissionId, ids);
        userAndPermissionsMapper.delete(wrapper);
        return removeBatchByIds(ids);
    }

    @Override
    public List<Permissions> queryPermissions(Permissions permission) {
        LambdaQueryWrapper<Permissions> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(permission.getId())) {
            wrapper.eq(Permissions::getId, permission.getId());
        }
        if (Objects.nonNull(permission.getName())) {
            wrapper.like(Permissions::getName, permission.getName());
        }
        return this.list(wrapper);
    }

    @Override
    public boolean bindUsers(String userId, List<String> permissionIds) {
        // 检查用户是否存在
        Long userCount = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getId, userId)
                .count();
        if (userCount == 0) {
            throw new DataProcessException("用户ID不存在");
        }

        // 检查权限是否存在
        Long permissionCount = new LambdaQueryChainWrapper<>(permissionsMapper)
                .in(Permissions::getId, permissionIds)
                .count();
        if (permissionCount != permissionIds.size()) {
            throw new DataProcessException("部分权限ID不存在");
        }

        // 绑定用户与权限
        List<UserAndPermissions> bindings = new ArrayList<>();
        permissionIds.forEach(permissionId -> bindings.add(new UserAndPermissions(userId, permissionId)));
        userAndPermissionsMapper.batchInsert(bindings);

        return true;
    }

@Override
public boolean unbindUsers(String userId, List<String> permissionIds) {
    // 构建删除条件
    LambdaQueryWrapper<UserAndPermissions> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(UserAndPermissions::getUserId, userId)
           .in(UserAndPermissions::getPermissionId, permissionIds);

    // 执行删除操作
    int deletedCount = userAndPermissionsMapper.delete(wrapper);

    // 检查是否删除成功
    if (deletedCount == 0) {
        throw new DataProcessException("解绑失败：未找到匹配的用户和权限绑定关系");
    }

    // 返回操作结果
    return true;
}


    @Override
    public List<Permissions> getUsersByPermission(String userId) {
        // 查询权限ID列表
        List<String> permissionIds = new LambdaQueryChainWrapper<>(userAndPermissionsMapper)
                .eq(UserAndPermissions::getUserId, userId)
                .select(UserAndPermissions::getPermissionId)
                .list().stream()
                .map(UserAndPermissions::getPermissionId).toList();

        // 如果权限ID列表为空，直接返回空列表
        if (permissionIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 根据权限ID列表查询权限详情
        return new LambdaQueryChainWrapper<>(permissionsMapper)
                .in(Permissions::getId, permissionIds)
                .list();
    }
}


package pub.synx.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pub.synx.pojo.po.Permissions;
import pub.synx.pojo.vo.req.BindReq;
import pub.synx.pojo.vo.req.DeleteReq;
import pub.synx.pojo.vo.req.EntitiesReq;
import pub.synx.pojo.vo.req.IdReq;
import pub.synx.pojo.vo.resp.ResultMessage;
import pub.synx.service.PermissionsService;
import pub.synx.util.CommonUtils;
import pub.synx.util.WrapperUtils;

import java.util.List;

/**
 * @description 权限管理接口，提供权限的增删改查和用户权限绑定/解绑操作
 * @version 2024
 */
@RestController
@RequestMapping("/permissions")
public class PermissionsController {

    private final PermissionsService permissionsService;

    public PermissionsController(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    /**
     * 新增权限
     * @param req 包含权限信息的请求体
     * @return 新增权限的ID列表
     */
    @PostMapping
    public ResultMessage addPermissions(@RequestBody @Valid EntitiesReq<Permissions> req) {
        return WrapperUtils.success(permissionsService.addPermissions(req.getEntities()));
    }

    /**
     * 修改权限
     * @param req 包含权限信息的请求体
     * @return 操作结果
     */
    @PutMapping
    public ResultMessage updatePermissions(@RequestBody @Valid EntitiesReq<Permissions> req) {
        return WrapperUtils.success(permissionsService.updatePermissions(req.getEntities()));
    }

    /**
     * 删除权限
     * @param req 包含权限ID的请求体
     * @return 操作结果
     */
    @DeleteMapping
    public ResultMessage deletePermissions(@RequestBody @Valid DeleteReq req) {
        return WrapperUtils.success(permissionsService.deletePermissions(req.getIds()));
    }

    /**
     * 查询权限
     * @param permissions 查询条件
     * @return 查询结果
     */
    @GetMapping
    public ResultMessage queryPermissions(Permissions permissions) {
        return WrapperUtils.success(CommonUtils.desensitize(permissionsService.queryPermissions(permissions)));
    }

    /**
     * 绑定权限到用户
     * @param req 包含用户ID和权限ID列表的请求体
     * @return 操作结果
     */
    @PutMapping("/users")
    public ResultMessage bindPermissionsToUsers(@RequestBody @Valid BindReq req) {
        return WrapperUtils.success(permissionsService.bindUsers(req.getId(), req.getIds()));
    }

    /**
     * 解绑用户的权限
     * @param req 包含用户ID和权限ID列表的请求体
     * @return 操作结果
     */
    @DeleteMapping("/users")
    public ResultMessage unbindPermissionsFromUsers(@RequestBody @Valid BindReq req) {
        return WrapperUtils.success(permissionsService.unbindUsers(req.getId(), req.getIds()));
    }

    /**
     * 获取用户的权限列表
     * @param req 包含用户ID的请求体
     * @return 用户的权限列表
     */
    @GetMapping("/users")
    public ResultMessage getPermissionsByUser(@RequestBody @Valid IdReq req) {
        return WrapperUtils.success(CommonUtils.desensitize(permissionsService.getUsersByPermission(req.getId())));
    }
}


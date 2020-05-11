package com.atguigu.acl.helper;

import com.atguigu.acl.entity.TPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 根据权限数据构建菜单数据
 * </p>
 *
 * @author qy
 * @since 2019-11-11
 */
public class TPermissionHelper {


    /**
     * 使用递归方法建菜单
     *
     * @param allPermission
     * @return
     */
    public static List<TPermission> bulid(List<TPermission> allPermission) {
        List<TPermission> treeNodes = new ArrayList<>();
        for (TPermission currentNode : allPermission) {
            //找到最外层
            if ("0".equals(currentNode.getPid())) {
                currentNode.setLevel(1);
                treeNodes.add(findChildren(currentNode, allPermission));
            }
        }
        return treeNodes;
    }

    /**
     * 递归查找子节点
     *
     * @param allPermission
     * @return
     */
    private static TPermission findChildren(TPermission currentNode, List<TPermission> allPermission) {
        for (TPermission permission : allPermission) {
            if (currentNode.getId().equals(permission.getPid())) {
                int level = currentNode.getLevel() + 1;
                permission.setLevel(level);
                currentNode.getChildren().add(findChildren(permission, allPermission));
            }
        }
        return currentNode;
    }

}

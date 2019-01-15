package demo.user.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chi
 */
@Component
public class PermissionService {
    public List<Permission> permissions() {
        return Lists.newArrayList(
            permission("product.read", "Read Product", "User could read product list page."),
            permission("product.write", "Create or Update Product", "User could create or update product."),
            permission("user.read", "Read User and Role", "User could read user list and role list page."),
            permission("user.write", "Write User and Role", "User could write user list and role list page.")
        );
    }

    private Permission permission(String name, String displayName, String description) {
        Permission permission = new Permission();
        permission.name = name;
        permission.displayName = displayName;
        permission.description = description;
        return permission;
    }
}

package app.demo.user.service;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType
public class CachedUserInfo {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "username")
    public String username;
    @XmlElement(name = "roleIds")
    public List<String> roleIds;
    @XmlElement(name = "permissionNames")
    public List<String> permissions = Lists.newArrayList();
}

package core.framework.monitor;

import core.framework.exception.UserAuthorizationException;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author neo
 */
public class MonitorAccessControl {
    public static void assertFromInternalNetwork(String clientIP) {
        try {
            InetAddress address = InetAddress.getByName(clientIP);
            if (!address.isLoopbackAddress() && !address.isSiteLocalAddress()) {
                throw new UserAuthorizationException("access denied");
            }
        } catch (UnknownHostException e) {
            throw new UserAuthorizationException("access denied", e);
        }
    }
}

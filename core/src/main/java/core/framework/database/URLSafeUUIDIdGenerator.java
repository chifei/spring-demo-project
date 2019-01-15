package core.framework.database;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @author neo
 */
public class URLSafeUUIDIdGenerator implements IdentifierGenerator {
    public static final String CLASS_NAME = "core.framework.database.URLSafeUUIDIdGenerator";
    public static final int KEY_LENGTH = 22;

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        UUID uuid = UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return Base64.encodeBase64URLSafeString(buffer.array());
    }
}

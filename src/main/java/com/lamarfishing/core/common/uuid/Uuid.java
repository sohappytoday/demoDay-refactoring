package com.lamarfishing.core.common.uuid;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class Uuid {

    //16자 UUID 생성
    public static String generateShortUUID() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(buffer.array())
                .substring(0, 16);
    }
}

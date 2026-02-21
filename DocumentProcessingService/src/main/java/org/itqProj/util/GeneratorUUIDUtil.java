package org.itqProj.util;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

public class GeneratorUUIDUtil {
    public static String generateUUID(int length) {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, length);
    }

    public static String generateDocumentUniqueNum(String generatedUUID) {
        return "doc_" + generatedUUID + "_" + OffsetDateTime.now(UTC);
    }

}

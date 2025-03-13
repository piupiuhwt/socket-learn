package com.hwt.netty.server;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class MIMEConstant {
    public static final String MIME_SUFFIX = "_MIME";
    public static final String CSS_MIME = "text/css";
    public static final String JS_MIME = "text/javascript";
    public static final String JPEG_MIME = "image/jpeg";
    public static final String JPG_MIME = "image/jpeg";
    public static final String PNG_MIME = "image/png";
    public static Map<String,String> mimeCacheMap = new HashMap<>();
    static {
        Field[] declaredFields = FileSuffixConstant.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getModifiers() != (Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL)) {
                continue;
            }
            try {
                declaredField.setAccessible(true);
                String value  = (String) declaredField.get(MIMEConstant.class);
                Field mimeField = MIMEConstant.class.getDeclaredField(declaredField.getName() + MIMEConstant.MIME_SUFFIX);
                mimeField.setAccessible(true);
                String mimeValue = (String) mimeField.get(MIMEConstant.class);
                mimeCacheMap.put(value, mimeValue);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}

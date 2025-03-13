package com.hwt.netty.server;

import lombok.Data;

@Data
public class URLEntity {
    private int type = 0;
    private String mimeType;
    private String url;
}

package com.yht.iptv.view;

import com.danikula.videocache.file.FileNameGenerator;

public class MyFileNameGenerator implements FileNameGenerator {

    // Urls contain mutable parts (parameter 'sessionToken') and stable video's id (parameter 'videoId').
    // e. g. http://example.com?videoId=abcqaz&sessionToken=xyz987
    public String generate(String url) {
        String substring = url.substring(url.lastIndexOf("/")+1);
//        Uri uri = Uri.parse(url);
//        String videoId = uri.getQueryParameter("videoId");
        return substring;
    }
}
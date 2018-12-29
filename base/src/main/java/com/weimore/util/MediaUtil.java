package com.weimore.util;

import com.google.gson.Gson;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Weimore
 *         2018/12/26.
 *         description:
 */

public class MediaUtil {

    public static RequestBody getJsonBody(Object object){
        return RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(object));
    }

    public static RequestBody getFileBody(File file){
        return RequestBody.create(MediaType.parse("multipart/form-data"), file);
    }


}

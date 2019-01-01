package com.weimore.caringhelper.entity;

import lombok.Data;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */
@Data
public class NearLocation {


    /**
     * address :
     * city : 杭州市
     * district : 拱墅区
     * key : 水月街-道路
     * pt : {"latitude":30.308535,"latitudeE6":30308535,"longitude":120.113114,"longitudeE6":120113114}
     * tag :
     * uid : 726bea4f9f3c50008d55ec16
     */

    private String address;
    private String city;
    private String district;
    private String key;
    private PtBean pt;
    private String tag;
    private String uid;


    @Data
    public static class PtBean {
        /**
         * latitude : 30.308535
         * latitudeE6 : 30308535
         * longitude : 120.113114
         * longitudeE6 : 120113114
         */

        private double latitude;
        private int latitudeE6;
        private double longitude;
        private int longitudeE6;


    }
}

package com.sep.isos_hc;

/**
 * Created by SPSW070059 on 10/10/2017.
 */

public class HospitalModel {
    private String name;
    private double lat;
    private double lng;
    private String ninnum;
    private String loc_type;
    private String sub_dist;
    private String dist;
    private String address;
    private String cont_num;
    private String mob_num;
    private String pincode;
    private String helpline;
    private String fax;
    private String email;
    private String website;
    private String category;
    private String distance;
    public HospitalModel(String name , double lat,double lng,String ninnum,String loc_type,String sub_dist,String dist,String distance,String address,String cont_num,String mob_num,String pincode,String helpline,String fax,String email,String website,String category) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.ninnum = ninnum;
        this.loc_type = loc_type;
        this.sub_dist = sub_dist;
        this.dist = dist;
        this.distance = distance;
        this.address = address;
        this.cont_num = cont_num;
        this.mob_num = mob_num;
        this.pincode = pincode;
        this.helpline = helpline;
        this.fax = fax;
        this.email = email;
        this.website = website;
        this.category = category;

    }
    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getNinnum() {
        return ninnum;
    }

    public String getLoc_type() {
        return loc_type;
    }

    public String getSub_dist() {
        return sub_dist;
    }

    public String getDist() {
        return dist;
    }

    public String getAddress() {
        return address;
    }

    public String getCont_num() {
        return cont_num;
    }

    public String getMob_num() {
        return mob_num;
    }

    public String getPincode() {
        return pincode;
    }

    public String getHelpline() {
        return helpline;
    }

    public String getFax() {
        return fax;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getCategory() {
        return category;
    }

    public String getDistance() {
        return distance;
    }

}

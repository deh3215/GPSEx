package com.example.a32150.gpsex;

/**
 * Created by 32150 on 2017/12/5.
 */

public class elevation {
    public MyResult[] results;
    public String status;
}

class MyLocation
{
    public double lat;
    public double lng;
}
class MyResult
{
    public double elevation;
    public MyLocation location;
    public double resolution;
}
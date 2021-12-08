package com.badabam.bookpath.config;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class PathDeterminant {
    private String OS_TYPE;

    public PathDeterminant(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dailyDirectory = dtf.format(now);
        if(isLINUX()){
            this.OS_TYPE = "/home/ec2-user/app/bookpath/"+dailyDirectory+"/";
        }else if(isWindow()){
            this.OS_TYPE = "C:\\Users\\ChoiJunho\\Desktop\\bookpath-showcase\\"+dailyDirectory+"\\";
        }else{
            this.OS_TYPE = "C:\\Users\\ChoiJunho\\Desktop\\bookpath-showcase\\"+dailyDirectory+"\\";
        }
    }

    public static boolean isLINUX(){
        return System.getProperty("os.name").toLowerCase().indexOf("linux")>=0;
    }
    public static boolean isWindow(){
        return System.getProperty("os.name").toLowerCase().indexOf("win")>=0;
    }
}

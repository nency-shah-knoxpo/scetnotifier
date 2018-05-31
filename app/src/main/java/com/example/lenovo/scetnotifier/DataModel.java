package com.example.lenovo.scetnotifier;

/**
 * Created by LENOVO on 23-09-2017.
 */
public class DataModel {
    //Workshop data model
    int workshopID;
    String Title = "";
    String Date = "";
    /*
        String Subject = "";
    */
    String Time = "";
    String Venue = "";
    String img = "";

    /*for fav data model*/
String favID="";
    String faviden="";
    String FavTitle="";
    String FavDate="";
    String FavImage="";

    public void setFavID(String favid) {
        this.favID = favid;
    }

    public String getFavID() {
        return this.favID;
    }

    public void setFaviden(String faviden) {
        this.faviden = faviden;
    }

    public String getFaviden() {
        return this.faviden;
    }


    public void setFavTitle(String Favtitle) {
        this.FavTitle = Favtitle;
    }

    public String getFavTitle() {
        return this.FavTitle;
    }

    public void setFavDate(String FavDate) {
        this.FavDate = FavDate;
    }

    public String getFavDate() {
        return this.FavDate;
    }

    public void setFavImage(String FavImage) {
        this.FavImage = FavImage;
    }

    public String getFavImage() {
        return this.FavImage;
    }

    /* data model for favourite over*/



    public void setworkshopID(int workshop_ID) {
        this.workshopID = workshop_ID;
    }

    public int getworkshopID() {
        return this.workshopID;
    }

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String W_date) {
        this.Date = W_date;
    }

    public String getTime() {
        return this.Time;
    }

    public void setTime(String W_Time) {
        this.Time = W_Time;
    }

    public String getVenue() {
        return this.Venue;
    }

    public void setVenue(String W_Venue) {
        this.Venue = W_Venue;
    }

    public String getimage() {
        return this.img;
    }

    public void setimage(String Image) {
        this.img = Image;
    }


    /*public String getSubject() {
        return this.Subject;
    }

    public void setSubject(String subject) {
        this.Subject = subject;
    }*/

    //Job data model

    int jobID;
    String comimg = "";
    String CompanyName = "";
    String ComLocation = "";
    String ComSkills = "";


    public void setjobID(int job_ID) {
        this.jobID = job_ID;
    }

    public int getjobID() {
        return this.jobID;
    }

    public void setcomimg(String p_Img) {
        this.comimg = p_Img;
    }

    public String getcomimg() {
        return this.comimg;
    }


    public String getCompanyName() {
        return this.CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }

    public String getComLocation() {
        return this.ComLocation;
    }

    public void setComLocation(String ComLocation) {
        this.ComLocation = ComLocation;
    }

    public String getComSkills() {
        return this.ComSkills;
    }

    public void setComSkills(String ComSkills) {
        this.ComSkills = ComSkills;
    }

    //Event Datamodel

    int eID;
    String EventName;
    String EventVenue;
    String EventDate;
    String EventDetails;
    String EventTime;
    String EventImage;

    public void setEventID(int e_ID) {
        this.eID = e_ID;
    }

    public int getEventID() {
        return eID;
    }

    public void setEventName(String Ename) {
        this.EventName = Ename;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventDetails(String EDetais) {
        this.EventDetails = EDetais;
    }

    public String getEventDetails() {
        return EventDetails;
    }

    public void setEventVenue(String EVenue) {
        this.EventVenue = EVenue;
    }

    public String getEventVenue() {
        return EventVenue;
    }

    public void setEventDate(String EDate) {
        this.EventDate = EDate;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventTime(String ETime) {
        this.EventTime = ETime;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventImage(String e_Img) {
        this.EventImage = e_Img;
    }

    public String getEventImage() {
        return EventImage;
    }


    //datamodel for noti

    int noti_ID;
    String noti;
    String day,mon,year;
    String notiImage;
    String notiden;

    public void setnotiID(int noti_ID) {
        this.noti_ID = noti_ID;
    }

    public int getnotiID() {
        return noti_ID;
    }

    public void setnotiiden(String notiiden) {
        this.notiden = notiiden;
    }

    public String getnotiiden() {
        return notiden;
    }


    public void setnoti(String noti) {
        this.noti = noti;
    }

    public String getnoti() {
        return noti;
    }

    public void setnotiday(String date) {
        this.day = date;
    }

    public String getnotiday() {
        return day;
    }

    public void setnotimonth(String date) {
        this.mon = date;
    }

    public String getnotimonth() {
        return mon;
    }


    public void setnotiyear(String date) {
        this.year = date;
    }

    public String getnotiyear() {
        return year;
    }

    public void setnotiImage(String notiimg) {
        this.notiImage = notiimg;
    }

    public String getnotiImage() {
        return notiImage;
    }




}

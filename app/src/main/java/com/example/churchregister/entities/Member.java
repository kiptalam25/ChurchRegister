package com.example.churchregister.entities;

public class Member {

    String id;
    String name;
    String idno;
    String phoneno;
    String marital_status;
    String baptized;
    String gender;
    String email;
    String date_of_birth;

//    String  date_of_marriage;
//    String marriage_by_rev;
//    String  date_of_joining;
//    String baptized_by;
//    String  date_of_baptism;
//    String home_county;
//    String sub_county;
//    String home_church;
//    String professional_area;
//    String gifted_in;


    public Member(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Member() {
    }

    public Member(String name, String idno, String phoneno, String marital_status, String baptized, String gender, String email, String date_of_birth) {
        this.name = name;
        this.idno = idno;
        this.phoneno = phoneno;
        this.marital_status = marital_status;
        this.baptized = baptized;
        this.gender = gender;
        this.email = email;
        this.date_of_birth = date_of_birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getBaptized() {
        return baptized;
    }

    public void setBaptized(String baptized) {
        this.baptized = baptized;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
}

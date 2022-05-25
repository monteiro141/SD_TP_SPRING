package com.example.noticiasquentinhas.entities;





import java.util.Date;

public class TopicFormSearch {
    private String name;


    private String date1;

    private String date2;

    public TopicFormSearch(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    @Override
    public String toString() {
        return "TopicFormSearch{" +
                "name='" + name + '\'' +
                ", date1=" + date1 +
                ", date2=" + date2 +
                '}';
    }
}

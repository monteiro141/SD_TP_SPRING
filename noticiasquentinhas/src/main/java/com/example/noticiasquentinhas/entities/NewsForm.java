package com.example.noticiasquentinhas.entities;

import org.mockito.internal.verification.Times;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewsForm {

    private String title;
    private String content;
    private String timeStart;
    private String timeEnd;
    private String creationDate;
    private String topic;

    public NewsForm() {
        this.creationDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public NewsForm(String timeStart, String timeEnd, String topic) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.topic = topic;
    }

    public NewsForm(String title, String content) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        creationDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public String toString() {
        return "NewsForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}

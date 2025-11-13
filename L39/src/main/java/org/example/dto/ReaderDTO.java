package org.example.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ReaderDTO {
    @JsonProperty("reader_id")
    private Integer readerId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    public ReaderDTO() {
    }

    public ReaderDTO(org.example.model.Reader reader) {
        this.readerId = reader.getReaderId();
        this.name = reader.getName();
        this.email = reader.getEmail();
        this.phone = reader.getPhone();
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
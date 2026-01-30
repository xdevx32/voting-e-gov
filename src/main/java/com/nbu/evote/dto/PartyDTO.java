package com.nbu.evote.dto;

import javax.validation.constraints.NotBlank;

public class PartyDTO {

    private Long id;

    @NotBlank(message = "Party name is required")
    private String name;

    private String number;

    private String slogan;

    private String imageUrl;

    private String groupImageUrl;

    private Integer ballotsCount;

    public PartyDTO() {
    }

    public PartyDTO(Long id, String name, String number, String slogan, String imageUrl, String groupImageUrl, Integer ballotsCount) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.slogan = slogan;
        this.imageUrl = imageUrl;
        this.groupImageUrl = groupImageUrl;
        this.ballotsCount = ballotsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }

    public Integer getBallotsCount() {
        return ballotsCount;
    }

    public void setBallotsCount(Integer ballotsCount) {
        this.ballotsCount = ballotsCount;
    }
}

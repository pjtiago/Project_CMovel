package com.estg.joaoviana.project_cmovel.Model;

/**
 * Created by PJ on 25/03/2017.
 */

public class Place {
    private String id;
    private String name;
    private String icon;
    private Double latitude;
    private Double longitude;
    private String vicinity;

    public Place(String id, String name, String icon, Double latitude, Double longitude, String vicinity) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vicinity = vicinity;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

package com.estg.joaoviana.project_cmovel.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PJ on 25/03/2017.
 */

public class PlacesList {
    private ArrayList<Place> placeList;

    public ArrayList<Place> getPlaceList() {
        return placeList;
    }

    /*public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }*/

    public void addToList(Place p){
        placeList.add(p);
    }

    public void emptyList(){
        placeList.clear();
    }
}

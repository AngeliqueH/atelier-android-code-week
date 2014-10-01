package com.atelier.angie.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListeCartes {

    private List<Carte> cartes = new ArrayList<Carte>();

    public void ajouter(Carte carte){
        cartes.add(carte);
    }

    public void melanger(){
        Collections.shuffle(cartes);
    }

    public Carte carteALaPlace(int place){
        return cartes.get(place);
    }
}

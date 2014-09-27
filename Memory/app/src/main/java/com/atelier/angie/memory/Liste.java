package com.atelier.angie.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Liste<T> {

    private ArrayList<T> list = new ArrayList<T>();

    public void ajouter(T objet){
        list.add(objet);
    }

    public void melanger(){
        Collections.shuffle(list);
    }

    public Carte carteAlaPlace(int place){
        return (Carte)list.get(place);
    }
}

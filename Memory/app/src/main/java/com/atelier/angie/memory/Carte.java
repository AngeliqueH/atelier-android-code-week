package com.atelier.angie.memory;

import android.view.View;
import android.widget.ImageView;

/**
 * Représentation d'une carte.
 * Contient toutes les informations necessaires à la gestion d'une carte.
 */
public class Carte {

    public int imageDeFace;
    public boolean estCoteFace=false;

    public Carte(int image){
        imageDeFace = image;
    }

}

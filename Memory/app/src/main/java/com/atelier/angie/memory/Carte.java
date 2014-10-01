package com.atelier.angie.memory;

/**
 * Représentation d'une carte.
 * Contient toutes les informations necessaires à la gestion d'une carte.
 */
public class Carte {

    private int imageDeFace;
    private boolean coteFace = false;

    public Carte(int image) {
        imageDeFace = image;
    }

    public int getImageDeFace() {
        return imageDeFace;
    }

    public boolean estCoteFace() {
        return coteFace;
    }

    public void cacherCarte() {
        this.coteFace = false;
    }

    public void revelerCarte() {
        this.coteFace = true;
    }

}

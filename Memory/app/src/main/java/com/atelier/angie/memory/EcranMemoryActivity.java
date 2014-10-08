package com.atelier.angie.memory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.os.Handler;

/**
 * Code permettant de réagir aux interactions de l'utilisateur: click sur les cartes, sur les boutons.
 */
public class EcranMemoryActivity extends Activity {

    private static final int NOMBRE_D_EMPLACEMENTS=6;
    private static final int IMAGE_DOS=R.drawable.dos2;


    private ImageView[] listeEmplacements = new ImageView[NOMBRE_D_EMPLACEMENTS];
    private List<Carte> listeCarte= new ArrayList<Carte>();
    private Carte carteRetournee = null;
    private int numeroEmplacementCarteRetournee=0;
    private List<Carte> carteGagnee=new ArrayList<Carte>();


    /** Creation de la vue*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_memory);
    }

    /** Appelé chaque fois que la vue est affichée */
    @Override
    protected void onResume() {
        super.onResume();
        trace("Lancement du jeu");
        initierLePlateauDeJeu();
    }

    /**
     * Prépare le plateau de jeu: distribue les cartes sur les emplacements.
     */
    private void initierLePlateauDeJeu(){

        //Lien avec les composants graphiques de la vue activity_ecran_memory.xml
        //Les 4 cases sont enregistrées dans une liste et le click sur la case est écouté.
        listeEmplacements[0]=(ImageView)findViewById(R.id.emplacement1View);
        listeEmplacements[0].setOnClickListener(quandJeToucheLEmplacement(0));
        listeEmplacements[1]=(ImageView)findViewById(R.id.emplacement2View);
        listeEmplacements[1].setOnClickListener(quandJeToucheLEmplacement(1));
        listeEmplacements[2]=(ImageView)findViewById(R.id.emplacement3View);
        listeEmplacements[2].setOnClickListener(quandJeToucheLEmplacement(2));
        listeEmplacements[3]=(ImageView)findViewById(R.id.emplacement4View);
        listeEmplacements[3].setOnClickListener(quandJeToucheLEmplacement(3));

        listeEmplacements[4]=(ImageView)findViewById(R.id.emplacement5View);
        listeEmplacements[4].setOnClickListener(quandJeToucheLEmplacement(4));
        listeEmplacements[5]=(ImageView)findViewById(R.id.emplacement6View);
        listeEmplacements[5].setOnClickListener(quandJeToucheLEmplacement(5));

        //Création des cartes qui seront ensuite distribuées sur les emplacements
        Carte carte1 = new Carte(R.drawable.robot1);
        Carte carte2 = new Carte(R.drawable.robot2);
        Carte carte3 = new Carte(R.drawable.robot1);
        Carte carte4 = new Carte(R.drawable.robot2);
        Carte carte5 = new Carte(R.drawable.robot3);
        Carte carte6 = new Carte(R.drawable.robot3);

        listeCarte.add(carte1);
        listeCarte.add(carte2);
        listeCarte.add(carte3);
        listeCarte.add(carte4);
        listeCarte.add(carte5);
        listeCarte.add(carte6);
    }

    /**
     * Réaction au click sur l'emplacement dont le numéro est en paramètre.
     * Les emplacements sont numérotés de 0 à 3, en partant de en haut à gauche.
     * @param numeroEmplacement
     * @return
     */
    private View.OnClickListener quandJeToucheLEmplacement(final int numeroEmplacement) {
        return new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Carte carteTouchee = listeCarte.get(numeroEmplacement);
                if(carteTouchee.estCoteFace){
                    trace("Carte de face touchee à la place "+numeroEmplacement+" on ne fait rien");
                }
                else
                {
                    trace("Carte de dos touchee à la place"+numeroEmplacement);
                    carteTouchee.estCoteFace=VRAI;
                    listeEmplacements[numeroEmplacement].setImageResource(carteTouchee.imageDeFace);
                    trace("timer...");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if(carteRetournee!=null){
                                trace("Il y a une carte retournee avant celle là, il faut verifier si c'est la meme carte");

                                if(carteRetournee.imageDeFace == carteTouchee.imageDeFace){
                                    trace("c'est une paire, on garde les deux cartes retournees");
                                    listeEmplacements[numeroEmplacement].setAlpha(0.5f);
                                    listeEmplacements[numeroEmplacementCarteRetournee].setAlpha(0.5f);
                                    carteGagnee.add(carteTouchee);
                                    carteGagnee.add(carteRetournee);

                                    carteRetournee=null;

                                    //On regarde si le jeu est fini, c'est a dire si toutes les cartes sont retournees
                                    if(carteGagnee.size()==NOMBRE_D_EMPLACEMENTS){
                                        trace("Gagné !");
                                        afficherPopupGagne();

                                    }

                                }
                                else
                                {
                                    trace("Les deux cartes sont differentes, on les retourne");
                                    carteRetournee.estCoteFace=FAUX;
                                    carteTouchee.estCoteFace=FAUX;
                                    listeEmplacements[numeroEmplacement].setImageResource(IMAGE_DOS);
                                    listeEmplacements[numeroEmplacementCarteRetournee].setImageResource(IMAGE_DOS);
                                    carteRetournee=null;
                                }
                            }
                            else
                            { //Sauvegarde de la carte retournee pour le prochain touch
                                trace("carte retournee sauvegardee");
                                numeroEmplacementCarteRetournee = numeroEmplacement;
                                carteRetournee = carteTouchee;
                            }
                        }
                    }, 500);


                }
            }
        };
    }

    private void afficherPopupGagne(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_popup_gagne)
                .setTitle(R.string.titre_popup_gagne);

        builder.setPositiveButton(R.string.boutton_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                relancerLeJeu();
                melangerLesCartes();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void melangerLesCartes(){
        Collections.shuffle(listeCarte);
    }

    private void relancerLeJeu(){
        for(Carte uneCarte: listeCarte){
            uneCarte.estCoteFace= FAUX;
        }

        for(ImageView emplacement: listeEmplacements){
            emplacement.setAlpha(1.0f);
            emplacement.setImageResource(IMAGE_DOS);
        }

        carteGagnee.clear();
        carteRetournee = null;
        numeroEmplacementCarteRetournee=0;
    }




    private Activity getActivity(){
        return this;
    }

    private void trace(String log){
        Log.d("MEMORY", log);
    }

    private static final boolean VRAI = true;
    private static final boolean FAUX = false;
}
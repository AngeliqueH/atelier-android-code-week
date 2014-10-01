package com.atelier.angie.memory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EcranMemoryActivity extends Activity {

    private static final int NOMBRE_D_EMPLACEMENTS = 4;
    private static final int IMAGE_DOS = R.drawable.dos2;

    private ImageView[] listeEmplacements = new ImageView[NOMBRE_D_EMPLACEMENTS];
    private List<Carte> listeCarte = new ArrayList<Carte>();
    private Carte carteRetournee = null;
    private int numeroEmplacementCarteRetournee = 0;
    private List<Carte> carteGagnee = new ArrayList<Carte>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_memory);

        initierLePlateauDeJeu();
    }

    private void initierLePlateauDeJeu() {

        listeEmplacements[0] = (ImageView) findViewById(R.id.emplacement1View);
        listeEmplacements[0].setOnClickListener(quandJeToucheLEmplacement(0));
        listeEmplacements[1] = (ImageView) findViewById(R.id.emplacement2View);
        listeEmplacements[1].setOnClickListener(quandJeToucheLEmplacement(1));
        listeEmplacements[2] = (ImageView) findViewById(R.id.emplacement3View);
        listeEmplacements[2].setOnClickListener(quandJeToucheLEmplacement(2));
        listeEmplacements[3] = (ImageView) findViewById(R.id.emplacement4View);
        listeEmplacements[3].setOnClickListener(quandJeToucheLEmplacement(3));

        Carte carte1 = new Carte(R.drawable.robot1);
        Carte carte2 = new Carte(R.drawable.robot2);
        Carte carte3 = new Carte(R.drawable.robot1);
        Carte carte4 = new Carte(R.drawable.robot2);
        listeCarte.add(carte1);
        listeCarte.add(carte2);
        listeCarte.add(carte3);
        listeCarte.add(carte4);
    }

    private View.OnClickListener quandJeToucheLEmplacement(final int numeroEmplacement) {
        return new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Carte carteTouchee = listeCarte.get(numeroEmplacement);
                if (carteTouchee.estCoteFace()) {
                    trace("Carte de face touchee à la place " + numeroEmplacement + " on ne fait rien");
                } else {
                    trace("Carte de dos touchee à la place" + numeroEmplacement);
                    carteTouchee.revelerCarte();
                    listeEmplacements[numeroEmplacement].setImageResource(carteTouchee.getImageDeFace());
                    trace("timer...");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (carteRetournee != null) {
                                trace("Il y a une carte retournee avant celle là, il faut verifier si c'est la meme carte");

                                if (carteRetournee.getImageDeFace() == carteTouchee.getImageDeFace()) {
                                    trace("c'est une paire, on garde les deux cartes retournees");
                                    listeEmplacements[numeroEmplacement].setAlpha(0.5f);
                                    listeEmplacements[numeroEmplacementCarteRetournee].setAlpha(0.5f);
                                    carteGagnee.add(carteTouchee);
                                    carteGagnee.add(carteRetournee);

                                    carteRetournee = null;

                                    //On regarde si le jeu est fini, c'est a dire si toutes les cartes sont retournees
                                    if (carteGagnee.size() == NOMBRE_D_EMPLACEMENTS) {
                                        trace("Gagné !");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage(R.string.message_popup_gagne)
                                                .setTitle(R.string.titre_popup_gagne);

                                        builder.setPositiveButton(R.string.boutton_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                relancerLeJeu();
                                                melangerLesCartes();
                                            }
                                        });
                                        /*builder.setNegativeButton(R.string.boutton_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                relancerLeJeu();
                                                melangerLesCartes();
                                            }
                                        });*/
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                } else {
                                    trace("Les deux cartes sont differentes, on les retourne");
                                    carteRetournee.cacherCarte();
                                    carteTouchee.cacherCarte();
                                    listeEmplacements[numeroEmplacement].setImageResource(IMAGE_DOS);
                                    listeEmplacements[numeroEmplacementCarteRetournee].setImageResource(IMAGE_DOS);
                                    carteRetournee = null;
                                }
                            } else { //Sauvegarde de la carte retournee pour le prochain touch
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


    private void melangerLesCartes() {
        Collections.shuffle(listeCarte);
    }

    private void relancerLeJeu() {
        for (Carte uneCarte : listeCarte) {
            uneCarte.cacherCarte();
        }

        for (ImageView emplacement : listeEmplacements) {
            emplacement.setAlpha(1.0f);
            emplacement.setImageResource(IMAGE_DOS);
        }

        carteGagnee.clear();
        carteRetournee = null;
        numeroEmplacementCarteRetournee = 0;
    }


    private Activity getActivity() {
        return this;
    }

    private void trace(String log) {
        Log.d("MEMORY", log);
    }

}
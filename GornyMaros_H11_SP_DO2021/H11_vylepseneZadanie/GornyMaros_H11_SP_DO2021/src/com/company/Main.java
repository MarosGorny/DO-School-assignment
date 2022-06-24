package com.company;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        DualnaHeuristika heu;
        heu = new DualnaHeuristika("H1_a.txt","H1_c.txt"); //Nacitanie ceny a hmotnosti
        heu.dualnaVsuvacaHeurestika();  //Pouzijem dualnu vsuvacu heuristiku, podla kriteria "Vlož prvok z dosiaľ nespracovaných
                                                                                            //prvkov, ktorý má najväčšiu hmotnosť"
        heu.vypisVysledkov();   //Vypis vysledkov
        heu.zapisVysledokDoSuboru("Výstup po prvom vylepšen.txt"); //Zapis heurestiky do .txt suboru

        System.out.println();
        System.out.println("--------------------------------------------");


        heu.vylepsenieHeurestiky(); //Pouzijem vylepsenie: strategiu vymeny prvkov
        heu.vypisVysledkov();   //Vypis vysledkov
        heu.zapisVysledokDoSuboru("Výstup po druhom vylepšení.txt"); //Zapis vylepsenej heuristiky do .txt suboru

        System.out.println();
    }
}

package com.company;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DualnaHeuristika {
    private int[] a; //hmotnost
    private int[] c; //cena
    private boolean[] z; // vybrany/nevybrany prvok
    private int m; // pocet riadkov v textovom subore
    private int aktualnaHmotnost;
    private int aktualnaCena;
    private int aktualnyPocetPrvkov;

    private static final int r = 200;   //minimalny pocet prvkov
    private static final int K = 14000; //minimalna kapacita

    public DualnaHeuristika(String nazovSuboruHmotnost, String nazovSuboruCena) throws FileNotFoundException {
        aktualnaCena = 0;
        aktualnaHmotnost = 0;
        aktualnyPocetPrvkov = 0;
        m = getPocetRiadkov(nazovSuboruHmotnost);  //Ziskam pocet riadkov z textoveho suboru

        a = new int[m];      //inicializacia s nulami
        c = new int[m];      //inicializacia s nulami
        z = new boolean[m];  //batoh je naplneny s false

        nacitajZoSuboruHmotnost(nazovSuboruHmotnost);   // do a[] priradi dane hodnoty z .txt
        nacitajZoSuboruCenu(nazovSuboruCena);           // do c[] priradi dane hodnoty z .txt

    }

    public void vypisVysledkov() {
        System.out.println("Aktuálna cena batohu:\t" + aktualnaCena);
        System.out.println("Aktuálna hmotnosť:\t\t" + aktualnaHmotnost);
        System.out.println("Aktuálny počet prvkov: \t" + aktualnyPocetPrvkov);
        System.out.println();
        System.out.println("Hodnota účelovej funkcie:\t" + aktualnaCena);

        System.out.println();
        System.out.println("Index vybraného predmetu... (1 až " + m + ")");
        for (int i = 0; i < m; i++) {
            if ((i + 1) % 100 == 0) {         //po stovkach spravi new line
                System.out.println();
            }
            if (z[i] == true) {
                System.out.print(i + 1 + ", "); //vypise index predmetu ktory je v batohu
            }
        }
    }

    public void dualnaVsuvacaHeurestika() {
        for (int i = 0; i < m; i++) {
            if (aktualnyPocetPrvkov >= r && aktualnaHmotnost >= K) {    //Ak splnam dane podmienky zadania, tak koncim
                break;
            } else {                                                    //ak podmienky nesplnam, pokracujem dalej
                vlozPrvokDoBatohuSMaxHmotnostou(getIndexMaximaHmotnosti());//vlozim do batohu prvok s najvacsou hmotnostou
            }
        }
    }

    public void vylepsenieHeurestiky() {
        int indexNahradnehoPrvku = -1;
        int novaCena = aktualnaCena;
        int novaHmotnost = aktualnaHmotnost;


        for (int i = 0; i < m; i++) {           //prejde kazdy prvok
            if (z[i] == true) {                 //ak je dany prvok zaradeny, skusim ho vymenit
                for (int j = 0; j < m; j++) {   //skusa dany prvok vymenit so vsetkymi ostatnymi prvkami
                    if (j == i || (z[j] == true)) continue;     //Ak chcem vymenit sam seba, alebo prvok, ktory je uz zaradeny, tak ho preskocim
                    int hmotnostPoVymene = aktualnaHmotnost - a[i] + a[j];  //Vypocitana hmotnost po vymene
                    int cenaPoVymene = aktualnaCena - c[i] + c[j];          //Vypocitana cena po vymene
                    if (hmotnostPoVymene >= K && cenaPoVymene < aktualnaCena) { //Ak splna podmienku K a cena je lepsia
                        if (cenaPoVymene > novaCena) continue;  //Ak som nasiel druheho kandidata na vymenu,tak
                                                                //Ak ma novy kandidat vacsiu cenu, tak ho preskocim
                        indexNahradnehoPrvku = j;   //Zapamatam si index vymeneneho prvku
                        novaCena = cenaPoVymene;    //Zapamatam si novuCenu kandidata
                        novaHmotnost = hmotnostPoVymene; //Zapamatam si novu hmotnost kandidata
                    }
                }
                if (indexNahradnehoPrvku != -1) {   //Ak sa nasiel nejaky kandidat, tak...
                    z[i] = false;                   //Predchodcu z batohu vyhodime
                    z[indexNahradnehoPrvku] = true; //Zaradime do batohu vymeneny prvok

                    aktualnaHmotnost = novaHmotnost; //Aktualizujem hmotnost batohu
                    aktualnaCena = novaCena;    //Aktualizujem cenu batohu

                    indexNahradnehoPrvku = -1; //Resetujem index nahradneho prvku
                }
            }
        }
    }

    public void vlozPrvokDoBatohuSMaxHmotnostou(int indexPrvku) {
        z[indexPrvku] = true;               //oznacim dany prvok, ze uz som ho vlozil do batohu
        aktualnaHmotnost += a[indexPrvku];  //pripocitam hmotnost prvku
        aktualnaCena += c[indexPrvku];      //pripocitam cenu prvku
        aktualnyPocetPrvkov++;             //pripocitam jednu jednotku k aktualnemu poctvu prvkom
    }

    public int getIndexMaximaHmotnosti() {
        int docasneMaximum = -1;
        int indexMaxima = -1;
        int cenaMaxima = -1;

        for (int i = 0; i < m; i++) {
            if (a[i] >= docasneMaximum && z[i] != true) {        //ak dany prvok este nie je v batohu a je zatial najvacsi
                if (a[i] == docasneMaximum && cenaMaxima < c[i]) {
                    continue;
                } else {
                    docasneMaximum = a[i];                          //zvolim ho ako maximalny prvok
                    indexMaxima = i;                                //priradim index maximalneho prvku
                    cenaMaxima = c[i];
                }


            }
        }

        return indexMaxima;                //vratim index maximalneho prvko
    }

    public int getPocetRiadkov(String nazovSuboru) {
        Path cesta = Paths.get(nazovSuboru);
        int riadky = 0;
        try {
            riadky = (int) Files.lines(cesta).count(); //pretypovanie na INT, vypocita kolko tam je riadkov

        } catch (IOException e) {
            System.out.println("Vyskytla sa chyba!");
            e.printStackTrace();
        }

        return riadky;
    }

    public void nacitajZoSuboruHmotnost(String nazovSuboru) throws java.io.FileNotFoundException {
        // vytvorenie novej instancie triedy Scanner pre citanie z textoveho suboru
        java.util.Scanner citac = new java.util.Scanner(new java.io.File(nazovSuboru));
        for (int i = 0; i < m; i++) { //pre vsetky riadky
            a[i] = citac.nextInt();  // nacitanie hodnoty prvku zo suboru
        }

        citac.close();
    }

    public void nacitajZoSuboruCenu(String nazovSuboru) throws java.io.FileNotFoundException {
        // vytvorenie novej instancie triedy Scanner pre citanie z textoveho suboru
        java.util.Scanner citac = new java.util.Scanner(new java.io.File(nazovSuboru));
        for (int i = 0; i < m; i++) { //pre vsetky riadky
            c[i] = citac.nextInt();  // nacitanie hodnoty prvku zo suboru
        }

        citac.close();
    }

    public void zapisVysledokDoSuboru(String nazovSuboru) {

        try {
            FileWriter zapis = new FileWriter(nazovSuboru);
            zapis.write("Počet predmetov = " + aktualnyPocetPrvkov + "\nHmotnosť batohu = " + aktualnaHmotnost + "\nÚčelová funkcia = " + aktualnaCena + "\n\n");
            zapis.write("Index prvku\tHmotnosť\tCena\n");
            for (int i = 0; i < m; i++) {
                if (z[i] == true) {
                    zapis.write(String.valueOf(i + 1) + "\t\t" + a[i] + "\t\t" + c[i] + "\n");
                }
            }
            zapis.close();
        } catch (IOException e) {
            System.out.println("Vyskytla sa chyba!");
            e.printStackTrace();
        }
    }


}

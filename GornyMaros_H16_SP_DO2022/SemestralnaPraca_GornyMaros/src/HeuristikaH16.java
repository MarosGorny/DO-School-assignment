import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class HeuristikaH16 {
    private int[] a; //hmotnost
    private int[] c; //cena
    private double[] koeficient; //pomer koeficientov cj/aj
    private boolean[] z; // vybrany/nevybrany prvok
    private int aktualnaHmotnost;
    private int aktualnaCena;
    private int aktualnyPocetPrvkov;

    private static final int n = 500;   //pocet prvkov v subore
    private static final int r = 350;   //minimalny pocet prvkov
    private static final int K = 10500; //minimalna kapacita

    public HeuristikaH16(String nazovSuboruHmotnost, String nazovSuboruCena) throws FileNotFoundException {


        a = new int[n];      //inicializacia hmotnost s nulami
        c = new int[n];      //inicializacia ceny s nulami
        z = new boolean[n];  //batoh je naplneny s false
        koeficient = new double[n]; //inicializcia koeficientov s nulami

        Arrays.fill(z,true); //batoh je naplneny s true
        nacitajZoSuboruHmotnost(nazovSuboruHmotnost);   // do a[] priradi dane hodnoty z .txt
        nacitajZoSuboruCenu(nazovSuboruCena);           // do c[] priradi dane hodnoty z .txt

        for (int i = 0; i < n; i++) {
            koeficient[i] = c[i]/a[i];  // cena/hmotnost
        }

        aktualnaCena = Arrays.stream(c).sum();          //Sum vsetkych cien
        aktualnaHmotnost = Arrays.stream(a).sum();      //Sum vsetkych hmotnosti
        aktualnyPocetPrvkov = n;                        //pocet prvkov v subore

    }

    public void vypisVysledkov() {
        System.out.println("Aktuálna cena batohu:\t" + aktualnaCena);
        System.out.println("Aktuálna hmotnosť:\t\t" + aktualnaHmotnost);
        System.out.println("Aktuálny počet prvkov: \t" + aktualnyPocetPrvkov);
        System.out.println();
        System.out.println("Hodnota účelovej funkcie:\t" + aktualnaCena);

        System.out.println();
        System.out.println("Index vybraného predmetu... (1 až " + n + ")");
        for (int i = 0; i < n; i++) {
            if ((i + 1) % 100 == 0) {         //po stovkach spravi new line
                System.out.println();
            }
            if (z[i] == true) {
                System.out.print(i + 1 + ", "); //vypise index predmetu ktory je v batohu
            }
        }
    }

    public int getIndexNajvacsiKoeficientZaDanychpodmienok(){
        double docasneMaximum = -1;
        int indexMaxima = -1;

        for (int i = 0; i < n; i++) {
            if (koeficient[i] > docasneMaximum && z[i] == true) {   //ak je dany prvok vacsi a este je v batohu
                if ((aktualnaHmotnost - a[i]) >= K) {       //Ak je splnena podmienka pre hmotnost
                    docasneMaximum = koeficient[i];
                    indexMaxima = i;
                }
            }
        }
        return  indexMaxima;                //vratim index s najvacsim koeficientom
    }

    public void odstranPrvokPodlaKoeficientu(int indexPrvku) {
        z[indexPrvku] = false;               //oznacim dany prvok, ze som ho vybral z batohu
        aktualnaHmotnost -= a[indexPrvku];  //odpocitam hmotnost prvku
        aktualnaCena -= c[indexPrvku];      //odpocitam cenu prvku
        aktualnyPocetPrvkov --;             //odpocitam jednu jednotku od aktualnemu poctvu prvkom
    }

    public void primarnaVyhodnostnyKoeficient() {
        for (int i = 0; i < n; i++) {
            int indexNajvacsiehoKoeficientu = getIndexNajvacsiKoeficientZaDanychpodmienok();

            if (indexNajvacsiehoKoeficientu == -1) {            //Ak uz z batohu nemozem odstranit prvok
                break;
            }

            if (aktualnyPocetPrvkov > r) {                      //Ak este mozem odobrat predmet
                odstranPrvokPodlaKoeficientu(indexNajvacsiehoKoeficientu);
            } else {
                break;
            }
        }
    }

    public void nacitajZoSuboruHmotnost(String nazovSuboru) throws java.io.FileNotFoundException
    {
        // vytvorenie novej instancie triedy Scanner pre citanie z textoveho suboru
        java.util.Scanner citac = new java.util.Scanner(new java.io.File(nazovSuboru));
        for (int i=0; i<n; i++) { //pre vsetky riadky
            a[i]=citac.nextInt();  // nacitanie hodnoty prvku zo suboru
        }

        citac.close();
    }

    public void nacitajZoSuboruCenu(String nazovSuboru) throws java.io.FileNotFoundException
    {
        // vytvorenie novej instancie triedy Scanner pre citanie z textoveho suboru
        java.util.Scanner citac = new java.util.Scanner(new java.io.File(nazovSuboru));
        for (int i=0; i<n; i++) { //pre vsetky riadky
            c[i]=citac.nextInt();  // nacitanie hodnoty prvku zo suboru
        }

        citac.close();
    }

    public void zapisVysledokDoSuboru(String nazovSuboru) {

        try {
            FileWriter zapis = new FileWriter(nazovSuboru);
            zapis.write("Počet predmetov = " + aktualnyPocetPrvkov + "\nHmotnosť batohu = " + aktualnaHmotnost + "\nÚčelová funkcia = " + aktualnaCena + "\n\n");
            zapis.write("Index prvku\tHmotnosť\tCena\n");
            for (int i = 0; i < n; i++) {
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

    public void vylepsenieHeurestiky() {
        int indexNahradnehoPrvku = -1;
        int novaCena = aktualnaCena;
        int novaHmotnost = aktualnaHmotnost;


        for (int i = 0; i < n; i++) {           //prejde kazdy prvok
            if (z[i] == true) {                 //ak je dany prvok zaradeny, skusim ho vymenit
                for (int j = 0; j < n; j++) {   //skusa dany prvok vymenit so vsetkymi ostatnymi prvkami
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
}
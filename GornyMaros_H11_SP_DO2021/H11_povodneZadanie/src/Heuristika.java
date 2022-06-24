import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Heuristika {
    private int [] a; //hmotnost
    private int [] c; //cena
    private boolean [] z; // vybrany/nevybrany prvok
    private int m; // pocet riadkov v textovom subore
    private int aktualnaHmotnost;
    private int aktualnaCena;
    private int aktualnyPocetPrvkov;

    private static final int r=200;   //minimalny pocet prvkov
    private static final int K=14000; //minimalna kapacita

    public Heuristika(String nazovSuboruHmotnost, String nazovSuboruCena) throws FileNotFoundException {
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

    public void vlozPrvokDoBatohuSMaxHmotnostou(int indexPrvku) {
        z[indexPrvku] = true;               //oznacim dany prvok, ze uz som ho vlozil do batohu
        aktualnaHmotnost += a[indexPrvku];  //pripocitam hmotnost prvku
        aktualnaCena += c[indexPrvku];      //pripocitam cenu prvku
        aktualnyPocetPrvkov ++;             //pripocitam jednu jednotku k aktualnemu poctvu prvkom
    }

    public int getIndexMaximaHmotnosti(){
        int docasneMaximum = -1;
        int indexMaxima = -1;

        for (int i = 0; i < m; i++) {
            if (a[i] > docasneMaximum && z[i] != true) {        //ak dany prvok este nie je v batohu a je zatial najvacsi
                docasneMaximum = a[i];                          //zvolim ho ako maximalny prvok
                indexMaxima = i;                                //priradim index maximalneho prvku
            }
        }

        return  indexMaxima;                //vratim index maximalneho prvko
    }

    public int getPocetRiadkov(String nazovSuboru){
        Path cesta = Paths.get(nazovSuboru);
        int riadky = 0;
        try {
            riadky = (int) Files.lines(cesta).count(); //pretypovanie na INT, vypocita kolko tam je riadkov

        }   catch (IOException e) {
            System.out.println("Vyskytla sa chyba!");
            e.printStackTrace();
        }
        return riadky;
    }
    public void nacitajZoSuboruHmotnost(String nazovSuboru) throws java.io.FileNotFoundException
    {
        // vytvorenie novej instancie triedy Scanner pre citanie z textoveho suboru
        java.util.Scanner citac = new java.util.Scanner(new java.io.File(nazovSuboru));
        for (int i=0; i<m; i++) { //pre vsetky riadky
            a[i]=citac.nextInt();  // nacitanie hodnoty prvku zo suboru
        }

        citac.close();
    }

    public void nacitajZoSuboruCenu(String nazovSuboru) throws java.io.FileNotFoundException
    {
        // vytvorenie novej instancie triedy Scanner pre citanie z textoveho suboru
        java.util.Scanner citac = new java.util.Scanner(new java.io.File(nazovSuboru));
        for (int i=0; i<m; i++) { //pre vsetky riadky
            c[i]=citac.nextInt();  // nacitanie hodnoty prvku zo suboru
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

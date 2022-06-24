import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        HeuristikaH16 heurestika;
        heurestika = new HeuristikaH16("H6_a.txt","H6_c.txt");
        heurestika.primarnaVyhodnostnyKoeficient();

        heurestika.vypisVysledkov();
        heurestika.zapisVysledokDoSuboru("Výstup_H16 - pôvodná úloha.txt");
        System.out.println("");
        System.out.println("============================Vylepšené riešenie============================");

        heurestika.vylepsenieHeurestiky();
        heurestika.vypisVysledkov();
        heurestika.zapisVysledokDoSuboru("Výstup_H16 - vylepšená úloha.txt");
    }
}

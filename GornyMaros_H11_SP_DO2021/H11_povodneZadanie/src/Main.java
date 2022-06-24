import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Heuristika heu;
        heu = new Heuristika("H1_a.txt","H1_c.txt");
        heu.dualnaVsuvacaHeurestika();

        heu.vypisVysledkov();
        heu.zapisVysledokDoSuboru("Výstup - pôvodná úloha H11.txt");
        System.out.println("");
    }
}

/*****************************************************************************
 * Algoritmid ja andmestruktuurid. LTAT.03.005
 * 2022/2023 sügissemester
 *
 * Kodutöö. Ülesanne nr 7a
 * Teema: Graafi läbimine
 *
 * Autor: Mihkel Hani
 *
 * Kasutab andmefaile Tipp.java, Kaar.java??
 *
 *****************************************************************************/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Kodu7aTest {

    /**
     * Meetod, mis vastavalt etteaantud väärtuse x ja lubatud tankimiste arvu k järgi leiab lähtelinnast alustades need linnad, mis on k tankimise kaugusel.
     * @param lähtelinn Antud lähtelinn.
     * @param x Kaugus kuhu ühe tankimisega jõuab.
     * @param k Lubatud tankimiste arv.
     * @param linnad Linnanimede massiiv.
     * @param M Linnade naabrusmaatriks.
     * @return Linnad kuhu niimoodi jõuab.
     */
    public static String[] jõuame(String lähtelinn, int x, int k, String[] linnad, int[][] M) {
        if (k == 0) { // Kui tankida ei saa, siis ei saa kuhugile.
            return new String[]{};
        }
        // Loon järjendi tippude jaoks.
        ArrayList<Tipp> külgnevus_struktuur = new ArrayList<>();
        // Lisan tipud järjendisse.
        for (String linn : linnad) {
            külgnevus_struktuur.add(new Tipp(linn));
        }
        // Käin naabrusmaatriksi läbi ja lisan kõigile tippudele ka kaared.
        for (int i = 0; i < M.length; i++) {
            Tipp alg = külgnevus_struktuur.get(i);
            for (int j = 0; j < M[i].length; j++) {
                // Pole mõtet teha kaart, kui sinna ei jõua.
                if (M[i][j] > 0 && M[i][j] <= x) { // Järeldan et läbitav distants on positiivne.
                    Tipp lõpp = külgnevus_struktuur.get(j);
                    Kaar lisatav = new Kaar(alg, lõpp, M[i][j]);
                    alg.lisaKaar(lisatav);
                }
            }
        }
        // Otsin üles esimese tipu.
        int lähtelinna_indeks = 0;
        /*for (int i = 0; i < külgnevus_struktuur.size(); i++) {
            if (külgnevus_struktuur.get(i).info == lähtelinn) {
                lähtelinna_indeks = i;
                break;
            }
        }*/
        for (int i = 0; i < linnad.length; i++) {
            if (linnad[i].equals(lähtelinn)) {
                lähtelinna_indeks = i;
                break;
            }
        }
        // Võtan esimese tipu.
        Tipp alg = külgnevus_struktuur.get(lähtelinna_indeks);
        // Panen tipu abivälja väärtusega 1.
        alg.x = k+1;
        // Algoritmi tööpõhimõte:
        // Uuendan abivälju kõigile tippudele,
        // mille abiväli on väärtusega 0 ja mis on ühendatud
        // tipuga mille abiväli on väärtusega k+1.
        // Lõpus tagastan kõik tipud mille abiväli on väärtusega 1.
        while (k > 0) {
            // Käin külgnevusstruktuuri läbi.
            for (Tipp tipp : külgnevus_struktuur) {
                // Kui tippu pole töödeldud.
                if (tipp.x == 0) {
                    // Teen kaarte järjekorra.
                    Queue<Kaar> kontrollitavad_kaared = new LinkedList<>();
                    // Panen tipu kaared järjekorda.
                    tipp.getKaared(kontrollitavad_kaared);
                    // Käin kaarte järjekorra läbi.
                    while (!kontrollitavad_kaared.isEmpty()) {
                        Kaar töödeldav = kontrollitavad_kaared.poll();
                        // Kui on ühendatud eelmise tipuga.
                        if (töödeldav.lõpp.x == k + 1) {
                            // Märgin töödelduna.
                            töödeldav.alg.x = k;
                            break;
                        }
                    }
                }
            }
            k--;
        }
        ArrayList<String> vaheList = new ArrayList<>();
        for (Tipp tipp : külgnevus_struktuur) {
            if (tipp.x == 1)
                vaheList.add(tipp.info);
        }
        String[] tagastus = new String[vaheList.size()];
        for (int i = 0; i < tagastus.length; i++) {
            tagastus[i] = vaheList.get(i);
        }
        // Tagastan massiivi.
        return tagastus;
    }

    public static void main(String[] args) {
        String[] linnad = new String[] {"A", "B", "C", "D", "E", "F"};
        int[][] M = new int[][] {
                new int[] {  0,  1,  1,100,  1, -1},
                new int[] {  1,  0,  1,  1, -1,100},
                new int[] {  1,  1,  0, -1,100, -1},
                new int[] {100,  1, -1,  0,  1,  1},
                new int[] {  1, -1,100,  1,  0,  1},
                new int[] { -1,100, -1,  1,  1,  0}
        };
        int x = 50;
        int k = 2;
        String[] tagastus = jõuame("B", x, k, linnad, M);
        for (String s : tagastus) {
            System.out.println(s);
        }
    }

}//Kodu7a

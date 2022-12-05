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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Kodu7a {

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
            for (int j = 0; j < M[i].length; j++) {
                // Pole mõtet teha kaart, kui sinna ei jõua.
                if (M[i][j] > 0 && M[i][j] <= x) { // Järeldan et läbitav distants on positiivne.
                    Tipp alg = külgnevus_struktuur.get(i);
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
        // Teen töödeltavatele tippudele ja kaartele järjekorra.
        Queue<Tipp> töödeldavad_tipud = new LinkedList<>();
        // Panen esimese tipu järjekorda.
        töödeldavad_tipud.offer(alg);
        // Märgin, et järjekorras.
        // 0 - töötlemata, pole järjekorras.
        // 1 - töötlemata, on järjekorras.
        // 2 - töödeldud.
        alg.x = 1;
        // Nii kaua kui tankimisi veel järel.
        while (k > 0) {
            // Salvestan mitu tippu järjekorrast vaja läbi käia.
            int mitu_töödelda = töödeldavad_tipud.size();
            // Käin need läbi.
            while (mitu_töödelda > 0) {
                // Võtan esimese tipu.
                alg = töödeldavad_tipud.poll();
                // Kontrollin, et pole töödeldud antud elementi.
                if (alg.x == 1) {
                    // Märgin töödelduna, et seda rohkem ei läbiks.
                    alg.x = 2;
                    // Teen kaartele järjekorra.
                    Queue<Kaar> töödeldavad_kaared = new LinkedList<>();
                    // Panen tipu kaared järjekorda.
                    alg.getKaared(töödeldavad_kaared);
                    // Käin kaared läbi.
                    while (!töödeldavad_kaared.isEmpty()) {
                        Kaar töödeldav = töödeldavad_kaared.poll();
                        // Lisan kõik tipud, mis pole järjekorras, järjekorda
                        if (töödeldav.lõpp.x == 0) {
                            // Panen järjekorda.
                            töödeldavad_tipud.offer(töödeldav.lõpp);
                            // Märgin, et järjekorras.
                            töödeldav.lõpp.x = 1;
                        }
                    }
                }
                // Vähendan töödeldavate tippude arvu.
                mitu_töödelda--;
            }
            // Vähendan tankimiste arvu.
            k--;
        }
        // Kõik töötlemata tipud, mis järele jäid, ongi, mis tuleb tagastada.
        String[] tagastus = new String[töödeldavad_tipud.size()];
        int i = 0;
        while (!töödeldavad_tipud.isEmpty()) {
            tagastus[i++] = töödeldavad_tipud.poll().info;
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
        String[] tagastus = jõuame("A", x, k, linnad, M);
        for (String s : tagastus) {
            System.out.println(s);
        }
    }

}//Kodu7a

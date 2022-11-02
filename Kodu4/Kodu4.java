/*****************************************************************************
 * Algoritmid ja andmestruktuurid. LTAT.03.005
 * 2022/2023 sügissemester
 *
 * Kodutöö. Ülesanne nr 4
 * Teema: Isikukoodide sortimine.
 *
 * Autor: Mihkel Hani
 *
 * Mõningane eeskuju: vt https://www.geeksforgeeks.org/counting-sort/
 *
 *****************************************************************************/

import java.util.Calendar;

public class Kodu4{

    /** Genereerib isikukoodi lähtudes reeglitest püstitatud <a href=https://et.wikipedia.org/wiki/Isikukood>siin.</a>
     * <br>
     * Numbrite tähendused:
     * <ul style="list-style-type:none">
     *      <li> 1 - sugu ja sünniaasta esimesed kaks numbrit, (1...8) </li>
     *      <li> 2-3 - sünniaasta 3. ja 4. numbrid, (00...99) </li>
     *      <li> 4-5 - sünnikuu, (01...12) </li>
     *      <li> 6-7 - sünnikuupäev (01...31) </li>
     *      <li> 8-10 - järjekorranumber samal päeval sündinute eristamiseks (000...999) </li>
     *      <li> 11 - kontrollnumber (0...9) </li> </ul>
     * @return Eesti isikukoodi reeglitele vastav isikukood
     */
    static long genereeriIsikukood() {
        java.util.concurrent.ThreadLocalRandom juhus = java.util.concurrent.ThreadLocalRandom.current();
        Calendar kalender = new java.util.GregorianCalendar();
        kalender.setTime(new java.util.Date(juhus.nextLong(-5364669600000L, 7258024800000L)));
        long kood = ((kalender.get(Calendar.YEAR) - 1700) / 100 * 2 - juhus.nextInt(2)) * (long) Math.pow(10, 9) +
                kalender.get(Calendar.YEAR) % 100 * (long) Math.pow(10, 7) +
                (kalender.get(Calendar.MONTH) + 1) * (long) Math.pow(10, 5) +
                kalender.get(Calendar.DAY_OF_MONTH) * (long) Math.pow(10, 3) +
                juhus.nextLong(1000);
        int korrutisteSumma = 0;
        int[] IAstmeKaalud = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
        for (int i = 0; i < 10; i++) korrutisteSumma += kood / (long) Math.pow(10, i) % 10 * IAstmeKaalud[9 - i];
        int kontroll = korrutisteSumma % 11;
        if (kontroll == 10) {
            korrutisteSumma = 0;
            int[] IIAstmeKaalud = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};
            for (int i = 0; i < 10; i++) korrutisteSumma += kood / (long) Math.pow(10, i) % 10 * IIAstmeKaalud[9 - i];
            kontroll = korrutisteSumma % 11;
            kontroll = kontroll < 10 ? kontroll : 0;
        }
        return kood * 10 + kontroll;
    }

    /** Sorteerib isikukoodid sünniaja järgi:
     * <ul style="list-style-type:none">
     *     <li>a) järjestuse aluseks on sünniaeg, vanemad inimesed on eespool;</li>
     *     <li>b) kui sünniajad on võrdsed, määrab järjestuse isikukoodi järjekorranumber (kohad 8-10);</li>
     *     <li>c) kui ka järjekorranumber on võrdne, siis määrab järjestuse esimene number.</li>
     * </ul>
     * @param isikukoodid sorteeritav isikukoodide massiiv
     */
    public static void sort(long[] isikukoodid){
        int max1 = 9998; // from == 3
        int min1 = 1;
        int range1 = max1 - min1 + 1;
        int max2 = 1231; // Meetod grupeerib sajandid/sood 1-2 -> 0, 3-4 -> 1, 5-6 -> 2, 7-8 -> 3
        int min2 = 101;
        int range2 = max2 - min2 + 1;
        int max3 = 399;
        int min3 = 0;
        int range3 = max3 - min3 + 1;
        int[] count1 = new int[range1];
        int[] count2 = new int[range2];
        int[] count3 = new int[range3];
        long[] output = new long[isikukoodid.length];
        long[] output2 = new long[isikukoodid.length];
        // (int)(arr.get(i) % powersOfTen[from] / powersOfTen[to]) leiab arvu antud indeksil.
        for (long l : isikukoodid) {
            int esimeneDigit =  (int)(l/10_000_000_000L);
            int grupp = (esimeneDigit + (esimeneDigit << 31 >>> 31) >> 1) -1;
            count1[(int) (l % 10_000L / 10L)*10 + esimeneDigit - min1]++;
            count2[(int)(l % 100_000_000L / 10_000L)-min2]++;
            count3[100*grupp+(int)(l%10_000_000_000L / 100_000_000L)]++;
        }

        for (int i = 1; i < count1.length; i++) {
            count1[i] += count1[i - 1];
        }
        for (int i = 1; i < count3.length; i++) {
            count3[i] += count3[i - 1];
        }
        // 8 99 12 31 999 9
        int päevaIndeks = 2;
        int kuuIndeks = 1;
        int indeks = 1;
        int eelmineIndeks = 0;
        while(indeks < count2.length) {
            count2[indeks] += count2[eelmineIndeks];
            eelmineIndeks = indeks;
            päevaIndeks++;
            if(päevaIndeks == 32) {
                päevaIndeks = 1;
                kuuIndeks++;
            }
            indeks = 100*kuuIndeks+päevaIndeks-min2;
        }
        // These three loops should be merged as well, gotta look into it, so it doesn't mess up the initial order.
        for (int i = isikukoodid.length - 1; i >= 0; i--) {
            output[--count1[(int)(isikukoodid[i] % 10_000L / 10L)*10 + (int)(isikukoodid[i]/10_000_000_000L) - min1]] = isikukoodid[i];
        }
        for (int i = output.length - 1; i >= 0; i--) {
            output2[--count2[(int)(output[i] % 100_000_000L / 10_000L)-min2]] = output[i];
        }
        for (int i = output2.length - 1; i >= 0; i--) {
            int esimeneDigit =  (int)(output2[i]/10_000_000_000L);
            int grupp = (esimeneDigit + (esimeneDigit << 31 >>> 31) >> 1) -1;
            isikukoodid[--count3[100*grupp+(int)(output2[i] % 10_000_000_000L / 100_000_000L)]] = output2[i];
        }
    }

    public static void main(String[] args) {
        System.out.println(5 << 6);
        System.out.println(80001010000L >> 33);
        System.out.println(Integer.toBinaryString(1 << 31 >> 30));
        System.out.println(Integer.toBinaryString( (3 + (3 << 31 >>> 31) >> 1) & (1 << 31 >> 30) ));
        System.out.println("---kiirus---");
        long[] isikukoodid = new long[10000000];
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < isikukoodid.length; i++) {
                isikukoodid[i] = genereeriIsikukood();
            }
            long a = System.currentTimeMillis();
            sort(isikukoodid);
            long b = System.currentTimeMillis();
            System.out.println(b-a + "ms");
        }
        System.out.println("--kontroll--");
        isikukoodid = new long[10];
        for (int i = 0; i < isikukoodid.length; i++) {
            isikukoodid[i] = genereeriIsikukood();
            System.out.print(isikukoodid[i]+" ");
        }
        System.out.println("\n---sorted---");
        sort(isikukoodid);
        for (long l : isikukoodid) {
            System.out.print(l+" ");
        }
    }
}

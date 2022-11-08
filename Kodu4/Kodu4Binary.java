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

public class Kodu4Binary{

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
        // Biggest formatted value is 3 99 12 31 999 8 9 - century,year,month,day,orderNr,sex/century,lastNr
        int[] count1 = new int[8192];
        int[] count2 = new int[8192];
        int[] count3 = new int[8192];
        int length = isikukoodid.length;
        long[] output = new long[length];
        long[] output2 = new long[length];
        for (int i = 0; i < length; i++) {
            isikukoodid[i] = formatIsikukood(isikukoodid[i]);
            int firstPart = (int)(isikukoodid[i] & 8191L);
            int uus = (int)(isikukoodid[i] >>> 13);
            int secondPart = uus & 8191;
            uus = uus >>> 13;
            int thirdPart = uus & 8191;
            count1[firstPart]++;
            count2[secondPart]++;
            count3[thirdPart]++;
        }
        for (int i = 1; i < count1.length; i++) {
            count1[i] += count1[i-1];
            count2[i] += count2[i-1];
            count3[i] += count3[i-1];
        }
        length--;
        for (int i = length; i >= 0; i--) {
            int firstPart = (int)(isikukoodid[i]) & 8191;
            output[--count1[firstPart]] = isikukoodid[i];
        }
        for (int i = length; i >= 0; i--) {
            int secondPart = (int)(output[i] >>> 13) & 8191;
            output2[--count2[secondPart]] = output[i];
        }
        for (int i = length; i >= 0; i--) {
            int thirdPart = (int)(output2[i] >>> 26) & 8191;
            isikukoodid[--count3[thirdPart]] = revertIsikukood(output2[i]);
        }
    }

    public static long firstPart(long isikukood) {
        return isikukood & 8191L;
    }
    public static long secondPart(long isikukood) {
        return (isikukood >>> 13) & 8191L;
    }
    public static long thirdPart(long isikukood) {
        return (isikukood >>> 26) & 8191L;
    }

    public static long formatIsikukood(long isikukood) {
        long lastDigit = isikukood%10L;
        long esimeneDigit = isikukood/10_000_000_000L;
        long grupp = (esimeneDigit + (esimeneDigit << 63 >>> 63) >> 1) - 1;
        return grupp*100_000_000_000L + ((isikukood%10_000_000_000L)/10L)*100L + esimeneDigit*10L + lastDigit;
    }
    public static long revertIsikukood(long isikukood) {
        long lastDigit = isikukood%10L;
        isikukood /= 10L;
        long esimeneDigit = isikukood%10L;
        return ((isikukood%10_000_000_000L)/10L)*10L + lastDigit + esimeneDigit*10_000_000_000L;
    }

    public static void main(String[] args) {
        System.out.println(Long.toBinaryString(399123199989L));
        System.out.println(Long.toBinaryString(399123199989L).length());
        System.out.println(5 << 6);
        System.out.println(80001010000L >> 33);
        System.out.println(Integer.toBinaryString(1 << 31 >> 30));
        System.out.println(Integer.toBinaryString( (3 + (3 << 31 >>> 31) >> 1) & (1 << 31 >> 30) ));
        System.out.println("---kiirus---");
        long[] isikukoodid = new long[10000000];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < isikukoodid.length; i++) {
                isikukoodid[i] = genereeriIsikukood();
            }
            long a = System.currentTimeMillis();
            sort(isikukoodid);
            long b = System.currentTimeMillis();
            System.out.println(b-a + "ms");
        }
        System.out.println("--kontroll--");
        isikukoodid = new long[] {60210316512L, 60210216512L, 50210316512L, 50210216512L};
        for (int i = 0; i < isikukoodid.length; i++) {
            //isikukoodid[i] = genereeriIsikukood();
            System.out.print(isikukoodid[i]+" ");
        }
        System.out.println("\n---sorted---");
        sort(isikukoodid);
        for (long l : isikukoodid) {
            System.out.print(l+" ");
        }
    }
}

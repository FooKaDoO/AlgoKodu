import java.util.Queue;

class Kaar{
    //graafi ühte kaart esitav klass

    int kaal=0; //kaare kaal
    Tipp alg=null; //kaare lähtetipp
    Tipp lõpp=null; //kaare suubumistipp
    Kaar jrg=null; //kaarte loetelus viit järgmisele kaarele

    public Kaar(Tipp alg, Tipp lõpp, int kaal){
        this.alg=alg;
        this.lõpp=lõpp;
        this.kaal=kaal;
    }//konstruktor

    /**
     * Rekursiivne meetod, mis lisab meie kaarte ahelasse uue kaare.
     * @param lisatav Antud uus kaar.
     */
    public void lisaKaar(Kaar lisatav) {
        if (this.jrg == null)
            this.jrg = lisatav;
        else
            this.jrg.lisaKaar(lisatav);
    }

    /**
     * Rekursiivne meetod, mis itereerides läbi kaarte ahela, lisab need antud järjekorda.
     * @param kaared Antud järjekord
     */
    public void getKaared(Queue<Kaar> kaared) {
        kaared.offer(this);
        if (this.jrg != null)
            this.jrg.getKaared(kaared);
    }

}//Kaar
import java.util.Queue;

class Tipp{
    //graafi tippu esitav klass
    String info=null; //tipu info
    int x=0; //abiväli
    int kaal=0; //tipu kaal (kui peaks vaja olema)

    //graafi tippude esitus on tippude ahel
    Tipp jrg; //tippude loetelus viit järgmisele tipule
    Kaar kaared=null; //sellest tipust väljuvate kaarte loetelu

    public Tipp(String info){
        this.info=info;
    }

    /**
     * Rekursiivne meetod, mis lisab meie kaarte ahelasse uue kaare.
     * @param lisatav Antud uus kaar.
     */
    public void lisaKaar(Kaar lisatav) {
        if (this.kaared == null)
            this.kaared = lisatav;
        else
            this.kaared.lisaKaar(lisatav);
    }

    /**
     * Rekursiivne meetod, mis itereerides läbi kaarte ahela, lisab need antud järjekorda.
     * @param kaared Antud järjekord
     */
    public void getKaared(Queue<Kaar> kaared) {
        if (this.kaared != null)
            this.kaared.getKaared(kaared);
    }

}//Tipp

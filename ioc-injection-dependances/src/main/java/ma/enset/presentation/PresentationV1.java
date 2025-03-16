package ma.enset.presentation;

import ma.enset.dao.DaoImpl;
import ma.enset.ext.DaoImplV2;
import ma.enset.metier.MetierImpl;

public class PresentationV1 {
    public static void main(String[] args) {

        DaoImplV2 d = new DaoImplV2();
        MetierImpl metier = new MetierImpl(d);
        //metier.setDao(d);
        System.out.println(metier.calcul());

    }
}
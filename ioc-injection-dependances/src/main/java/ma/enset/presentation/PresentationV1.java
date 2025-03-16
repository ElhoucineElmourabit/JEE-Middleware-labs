package ma.enset.presentation;

import ma.enset.dao.DaoImpl;
import ma.enset.metier.MetierImpl;

public class PresentationV1 {
    public static void main(String[] args) {

        DaoImpl d = new DaoImpl();
        MetierImpl metier = new MetierImpl(d);
        System.out.println(metier.calcul());

    }
}
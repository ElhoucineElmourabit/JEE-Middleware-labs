package ma.enset.presentation;


import ma.enset.dao.IDao;
import ma.enset.metier.IMetier;

import java.io.File;
import java.util.Scanner;

public class PresentationV2 {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("config.txt"));
            String daoClassname = scanner.nextLine();
            Class cDao = Class.forName(daoClassname);
            IDao dao = (IDao) cDao.getConstructor().newInstance();
            System.out.println(dao.getData());

            String metierClassname = scanner.nextLine();
            Class cMetier = Class.forName(metierClassname);
            IMetier metier = (IMetier) cMetier.getConstructor(IDao.class).newInstance(dao);
            System.out.println(metier.calcul());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

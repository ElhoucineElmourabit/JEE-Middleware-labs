package ma.enset.presentation;

import ma.enset.metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresentationAvecAnnotations {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ext", "metier", "dao");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Res = "+metier.calcul());
    }
}

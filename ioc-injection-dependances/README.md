# Injection des Dépendances et Inversion de Contrôle

## 1. Injection des Dépendances par Instanciation Statique (Setter)  
**Description** :  
Instanciation directe avec injection via un setter.  

**Exemple** :  
```java
IDao dao = new DaoImpl();
IMetier metier = new MetierImpl();
metier.setDao(dao);
```

**Cas d’usage** :  
- Couplage fort.  
- Risque de `NullPointerException` si le setter est oublié.  


## 2. Injection des Dépendances par Instanciation Statique (Constructeur)  
**Description** :  
Instanciation directe des dépendances via le constructeur, sans flexibilité.  

**Exemple** :  
```java
IDao dao = new DaoImpl();
IMetier metier = new MetierImpl(dao);
```

**Cas d’usage** :  
- Couplage fort.  
- Difficile à modifier sans recompilation.  


## 3. Injection des Dépendances par Instanciation Dynamique (Constructeur)  
**Description** :  
Instanciation dynamique avec passage des dépendances via le constructeur.  

**Exemple** :  
```java
Scanner scanner = new Scanner(new File("config.txt"));
String daoClassname = scanner.nextLine();
Class cDao = Class.forName(daoClassname);
IDao dao = (IDao) cDao.getConstructor().newInstance();
            

String metierClassname = scanner.nextLine();
Class cMetier = Class.forName(metierClassname);
IMetier metier = (IMetier) cMetier.getConstructor(IDao.class).newInstance(dao);
            
```

**Cas d’usage** :  
- Modification des dépendances via une configuration externe sans recompilation.
- Évite le couplage fort, mais nécessite du code technique.


## 4. Injection des Dépendances par Instanciation Dynamique (Setter)  
**Description** :  
Instanciation dynamique des classes via réflexion et injection via un setter.  

**Exemple** :  
```java
Scanner scanner = new Scanner(new File("config.txt"));
String daoClassname = scanner.nextLine();
Class cDao = Class.forName(daoClassname);
IDao dao = (IDao) cDao.getConstructor().newInstance();

String metierClassname = scanner.nextLine();
Class cMetier = Class.forName(metierClassname);
IMetier metier = (IMetier) cMetier.getConstructor().newInstance();

Method setDao = cMetier.getDeclaredMethod("setDao", IDao.class);
setDao.invoke(metier, dao);
```

**Cas d’usage** :  
- Modification des dépendances via une configuration externe sans recompilation.
- Évite le couplage fort, mais nécessite du code technique.  


## 5. Injection des Dépendances avec Spring - Version XML  
**Description** :  
Utilisation d’un fichier XML (`config.xml`) pour définir les beans et leurs dépendances.  

**Configuration XML** :  
```xml
<beans>
    <bean id="dao" class="dao.DaoImpl"/>
    <bean id="metier" class="metier.MetierImpl">
        <property name="dao" ref="dao"/> <!-- Injection via setter -->
        <!-- Ou -->
        <constructor-arg ref="dao"/> <!-- Injection via constructeur -->
    </bean>
</beans>
```

**Exemple** :  
```java
ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
IMetier metier = context.getBean(IMetier.class);
System.out.println(metier.calcul());
```

**Cas d’usage** :  
- Centralisation de la configuration.  
- Flexibilité pour modifier les implémentations sans recompiler le code.  


## 6. Injection des Dépendances avec Spring - Version Annotations  
**Description** :  
Spring permet d'utiliser des annotations pour déclarer et injecter automatiquement les dépendances, éliminant le code technique.  

**Annotations clés** :  
- `@Component` : Marque une classe comme un bean géré par Spring.  
- `@Repository` : Spécifique à la couche DAO.  
- `@Service` : Spécifique à la couche métier.  
- `@Autowired` : Injecte automatiquement une dépendance compatible (par constructeur, setter ou champ).  
- `@Qualifier` : Résout les ambiguïtés si plusieurs beans implémentent la même interface.  

**Exemple** :  
```java
@Repository("dao")
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        return 23;
    }
}

@Service("metier")
public class MetierImpl implements IMetier {
    private final IDao dao;

    @Autowired
    public MetierImpl(@Qualifier("dao") IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        return dao.getData() * 23;
    }
}
```

**Cas d’usage** :  
- Plus besoin d’écrire du code technique pour gérer les dépendances, Spring s’en charge automatiquement.
- Permet de découpler les composants (DAO, services, contrôleurs) et facilite leur réutilisation.


## Comparaison des Méthodes  
| Méthode               | Flexibilité | Couplage | Complexité | Maintenance |  
|-----------------------|-------------|----------|------------|-------------|  
| Spring (Annotations)  | Élevée      | Faible   | Faible     | Facile      |  
| Spring (XML)          | Élevée      | Faible   | Modérée    | Modérée     |  
| Dynamique (Setter)    | Modérée     | Faible   | Élevée     | Difficile   |  
| Dynamique (Constructeur) | Modérée  | Faible   | Élevée     | Difficile   |  
| Statique (Constructeur) | Faible    | Fort     | Faible     | Difficile   |  
| Statique (Setter)     | Faible      | Fort     | Faible     | Difficile   |   

**Conclusion** :  
Spring (avec annotations ) est la méthode recommandée pour sa flexibilité et sa maintenabilité.
package ma.enset.ext;

import ma.enset.dao.IDao;

public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version web services");
        double t = 11;
        return t;
    }
}
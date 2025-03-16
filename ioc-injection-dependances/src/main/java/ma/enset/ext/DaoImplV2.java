package ma.enset.ext;

import ma.enset.dao.IDao;
import org.springframework.stereotype.Repository;

@Repository("dao2")
public class DaoImplV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version web services");
        double t = 11;
        return t;
    }
}
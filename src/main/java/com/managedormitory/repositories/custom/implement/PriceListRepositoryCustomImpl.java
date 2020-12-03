package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dto.powerbill.PowerBillDto;
import com.managedormitory.models.dto.price.PriceListDto;
import com.managedormitory.repositories.custom.PriceListRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.FloatType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class PriceListRepositoryCustomImpl implements PriceListRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<PriceListDto> getAllPriceList() {
        String queryGet = "SELECT pl.id AS id, pl.name AS name, pl.price AS price FROM price_list pl ORDER  BY pl.id ASC";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryGet);
        query.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("price", StandardBasicTypes.FLOAT);
        query.setResultTransformer(new AliasToBeanResultTransformer(PriceListDto.class));
        return safeList(query);
    }

    @Override
    public int addPriceList(PriceListDto priceListDto) {
        String queryAdd = "INSERT INTO price_list(name, price) VALUES(:name, :price)";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryAdd);
        query.setParameter("name", new TypedParameterValue(StringType.INSTANCE, priceListDto.getName()))
                .setParameter("price", new TypedParameterValue(FloatType.INSTANCE, priceListDto.getPrice()));

        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}

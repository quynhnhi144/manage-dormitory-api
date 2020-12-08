package com.managedormitory.repositories.custom.implement;

import com.managedormitory.models.dao.Mail;
import com.managedormitory.repositories.custom.MailRepositoryCustom;
import org.hibernate.Session;
import org.hibernate.jpa.TypedParameterValue;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class MailRepositoryCustomImpl implements MailRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    @Override
    public int addMail(Mail mail) {
        String queryInsert = "INSERT INTO mail (subject, content) VALUES(:subject, :content)";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryInsert);
        query.setParameter("subject", new TypedParameterValue(StringType.INSTANCE, mail.getSubject()))
                .setParameter("content", new TypedParameterValue(StringType.INSTANCE, mail.getContent()));
        return query.executeUpdate();
    }

    @Override
    public int updateMail(Integer id, Mail mail) {
        String queryUpdate = "UPDATE mail SET subject = :subject, content = :content WHERE id = :id";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryUpdate);
        query.setParameter("subject", new TypedParameterValue(StringType.INSTANCE, mail.getSubject()))
                .setParameter("content", new TypedParameterValue(StringType.INSTANCE, mail.getContent()))
                .setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id));
        return query.executeUpdate();
    }

    @Override
    public int deleteMail(Integer id) {
        String queryDelete = "DELETE FROM mail WHERE id = :id";
        NativeQuery<Query> query = getCurrentSession().createNativeQuery(queryDelete);
        query.setParameter("id", new TypedParameterValue(IntegerType.INSTANCE, id));
        return query.executeUpdate();
    }

    public static <Entity> List<Entity> safeList(Query query) {
        return query.getResultList();
    }

    protected Session getCurrentSession() {
        return (Session) entityManager.unwrap(Session.class);
    }
}

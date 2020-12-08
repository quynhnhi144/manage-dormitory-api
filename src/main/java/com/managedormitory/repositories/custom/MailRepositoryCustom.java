package com.managedormitory.repositories.custom;

import com.managedormitory.models.dao.Mail;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepositoryCustom {

    int addMail(Mail mail);

    int updateMail(Integer id, Mail mail);

    int deleteMail(Integer id);
}

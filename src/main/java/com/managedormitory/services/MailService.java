package com.managedormitory.services;

import com.managedormitory.models.dao.Mail;
import com.managedormitory.models.dto.pagination.PaginationMail;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;

public interface MailService {
    List<Mail> getAllMails();

    PaginationMail filterAllMails(String subject, int skip, int take);

    Optional<Mail> getMailById(Integer mailId);

    Mail updateMail(Integer mailId, Mail mail);

    Mail addMail(Mail mail);

    void deleteMail(Integer mailId);

    void sendMail(Mail mail, StudentDetailDto studentDetailDto, JavaMailSender javaMailSender, MimeMessage message, MimeMessageHelper mimeMessageHelper);
}

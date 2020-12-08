package com.managedormitory.services.impl;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.Mail;
import com.managedormitory.models.dto.pagination.PaginationMail;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.repositories.MailRepository;
import com.managedormitory.repositories.custom.MailRepositoryCustom;
import com.managedormitory.services.MailService;
import com.managedormitory.utils.PaginationUtils;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private MailRepositoryCustom mailRepositoryCustom;

    @Override
    public List<Mail> getAllMails() {
        return mailRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public PaginationMail filterAllMails(String subject, int skip, int take) {
        List<Mail> mailList = getAllMails();
        if (subject != null && !subject.equals("")) {
            String searchText = subject.toLowerCase() + StringUtil.DOT_STAR;
            mailList = mailList.stream()
                    .filter(mail -> mail.getSubject() != null && mail.getSubject().toLowerCase().matches(searchText))
                    .collect(Collectors.toList());
        }
        int total = mailList.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<Mail>> mailMap = new HashMap<>();
        mailMap.put("data", mailList.subList(skip, lastElement));
        return new PaginationMail(mailMap, total);
    }

    @Override
    public Optional<Mail> getMailById(Integer mailId) {
        return mailRepository.findById(mailId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mail updateMail(Integer mailId, Mail mail) {
        if(mailRepositoryCustom.updateMail(mailId, mail) > 0){
            return mail;
        }else{
            throw new BadRequestException("Khong the cap nhat mail nay!!!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mail addMail(Mail mail) {
        if(mailRepositoryCustom.addMail(mail) > 0){
            return mail;
        }else{
            throw new BadRequestException("Khong the them mail nay!!!");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMail(Integer mailId) {
        Mail mail = mailRepository.findById(mailId).get();
        mailRepository.delete(mail);
    }

    @Override
    public void sendMail(Mail mail, StudentDetailDto studentDetailDto, JavaMailSender javaMailSender, MimeMessage message, MimeMessageHelper helper) {
        try {
            helper.setTo(studentDetailDto.getEmail());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent());
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

package org.feelfee.core.service;

import org.feelfee.core.model.ClientResponse;
import org.feelfee.core.repository.AdvRepository;
import org.feelfee.core.repository.ClientResponseRepository;
import org.feelfee.core.repository.UserProfileRepository;
import org.feelfee.core.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientResponseService {

    private final ClientResponseRepository clientResponseRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdvRepository advRepository;
    private final MailService mailService;


    @Autowired
    public ClientResponseService(ClientResponseRepository clientResponseRepository, UserProfileRepository userProfileRepository, AdvRepository advRepository, MailService mailService) {
        this.clientResponseRepository = clientResponseRepository;
        this.userProfileRepository = userProfileRepository;
        this.advRepository = advRepository;
        this.mailService = mailService;
    }


    public void save(ClientResponse clientResponse) {
        try {
            clientResponseRepository.save(clientResponse);
            // todo запилить отправку email
            //получаем почту менеджера, опубликовавшего объявление и отправляем ему e-mail
            advRepository.findAdvByUrl(clientResponse.getAdvSourceLink()).ifPresent(adv -> {
                String email = adv.getManager().getEmail();
                System.out.println(email);
//                EmailSender emailSender = new EmailSender();

                String mailBody = prepareBody(clientResponse);
//                emailSender.send(email, mailBody);
                mailService.sendEmail(email, "LinkMe.club нотификация", mailBody);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String prepareBody(ClientResponse clientResponse) {

        String template = "Новый клиент!\n" +
                "Объявление: ${sourceLink}\n" +
                "Имя: ${clientName}\n" +
                "Телефон: ${clientPhone}\n" +
                "Риэлтор: ${isRealtor}\n";
        String sourceLink = clientResponse.getAdvSourceLink();
        String clientName = clientResponse.getClientName();
        String clientPhone = clientResponse.getClientPhone();
        Boolean isRealtor = clientResponse.getIsRealtor();
        return template
                .replace("${sourceLink}", sourceLink)
                .replace("${clientName}", clientName)
                .replace("${clientPhone}", clientPhone)
                .replace("${isRealtor}", isRealtor ? "Да" : "Нет");
    }


}

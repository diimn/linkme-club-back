package org.feelfee.core.service;

import org.feelfee.core.Globals;
import org.feelfee.core.exception.ResourceNotFoundException;
import org.feelfee.core.model.*;
import org.feelfee.core.model.service.AdvExtendedWithCounterDto;
import org.feelfee.core.model.service.AdvUpload;
import org.feelfee.core.model.service.MainPageRandomAdv;
import org.feelfee.core.repository.AdvContentRepository;
import org.feelfee.core.repository.AdvRepository;
import org.feelfee.core.repository.RepostRepository;
import org.feelfee.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvService {

    private final AdvContentRepository advContentRepository;
    private final AdvRepository advRepository;
    private final RepostRepository repostRepository;
    private final ManagerService managerService;
    private final ImageService imageService;
    private final Globals globals;
    private final CounterService counterService;

    @Autowired
    public AdvService(AdvContentRepository advContentRepository, AdvRepository advRepository, RepostRepository repostRepository, ManagerService managerService, ImageService imageService, Globals globals, CounterService counterService) {
        this.advContentRepository = advContentRepository;
        this.advRepository = advRepository;
        this.repostRepository = repostRepository;
        this.managerService = managerService;
        this.imageService = imageService;
        this.globals = globals;
        this.counterService = counterService;
    }


    public AdvContent getContentByUrl(String url) {
        if (advRepository.findAdvByUrl(url).isPresent()) {
            return advRepository.findAdvByUrl(url).get().getAdvContent();
        } else {
            throw new ResourceNotFoundException("AdvContent not  found");
        }
    }

    public AdvContent getContentByUniqUrl(String url) {
        if (repostRepository.findByUniqLink(url).isPresent()) {
            Repost repost = repostRepository.findByUniqLink(url).get();
            return repost.getAdv().getAdvContent();
        } else {
            throw new ResourceNotFoundException("AdvContent not  found");
        }
    }

    public String getUrlByUniqUrl(String url) {
        if (repostRepository.findByUniqLink(url).isPresent()) {
            Repost repost = repostRepository.findByUniqLink(url).get();
            return repost.getAdv().getUrl();
        } else {
            throw new ResourceNotFoundException("AdvContent not  found");
        }
    }

    public List<MainPageRandomAdv> getIds() {
        List<String> allIds = advRepository.getAllActiveIds();
        Set<String> result = getRandomValues(allIds);
        return result.stream()
                .map(advRepository::findAdvById)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MainPageRandomAdv toDTO(Optional<Adv> adv) {
        if (!adv.isPresent()) {
            return null;
        }
        AdvContent content = adv.get().getAdvContent();
        return MainPageRandomAdv.builder()
                .bonus(content.getBonus())
                .commentHeader(content.getDescHead())
                .commentPrice(content.getPrice_comment())
                .header(content.getHeadTop())
                .price(content.getPrice())
                .url(adv.get().getUrl())
                .build();
    }

    //todo параметризовать объем
    private Set<String> getRandomValues(List<String> source) {
        HashSet<String> result = new HashSet<>();
        Random randomGenerator = new Random();
        while (result.size() < 2) {
            result.add(source.get(randomGenerator.nextInt(source.size())));
        }
        return result;
    }


    public Page<Adv> findAllByRole1(Pageable pageable, String token) {

        String managerName = Utils.getUserFromJWT(token);
        Manager manager = managerService.findByLogin(managerName).get();
        if ("admin".equals(manager.getRole().getName())) {
            return advRepository.findAll(pageable);
        } else {
            return advRepository.findAllByManager_Id(manager.getId(), pageable);
        }
    }

    public Page<AdvExtendedWithCounterDto> findAllByRole(Pageable pageable, String token) {

        String managerName = Utils.getUserFromJWT(token);
        Manager manager = managerService.findByLogin(managerName).get();
        if ("admin".equals(manager.getRole().getName())) {
            return transformToExtendedAdv(advRepository.findAll(pageable));
        } else {
            return transformToExtendedAdv(advRepository.findAllByManager_Id(manager.getId(), pageable));
        }
    }

    private Page<AdvExtendedWithCounterDto> transformToExtendedAdv(Page<Adv> advPage) {
        return advPage.map(adv -> {
            AdvCounter counter = counterService.getAdvCounter(adv.getUrl());
            return new AdvExtendedWithCounterDto(
                    adv,
                    counter.getBuyCounter(),
                    counter.getLoginCounter(),
                    counter.getRepostCounter()
            );
        });
    }

//    public Adv updateAdvByIdOld(String id, AdvUpload advUpload, String token) {
//        String url = advUpload.getUrl();
//        if (url == null || url.isEmpty()) {
//            throw new RuntimeException("URL can't be null");
//        }
//        Adv result;
//
//        Adv adv = new Adv();
//        AdvContent advContent = advContentRepository.findById(
//                advRepository.findAdvById(id).get().getAdvContent().getId())
//                .orElse(new AdvContent());
//        fillContent(advContent, advUpload);
//        adv.setId(id);
//        result = saveContentAndFillAdv(advUpload, url, adv, advContent);
//        System.out.println(result);
//        uploadPhotos(advUpload);
//        return result;
//    }

    public Adv updateAdvById(String id, AdvUpload advUpload, String token) {
        String url = advUpload.getUrl();
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("URL can't be null");
        }
        Adv result;

        Adv adv = advRepository.findAdvById(id).get();
        AdvContent advContent = advContentRepository.findById(adv
                .getAdvContent().getId())
                .orElse(new AdvContent());
        fillContent(advContent, advUpload);
        result = saveContentAndFillAdv(advUpload, url, adv, advContent, null);
        System.out.println(result);
        uploadPhotos(advUpload);
        return result;
    }

    public Adv createAdv(AdvUpload advUpload, String token) {
        String url = advUpload.getUrl();
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("URL can't be null");
        }
        Adv result;
        if (!advRepository.existsAdvByUrl(url)) {
            Adv adv = new Adv();
            AdvContent advContent = fillContent(advUpload);
            String login = Utils.getUserFromJWT(token);
            result = saveContentAndFillAdv(advUpload, url, adv, advContent, login);
        } else {
            result = advRepository.findAdvByUrl(url).get();
        }
        System.out.println(result);
        uploadPhotos(advUpload);
        return result;
    }


    private AdvContent fillContent(AdvUpload advUpload) {
        AdvContent advContent = new AdvContent();
        fillContent(advContent, advUpload);
        return advContent;
    }

    private void fillContent(AdvContent advContent, AdvUpload advUpload) {
        advContent.setHeadTop(advUpload.getAdvContent().getHeadTop());
        advContent.setBonus(advUpload.getAdvContent().getBonus());
        advContent.setBrokerName(advUpload.getAdvContent().getBrokerName());
        advContent.setBrokerComment(advUpload.getAdvContent().getBrokerComment());
        advContent.setBullets(advUpload.getAdvContent().getBullets());
        advContent.setCoordinates(advUpload.getAdvContent().getCoordinates());
        advContent.setDescHead(advUpload.getAdvContent().getDescHead());
        advContent.setDescription(advUpload.getAdvContent().getDescription());
        advContent.setPrice(advUpload.getAdvContent().getPrice());
        advContent.setPrice_comment(advUpload.getAdvContent().getPrice_comment());
        advContent.setSlider1_comments(advUpload.getAdvContent().getSlider1_comments());
        advContent.setShareText(advUpload.getAdvContent().getShareText());
    }

    void uploadPhotos(AdvUpload advUpload) {
        String url = advUpload.getUrl();
        imageService.uploadPhotos(url, "headPhoto",
                Collections.singletonList(new AdvUpload.Slider(0, advUpload.getHeadPhoto())));
        imageService.uploadPhotos(url, "brokerPhoto",
                Collections.singletonList(new AdvUpload.Slider(0, advUpload.getBrokerPhoto())));
        imageService.uploadPhotos(url, "slider1", advUpload.getSlider1());
        imageService.uploadPhotos(url, "slider2", advUpload.getSlider2());
        imageService.uploadPhotos(url, "shareImage", Collections.singletonList(
                new AdvUpload.Slider(0, advUpload.getShareImage())));

    }


    private Adv saveContentAndFillAdv(AdvUpload advUpload, String url, Adv adv, AdvContent advContent, String login) {
        Adv result;
        advContentRepository.save(advContent);

        adv.setUrl(url);
        adv.setIsMainPageActive(advUpload.getIsMainPageActive());
        adv.setAdvContent(advContent);
        adv.setCreatingDate(new Date());

        if (adv.getManager() == null && login != null) {
            Manager manager = managerService.findByLogin(login).get();
            adv.setManager(manager);
        }

        result = advRepository.save(adv);
        return result;
    }
}

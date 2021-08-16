package org.feelfee.core.service;

import org.feelfee.core.filestore.FileStore;
import org.feelfee.core.model.Adv;
import org.feelfee.core.model.Repost;
import org.feelfee.core.repository.AdvRepository;
import org.feelfee.core.repository.RepostRepository;
import org.feelfee.core.repository.UserProfileRepository;
import org.feelfee.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RepostService {

    private final RepostRepository repostRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdvRepository advRepository;


    @Autowired
    public RepostService(RepostRepository repository, FileStore fileStore, UserProfileRepository userProfileRepository, AdvRepository advRepository) {
        this.repostRepository = repository;
        this.userProfileRepository = userProfileRepository;
        this.advRepository = advRepository;
    }

    public Repost findOrSaveByUserProfile_SocialIdAndAdv_Url(String socialId, String url, String socialType) {
        Optional<Repost> optionalRepost
                = repostRepository.findByUserProfile_SocialTypeAndUserProfile_SocialIdAndAdv_Url(socialType, socialId, url);
        return optionalRepost.orElseGet(() -> save(socialId, url, socialType));
    }


    public Repost save(String socialId, String url, String socialType) {
        String uniqLink = Utils.getUniqLink();
        Repost repost = new Repost(UUID.randomUUID().toString(), uniqLink,
                advRepository.findAdvByUrl(url).get(),
                userProfileRepository.findUserProfileBySocialIdAndSocialType(socialId, socialType).get(),
                0);
        return repostRepository.save(repost);

    }

    public Optional<Repost> findByUniqUrl(String url) {
        return repostRepository.findByUniqLink(url);
    }

    /*
     * todo сломалась логика при получении ссылки для другого пользователя
     * */
    public Repost findByUserProfile_SocialIdAndUniqLink(String socialType, String socialId, String uniqUrl) {
        //по уникальной ссылке получаем id объявления, если для пользователя есть репост - вернуть репост
        //если нет - создать
        String url = repostRepository.findByUniqLink(uniqUrl).get().getAdv().getUrl();
        Optional<Repost> optionalRepost
                = repostRepository
                .findByUserProfile_SocialTypeAndUserProfile_SocialIdAndAdv_Url(socialType, socialId, url);
        return optionalRepost.orElseGet(() -> {
            repostRepository.findByUniqLink(uniqUrl);
            return save(socialId, url, socialType);
        });
    }

    public Optional<Repost> incrementRepost(String url) {
        System.out.println("URL: " + url);
        Optional<Repost> repost = repostRepository.findByUniqLink(url);
        System.out.println("Repost: " + repost.get());
        repost.ifPresent(repost1 -> {
            if (repost1.getRepostCount() != null) {
                repost1.setRepostCount(repost1.getRepostCount() + 1);
            } else {
                repost1.setRepostCount(1);
            }
            repostRepository.save(repost1);
        });
        return repost;
    }
}

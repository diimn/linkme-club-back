package org.feelfee.core.repository;


import org.feelfee.core.model.Repost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepostRepository extends CrudRepository<Repost, String> {

    //     Optional<Adv> findAdvByUrl(String url);
//     Boolean existsAdvByUrl(String url);
//     Optional<Repost> findByUserProfile_SocialIdAndUniqLink(St)
//    Optional<Repost> findByUserProfile_SocialIdAndAdv_Url(String userProfile_SocialId, String adv_Url);

    Optional<Repost> findByUserProfile_SocialTypeAndUserProfile_SocialIdAndAdv_Url(String userProfile_SocialType,
                                                          String userProfile_SocialId, String adv_Url);

//    Optional<Repost> findByUserProfile_SocialIdAndUniqLink(String userProfile_SocialId, String adv_Url);

    Optional<Repost> findByUserProfile_SocialTypeAndUserProfile_SocialIdAndUniqLink(String userProfile_SocialType,
                                                           String userProfile_SocialId, String adv_Url);

    Optional<Repost> findByUniqLink(String uniqLink);


}

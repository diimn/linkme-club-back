package org.feelfee.core.repository;

import org.feelfee.core.model.ClientResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientResponseRepository extends CrudRepository<ClientResponse, String> {

//     Optional<Adv> findAdvByUrl(String url);
//     Boolean existsAdvByUrl(String url);
//     Optional<Repost> findByUserProfile_SocialIdAndUniqLink(St)
//     Optional<Repost> findByUserProfile_SocialIdAndAdv_Url(String userProfile_SocialId, String adv_Url);
//     Optional<Repost> findByUserProfile_SocialIdAndUniqLink(String userProfile_SocialId, String adv_Url);
//     Optional<Repost> findByUniqLink(String uniqLink);




}

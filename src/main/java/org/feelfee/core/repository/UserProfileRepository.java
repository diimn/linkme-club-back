package org.feelfee.core.repository;

import org.feelfee.core.model.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, String> {

    Optional<UserProfile> findUserProfileBySocialIdAndSocialType(String socialId, String socialType);
//    Boolean existsAdvByUrl(String url);

}

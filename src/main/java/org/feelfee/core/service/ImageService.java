package org.feelfee.core.service;

import org.feelfee.core.Globals;
//import org.feelfee.core.bucket.BucketName;
import org.feelfee.core.filestore.FileStore;
import org.feelfee.core.model.UserProfile;
import org.feelfee.core.model.service.AdvUpload;
import org.feelfee.core.model.service.SliderWrapper;
import org.feelfee.core.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.*;

@Service
public class ImageService {

    private final UserProfileRepository repository;

    private final FileStore fileStore;

    private final Globals globals;

    @Autowired
    public ImageService(UserProfileRepository repository, FileStore fileStore, Globals globals) {
        this.repository = repository;
        this.fileStore = fileStore;
        this.globals = globals;
    }


    public List<UserProfile> getUserProfiles() {
        return (List<UserProfile>) repository.findAll();
    }

    public Optional<UserProfile> getUserProfileBySocialIdAndSocialType(String userSocialId, String socialType) {
        return repository.findUserProfileBySocialIdAndSocialType(userSocialId, socialType);
    }

    public Map<String, UserProfile> getUserProfileMap() {
        return getUserProfiles().stream()
                .collect(Collectors.toMap(UserProfile::getId, userProfile -> userProfile));
    }

    /*
     *
     * url - уникальный url
     * type - slider1, slider2, headPhoto
     *
     */
    public void uploadFile(String url, String type,
                           String fileName, InputStream inputStream) {
        String path = String.format("%s/%s/%s", globals.getBucketName(), url, type);
//        String fileNameForSave = String.format("%s-%s", fileName, new Date(System.currentTimeMillis()));

        fileStore.save(path, fileName, Optional.empty(), inputStream);
    }


    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return getUserProfiles().stream()
                .filter(userProfile -> userProfile.getId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User profile %s not found", userProfileId)));
    }


    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUserProfileOrThrow(userProfileId);
        String path = String.format("%s/%s",
                globals.getBucketName(),
                user.getId());
        if (user.getUserProfileImageLink() != null && !user.getUserProfileImageLink().isEmpty()) {
            return fileStore.download(path, user.getUserProfileImageLink());
        } else {
            return new byte[0];
        }
    }

    public int getNumberSliderPhotos(String url, String type) {
        return fileStore.getNumberSliderPhotos(url, type);
    }

    public SliderWrapper getNumberSliderPhotos(String url) {
        return fileStore.getNumberSliderPhotos(url);
    }

    public byte[] getHeaderPhoto(String url) {
        return fileStore.getHeadPhoto(url);
    }

    public String getHeaderPhotoUrl(String url) {
        return fileStore.getHeadPhotoUrl(url);
    }

    public String getSharePhotoUrl(String url) {
        return fileStore.getSharePhotoUrl(url);
    }

    public byte[] getSliderPhoto(String url, int index, String type) {
        return fileStore.getSliderPhoto(url, index, type);
    }

    public byte[] getBrokerPhoto(String url) {
        return fileStore.getBrokerPhoto(url);
    }

    public void uploadPhotos(String url, String type, List<AdvUpload.Slider> photoUploads) {
        if (photoUploads != null) {
            photoUploads.forEach(photoUpload -> {
                if (photoUpload.getContent() != null) {
                    int index = photoUpload.getIndex();
                    String content = photoUpload.getContent().split(",")[1];
                    byte[] img = Base64.getDecoder().decode(content);
                    try {
                        try (InputStream in = new ByteArrayInputStream(img)) {
                            uploadFile(url, type, index + ".jpg", in);
                        }
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            });
        }
    }
}

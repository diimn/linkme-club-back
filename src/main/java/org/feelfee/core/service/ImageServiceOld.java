package org.feelfee.core.service;

import org.feelfee.core.Globals;
import org.feelfee.core.filestore.FileStore;
import org.feelfee.core.model.UserProfile;
import org.feelfee.core.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.*;

//import org.feelfee.core.bucket.BucketName;

@Service
public class ImageServiceOld {

    private final UserProfileRepository repository;

    private final FileStore fileStore;

    private final Globals globals;

    @Autowired
    public ImageServiceOld(UserProfileRepository repository, FileStore fileStore, Globals globals) {
        this.repository = repository;
        this.fileStore = fileStore;
        this.globals = globals;
    }

    public UserProfile createOrUpdateUserProfile(UserProfile userProfile) {
        return repository.save(userProfile);
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

    public void uploadFile(String directory, String adv_id, MultipartFile file) {
        isFileNotEmpty(file);
        isImage(file);

        Map<String, String> metaData = extractMetaData(file);
        String path = String.format("%s/%s/%s", globals.getBucketName(), adv_id, directory);
        String fileName = String.format("%s-%s", file.getOriginalFilename(), new Date(System.currentTimeMillis()));
        try {
            fileStore.save(path, fileName, Optional.of(metaData), file.getInputStream());
            //сохраняем ?
//            user.setUserProfileImageLink(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void uploadFile(String url, String type,
                           String fileName, InputStream inputStream) {
//        isFileNotEmpty(file);
//        isImage(file);

//        Map<String, String> metaData = extractMetaData(file);
        String path = String.format("%s/%s/%s", globals.getBucketName(), url, type);
        String fileNameForSave = String.format("%s-%s", fileName, new Date(System.currentTimeMillis()));

        fileStore.save(path, fileNameForSave, Optional.empty(), inputStream);
        //сохраняем ?
//            user.setUserProfileImageLink(fileName);
    }

//    public void upload(final UUID userProfileId, MultipartFile file) {
//        isFileNotEmpty(file);
//        isImage(file);
//      /*  Map<UUID, UserProfile> profileMap = getUserProfileMap();
//        if (profileMap.containsKey(userProfileId)) {
//            System.out.println(file.getName() + ":" + file.getContentType());
//            s3Store.save();
//        }*/
//        UserProfile user = getUserProfileOrThrow(userProfileId);
//        Map<String, String> metaData = extractMetaData(file);
//        String path = String.format("%s/%s", globals.getBucketName(), user.getId());
//        String fileName = String.format("%s-%s", file.getOriginalFilename(), new Date(System.currentTimeMillis()));
//        try {
//            fileStore.save(path, fileName, Optional.of(metaData), file.getInputStream());
//            //сохраняем
//            user.setUserProfileImageLink(fileName);
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//    }

    private Map<String, String> extractMetaData(MultipartFile file) {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("Content-Type", file.getContentType());
        metaData.put("Content-Length", String.valueOf(file.getSize()));
        return metaData;
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return getUserProfiles().stream()
                .filter(userProfile -> userProfile.getId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        String.format("User profile %s not found", userProfileId)));
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_BMP.getMimeType(), IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("Cant upload not image " + file.getContentType());
        }
    }

    private void isFileNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalStateException("Can't upload empty file");
        }
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
}

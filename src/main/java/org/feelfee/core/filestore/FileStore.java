package org.feelfee.core.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.feelfee.core.Globals;
import org.feelfee.core.model.service.SliderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    private final Globals globals;

    @Autowired
    public FileStore(AmazonS3 s3, Globals globals) {
        this.s3 = s3;
        this.globals = globals;
    }

    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetaData,
                     InputStream inputStream) {
        ObjectMetadata objectMetaData = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetaData::addUserMetadata);
            }
        });
        try {
            s3.putObject(path, fileName, inputStream, objectMetaData);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to load data to s3", e);
        }
    }

    public byte[] download(String fullPath, String key) {
        try {
            S3Object obj = s3.getObject(fullPath, key);
            S3ObjectInputStream objInStream = obj.getObjectContent();
            return IOUtils.toByteArray(objInStream);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download data from s3", e);
        }
    }

    public void getImages(String path) {
        try {
            s3.listObjectsV2(globals.getBucketName(), "qwe");
//            s3.listObjectsV2()
//            S3Object obj = s3.getObject(fullPath, key);
//            S3ObjectInputStream objInStream = obj.getObjectContent();
//            return IOUtils.toByteArray(objInStream);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to download data from s3", e);
        }
    }

    public byte[] getHeadPhoto(String url) {
        return getPhoto("headPhoto", url);
    }

    public String getHeadPhotoUrl(String advUrl) {
        return getImageUrl("headPhoto", advUrl);
    }

    public String getSharePhotoUrl(String advUrl) {
        return getImageUrl("shareImage", advUrl);
    }

    public int getNumberSliderPhotos(String url, String type) {
        ListObjectsV2Result objList = s3.listObjectsV2(globals.getBucketName(), url + "/" + type);
        return objList.getObjectSummaries().size();
    }

    public SliderWrapper getNumberSliderPhotos(String url) {
        ListObjectsV2Result objList1 = s3.listObjectsV2(globals.getBucketName(), url + "/" + "slider1");
        ListObjectsV2Result objList2 = s3.listObjectsV2(globals.getBucketName(), url + "/" + "slider2");
        SliderWrapper sliderWrapper
                = new SliderWrapper(objList1.getObjectSummaries().size(), objList2.getObjectSummaries().size());
        return sliderWrapper;
    }

    public byte[] getSliderPhoto(String url, int index, String type) {
        try {
            ListObjectsV2Result objList = s3.listObjectsV2(globals.getBucketName(), url + "/" + type);
            if (objList != null && !objList.getObjectSummaries().isEmpty()) {
                S3ObjectSummary objSum = objList.getObjectSummaries().get(index);

                S3Object obj = s3.getObject(globals.getBucketName(), objSum.getKey());
                S3ObjectInputStream objInStream = obj.getObjectContent();
                return IOUtils.toByteArray(objInStream);
            }
            return null;
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download data from s3", e);
        }
    }

    public byte[] getBrokerPhoto(String url) {
        return getPhoto("brokerPhoto", url);
    }

    private byte[] getPhoto(String type, String url) {
        try {
            ListObjectsV2Result objList = s3.listObjectsV2(globals.getBucketName(), url + "/" + type);
            S3ObjectSummary objSum = objList.getObjectSummaries().get(0);
            S3Object obj = s3.getObject(globals.getBucketName(), objSum.getKey());
            S3ObjectInputStream objInStream = obj.getObjectContent();
            return IOUtils.toByteArray(objInStream);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download data from s3", e);
        }
    }

    private String getImageUrl(String type, String advUrl) {
        try {
            ListObjectsV2Result objList = s3.listObjectsV2(globals.getBucketName(), advUrl + "/" + type);
            S3ObjectSummary objSum = objList.getObjectSummaries().get(0);

            URL imageUrl = s3.getUrl(globals.getBucketName(), objSum.getKey());

            return imageUrl.toString();

        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to get image url from s3", e);
        }
    }
}

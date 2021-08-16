package org.feelfee.core.bucket;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BucketName {

    PROFILE_IMAGE("linkme-store");

    private final String bucketName;

}

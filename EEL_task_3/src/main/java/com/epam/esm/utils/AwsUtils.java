package com.epam.esm.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.epam.esm.exceptionhandler.exceptions.rest.InvalidFileException;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@UtilityClass
public class AwsUtils {
    @Value("${aws.access}")
    private String accessKey;
    @Value("${aws.secret}")
    private String secretKey;

    public static String loadImage(MultipartFile image,String directory) {
        String extension = ParamsValidation.validateFileExtension(image);
        try (InputStream imageInputStream = image.getInputStream()) {
            Regions region = Regions.EU_NORTH_1;
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            String bucket = "mjc-content";
            String imageId = UUID.randomUUID().toString();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            String objectKey = directory + imageId + "." + extension;
            s3Client.putObject(new PutObjectRequest(bucket, objectKey, imageInputStream, metadata));
            return "https://" + bucket + ".s3." + region.getName() + ".amazonaws.com/" + objectKey;
        } catch (IOException e) {
            throw new InvalidFileException(e.getMessage());
        }
    }
}

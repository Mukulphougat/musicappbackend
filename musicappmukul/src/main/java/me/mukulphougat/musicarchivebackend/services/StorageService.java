package me.mukulphougat.musicarchivebackend.services;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {
    private final AmazonS3 space;
    private final String access;
    private final String secret;
    public StorageService(@Value("${accessKey}") String access, @Value("${secretKey}") String secret) {
        this.access = access;
        this.secret = secret;

        /*
        "AKIAYRDU4DKDSEMTIHMC"
        "oD+nM/TPuaIcz8dSB9dpnR3+dB8PzPJ1+yhsqp+J"

         */

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(access,secret);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        space = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion("us-east-1").build();
    }


    public List<String> getSongFileNames() {

        ListObjectsV2Result result;
        if (space != null && space.doesBucketExistV2("musicarchivemukul")) {

            result = space.listObjectsV2("musicarchivemukul");
            List<S3ObjectSummary> objects = result.getObjectSummaries();

            return objects.stream()
                    .map(S3ObjectSummary::getKey).collect(Collectors.toList());
        } else {
            System.out.println("Not Exists");
            return null;
        }
    }

    public void uploadSong(MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        space.putObject(
                new PutObjectRequest(
                        "musicarchivemukul",
                        file.getOriginalFilename(),
                        file.getInputStream(),
                        objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead)
        );

    }
}

package me.mukulphougat.musicarchivebackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MusicArchiveBackendApplication {
//    public static String accessK = null;
//
//    public MusicArchiveBackendApplication(@Value("${accessKey}") String accessK) {
//        MusicArchiveBackendApplication.accessK = accessK;
//    }

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(MusicArchiveBackendApplication.class, args);
//        StorageService storageService = context.getBean(StorageService.class);
//        storageService.getSongFileNames();
//        System.out.println(storageService.getSongFileNames());
//        System.out.println(accessK);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

}

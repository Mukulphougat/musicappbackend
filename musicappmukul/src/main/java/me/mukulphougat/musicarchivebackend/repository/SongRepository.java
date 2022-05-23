package me.mukulphougat.musicarchivebackend.repository;

import me.mukulphougat.musicarchivebackend.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Song, String> {
    boolean existsSongByFileNameEquals(String fileName);
    boolean existsSongByTitleEquals(String title);

}

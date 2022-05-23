package me.mukulphougat.musicarchivebackend.controllers;

import com.mongodb.lang.Nullable;
import me.mukulphougat.musicarchivebackend.model.Song;
import me.mukulphougat.musicarchivebackend.repository.SongRepository;
import me.mukulphougat.musicarchivebackend.services.StorageService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.*;

@RestController
@EnableWebMvc
@RequestMapping(value = "/songs")
@CrossOrigin(origins="http://localhost:3000")
public class SongController {
    private final StorageService storageService;
    private final SongRepository songRepository;
    @Autowired
    public SongController(StorageService storageService, SongRepository songRepository){
        this.storageService = storageService;
        this.songRepository = songRepository;
    }
    @GetMapping
    public ResponseEntity<List<Song>> getSongs() {
        return ResponseEntity.ok(songRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSong(@PathVariable String id){
        Optional<Song> song = songRepository.findById(id);
        if ( song.isPresent() ) {
            return ResponseEntity.ok(song.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/uploadMySong")
    public ResponseEntity<?> uploadSong(@Nullable @RequestParam("Title") String title,@Nullable @RequestParam("Artist") String artist , @Nullable @RequestParam("Favourite") boolean favourite, @RequestParam(value = "file") MultipartFile file) throws IOException {
        if ( songRepository.existsSongByTitleEquals(title)  || songRepository.existsSongByFileNameEquals(file.getOriginalFilename())) {
            return ResponseEntity.badRequest().body("already in database");
        }
        else {
            System.out.println("Uploading the file");
            storageService.uploadSong(file);
            Song song = new Song();
            song.setFileName(file.getOriginalFilename());
            song.setFavourite(favourite);
            song.setTitle(title);
            song.setArtist(artist);
            Song insertedSong = songRepository.insert(song);
            return new ResponseEntity<>(insertedSong, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable String id, @RequestBody Song songData){
        Optional<Song> songOptional = songRepository.findById(id);
        if ( songOptional.isPresent() ) {
            Song song = songOptional.get();
            if ( songData.getTitle() != null ) {
                song.setTitle(songData.getTitle());
            }
            if ( songData.getArtist() != null ) {
                song.setArtist(songData.getArtist());
            }
            song.setFavourite(songData.isFavourite());
            songRepository.save(song);
            return ResponseEntity.ok(song);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Song> deleteSong(@PathVariable String id){
        if ( songRepository.existsById(id) ) {
            songRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/PrintHey")
    public String printhey() {
        return "Hey!";
    }
}

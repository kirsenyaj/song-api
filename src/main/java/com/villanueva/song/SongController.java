package com.villanueva.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villanueva/songs")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    // GET /villanueva/songs - Retrieve all songs
    @GetMapping
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // POST /villanueva/songs - Add a new song
    @PostMapping
    public Song addSong(@RequestBody Song song) {
        return songRepository.save(song);
    }

    // GET /villanueva/songs/{id} - Retrieve a specific song by ID
    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
        return songRepository.findById(id).orElse(null);
    }

    // PUT /villanueva/songs/{id} - Update an existing song
    @PutMapping("/{id}")
    public Song updateSong(@PathVariable Long id, @RequestBody Song songDetails) {
        return songRepository.findById(id).map(song -> {
            song.setTitle(songDetails.getTitle());
            song.setArtist(songDetails.getArtist());
            song.setAlbum(songDetails.getAlbum());
            song.setGenre(songDetails.getGenre());
            song.setUrl(songDetails.getUrl());
            return songRepository.save(song);
        }).orElseGet(() -> {
            songDetails.setId(id);
            return songRepository.save(songDetails);
        });
    }

    // DELETE /villanueva/songs/{id} - Delete a song and return a specific string message
    @DeleteMapping("/{id}")
    public String deleteSong(@PathVariable Long id) {
        songRepository.deleteById(id);
        return "Song with ID " + id + " deleted.";
    }

    // GET /villanueva/songs/search/{keyword} - Search across title, artist, album, and genre
    @GetMapping("/search/{keyword}")
    public List<Song> searchSongs(@PathVariable String keyword) {
        return songRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrGenreContainingIgnoreCase(
                keyword, keyword, keyword, keyword
        );
    }
}

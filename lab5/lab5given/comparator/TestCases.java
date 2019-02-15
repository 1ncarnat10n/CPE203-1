import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.*;

import org.junit.Test;
import org.junit.Before;

public class TestCases {
    private static final Song[] songs = new Song[] {
            new Song("Decemberists", "The Mariner's Revenge Song", 2005),
            new Song("Rogue Wave", "Love's Lost Guarantee", 2005),
            new Song("Avett Brothers", "Talk on Indolence", 2006),
            new Song("Gerry Rafferty", "Baker Street", 1998),
            new Song("City and Colour", "Sleeping Sickness", 2007),
            new Song("Foo Fighters", "Baker Street", 1997),
            new Song("Queen", "Bohemian Rhapsody", 1975),
            new Song("Gerry Rafferty", "Baker Street", 1978)
        };

    @Test
    public void testArtistComparator() {
        Song[] handSortedSongs = new Song[] {songs[2], songs[4], songs[0], songs[5], songs[3], songs[7], songs[6], songs[1]};
        Song[] sortedSongs = songs.clone();
        Arrays.sort(sortedSongs, new ArtistComparator());

        for (int i=0; i < sortedSongs.length; i++) {
            assertEquals(sortedSongs[i], handSortedSongs[i]);
        }
    }

    @Test
    public void testLambdaTitleComparator() {
        Comparator<Song> byTitle = (Song song1, Song song2) -> song1.getTitle().compareTo(song2.getTitle());
        Song[] handSortedSongs = new Song[] {songs[3], songs[5], songs[7], songs[6], songs[1], songs[4], songs[2], songs[0]};
        Song[] sortedSongs = songs.clone();
        Arrays.sort(sortedSongs, byTitle);

        for (int i=0; i < sortedSongs.length; i++) {
            assertEquals(sortedSongs[i], handSortedSongs[i]);
        }
    }

    @Test
    public void testYearExtractorComparator() {
        Song[] handSortedSongs = new Song[] {songs[4], songs[2], songs[0], songs[1], songs[3], songs[5], songs[7], songs[6]};
        Song[] sortedSongs = songs.clone();
        Arrays.sort(sortedSongs, Comparator.comparingInt(Song::getYear).reversed());

        for (int i=0; i < sortedSongs.length; i++) {
            assertEquals(sortedSongs[i], handSortedSongs[i]);
        }
    }

    @Test
    public void testComposedComparator() {
        Comparator<Song> byTitle = (Song song1, Song song2) -> song1.getTitle().compareTo(song2.getTitle());
        Song[] handSortedSongs = new Song[] {songs[7], songs[5], songs[3], songs[6], songs[1], songs[4], songs[2], songs[0]};
        Song[] sortedSongs = songs.clone();
        Arrays.sort(sortedSongs, new ComposedComparator(byTitle, Comparator.comparingInt(Song::getYear)));

        for (int i=0; i < sortedSongs.length; i++) {
            assertEquals(sortedSongs[i], handSortedSongs[i]);
        }
    }

    @Test
    public void testThenComparing() {
        Comparator<Song> byTitle = (Song song1, Song song2) -> song1.getTitle().compareTo(song2.getTitle());
        Song[] handSortedSongs = new Song[] {songs[7], songs[5], songs[3], songs[6], songs[1], songs[4], songs[2], songs[0]};
        Song[] sortedSongs = songs.clone();
        Arrays.sort(sortedSongs, byTitle.thenComparing(Comparator.comparingInt(Song::getYear)));

        for (int i=0; i < sortedSongs.length; i++) {
            assertEquals(sortedSongs[i], handSortedSongs[i]);
        }

    }

    @Test
    public void runSort() {
        List<Song> songList = new ArrayList<>(Arrays.asList(songs));
        List<Song> expectedList = Arrays.asList(
            new Song("Avett Brothers", "Talk on Indolence", 2006),
            new Song("City and Colour", "Sleeping Sickness", 2007),
            new Song("Decemberists", "The Mariner's Revenge Song", 2005),
            new Song("Foo Fighters", "Baker Street", 1997),
            new Song("Gerry Rafferty", "Baker Street", 1978),
            new Song("Gerry Rafferty", "Baker Street", 1998),
            new Song("Queen", "Bohemian Rhapsody", 1975),
            new Song("Rogue Wave", "Love's Lost Guarantee", 2005)
            );

        Comparator<Song> artistComparator = new ArtistComparator();
        Comparator<Song> byTitle = (Song song1, Song song2) -> song1.getTitle().compareTo(song2.getTitle());
        songList.sort(artistComparator.thenComparing(byTitle.thenComparing(Comparator.comparingInt(Song::getYear))));


        assertEquals(songList, expectedList);
    }

}

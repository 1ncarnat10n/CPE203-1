import java.util.Comparator;

public class ComposedComparator implements Comparator<Song> {
    private Comparator<Song> c1;
    private Comparator<Song> c2;

    public ComposedComparator(Comparator<Song> c1, Comparator<Song> c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public int compare(Song song1, Song song2) {
        int c1Res = c1.compare(song1, song2);
        if (c1Res == 0) {
            return c2.compare(song1, song2);
        }
        else {
            return c1Res;
        }

    }


}

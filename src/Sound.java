import javax.sound.sampled.*;

public class Sound {

    Clip clip;
    private String[] tracks = {
            "/sounds/song1.wav",
            "/sounds/song2.wav",
            "/sounds/song3.wav",
            "/sounds/song4.wav",
            "/sounds/song5.wav",
            "/sounds/song6.wav",
            "/sounds/song7.wav",
            "/sounds/song8.wav",
            "/sounds/song9.wav"
    };

    private int currentTrack = (int)(Math.random() * tracks.length);

    public void play(String path)
    {
        try
        {
            stop();
            var music = getClass().getResource(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(music);
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {e.printStackTrace();}
    }

    public void playTrack()
    {
        play(tracks[currentTrack]);

        clip.addLineListener(event ->
        {
            if (event.getType() == LineEvent.Type.STOP) {if (clip.getFramePosition() >= clip.getFrameLength()) {nextTrack();}}
        });
    }

    public void nextTrack()
    {
        currentTrack++;
        if (currentTrack >= tracks.length) {currentTrack = 0;}
        playTrack();
    }

    public void previousTrack()
    {
        currentTrack--;
        if (currentTrack < 0) {currentTrack = tracks.length - 1;}
        playTrack();
    }

    public void stop()
    {
        if (clip != null) {clip.stop();clip.close();}
    }
}
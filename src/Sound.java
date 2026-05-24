import javax.sound.sampled.*;

public class Sound {

    Clip clip;

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

    public void stop()
    {
        if (clip != null) {clip.stop();clip.close();}
    }
}
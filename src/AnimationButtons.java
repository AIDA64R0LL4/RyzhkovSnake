public class AnimationButtons {

    public float restartScale = 1.0f;
    public float homeScale = 1.0f;

    public void animate(boolean restartHover, boolean homeHover) {

        float speed = 0.12f;

        restartScale += ((restartHover ? 1.12f : 1.0f) - restartScale) * speed;
        homeScale += ((homeHover ? 1.12f : 1.0f) - homeScale) * speed;
    }
}
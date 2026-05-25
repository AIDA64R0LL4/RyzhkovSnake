import java.io.*;

public class SaveLoadStats {

    public int bestScore;
    public int deaths;
    public int wins;
    public int totalApples;

    public void load() {

        try {

            File file = new File("stats.txt");

            if (!file.exists()) {

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("0\n0\n0\n0");
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));

            bestScore = Integer.parseInt(reader.readLine());
            deaths = Integer.parseInt(reader.readLine());
            wins = Integer.parseInt(reader.readLine());
            totalApples = Integer.parseInt(reader.readLine());

            reader.close();

        } catch (Exception e) {

            bestScore = 0;
            deaths = 0;
            wins = 0;
            totalApples = 0;

            e.printStackTrace();
        }
    }

    public void save() {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter("stats.txt"));

            writer.write(bestScore + "\n");
            writer.write(deaths + "\n");
            writer.write(wins + "\n");
            writer.write(totalApples + "\n");

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

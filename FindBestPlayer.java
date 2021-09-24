package FBP;

import java.io.*;
import java.util.*;

public class FindBestPlayer {

    public static Map<String, Integer> mapOfPlayers = new HashMap<>();

    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        set.add("D:\\test1.csv");
        set.add("D:\\test2.csv");

        for (String text : set)
        {
            File file = new File(text);

            if (file.exists() && !file.isDirectory()) {
                String extension = "";
                String fileFormat = file.getName();
                int k = fileFormat.lastIndexOf('.');
                if (k >= 0) { extension = fileFormat.substring(k+1); }

                if (extension.equals("csv")) {

                    playingGame(oneGameFromFile(text));
                }
            } else {
                System.out.println("Wrong file format!");
                break;
            }
        }

        System.out.println("The nickname of the Most Valuable Player: " + findTheBestPlayer());
    }

    public static String[] splitString(String str){
        String[] subStr;
        subStr = str.split(";");
        return subStr;
    }

    public static ArrayList<String> oneGameFromFile (String fileName) {
        ArrayList<String> oneGame = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))  {
            while (reader.ready()){
                String volum = reader.readLine();
                oneGame.add(volum);
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return oneGame;
    }

    public static void playingGame(ArrayList<String> gameList){
        int gameNumbr = whatGameIsIt(gameList.get(0));
        gameList.remove(0);
        String[][] gameAsList = new String[gameList.size()][];
        for (int i = 0; i < gameList.size(); i++)
        {
            gameAsList[i] = splitString(gameList.get(i));
        }
        if (isThereAnyClones(gameAsList)) {
            System.out.println("There is more than one unique player in the game");
        } else {
            for (int i = 0; i < gameAsList.length; i++) {
                addPointsToPlayers(gameAsList[i][1], ratingPointsForOneGame(gameAsList[i], gameNumbr, winnerTeam(gameAsList)));
            }
        }
    }

    public static int whatGameIsIt (String gameName) {
        int gameNumber = -1;
        if (gameName.equals("BASKETBALL")) {
            gameNumber = 1;
        } else if (gameName.equals("HANDBALL")) {
            gameNumber = 2;
        } else {
            gameNumber = 0;
        }
        return gameNumber;
    }

    public static boolean isThereAnyClones(String[][] players) {
        boolean ans = false;
        for (int n = 0; n < players.length; n++) {
            for (int m = n+1; m < players.length; m++) {
                if (players[n][1].equals(players[m][1])) {
                    ans = true;
                    break;
                }
            }
        }
        return ans;
    }

    public static String winnerTeam (String[][] gameAsList) {
        String winner;
        String team1 = gameAsList[0][3];
        String team2 = "";
        int scoreTeam1 = 0;
        int scoreTeam2 = 0;
        for (int i = 0; i < gameAsList.length; i++) {
            if (!gameAsList[i][3].equals(team1)) {
                team2 = gameAsList[i][3];
                break;
            }
        }
        for (int i = 0; i < gameAsList.length; i++) {
            if (gameAsList[i][3].equals(team1)) {
                scoreTeam1 += Integer.parseInt(gameAsList[i][4]);
            } else if (gameAsList[i][3].equals(team2)) {
                scoreTeam2 += Integer.parseInt(gameAsList[i][4]);
            } else {
                System.out.println("Error! More than two teams playing!");
            }
        }
        if (scoreTeam1 > scoreTeam2) {
            winner = team1;
        } else {
            winner = team2;
        }
        return winner;
    }

    public static int ratingPointsForOneGame (String[] playersStat, int gameNumber, String winner) {
        int score = 0;
        if (gameNumber == 1) {
            score = Integer.parseInt(playersStat[4]) * 2 + Integer.parseInt(playersStat[5]) * 1 + Integer.parseInt(playersStat[6]) * 1;
        } else if (gameNumber == 2) {
            score = Integer.parseInt(playersStat[4]) * 2 - Integer.parseInt(playersStat[5]) * 1;
        } else {
            System.out.println("Wrong game name!");
        }

        if (playersStat[3].equals(winner)){
            score += 10;
        }
        return score;
    }

    public static void addPointsToPlayers (String playersNickname, int points) {
        if (mapOfPlayers.isEmpty()) {
            mapOfPlayers.put(playersNickname, points);
        } else if (!mapOfPlayers.containsKey(playersNickname)) {
            mapOfPlayers.put(playersNickname, points);
        } else {
            mapOfPlayers.put(playersNickname, mapOfPlayers.get(playersNickname) + points);
        }
    }

    public static String findTheBestPlayer () {
        String bestPlayer = "";
        int maxPoint = 0;
        for (Map.Entry<String, Integer> pair : mapOfPlayers.entrySet())
        {
            String key = pair.getKey();
            int value = pair.getValue();
            if (maxPoint < value) {
                maxPoint = value;
                bestPlayer = key;
            }
        }
        return bestPlayer;
    }
}

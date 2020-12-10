import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class TextFileManager {
    private Game game;

    public TextFileManager(Game game) {
        this.game = game;
    }

//    public String[][] getHighScores(String filePath) {
//        BufferedReader buffReader = null;
//        FileReader fileReader = null;
//        ArrayList<String[]> highScores = new ArrayList<String[]>();
//        String nextLine = null;
//
//        try {
//            if (filePath == null) {
//                return null;
//            } else {
//                fileReader = new FileReader(filePath);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("file doesnt exist");
//            return null;
//        } finally {
//            if (fileReader != null) {
//                buffReader = new BufferedReader(fileReader);
//            }
//
//            try {
//                if (buffReader != null) {
//                    nextLine = buffReader.readLine();
//                }
//
//            } catch (IOException e) {
//                return null;
//            }
//        }
//
//        while (nextLine != null) {
//            String[] splitLine = nextLine.split(" ");
//
//            if (splitLine != null) {
//                if (splitLine.length == 2) {
//                    int score;
//                    try {
//                        score = Integer.parseInt(splitLine[1].trim());
//                    } catch (NumberFormatException e) {
//                        score = -1;
//                    }
//
//                    if (score > 0) {
//                        highScores.add(splitLine);
//                    }
//
//                }
//            }
//
//            try {
//                if (buffReader != null) {
//                    nextLine = buffReader.readLine();
//                }
//            } catch (IOException e) {
//                return null;
//            }
//
//        }
//
//        try {
//            buffReader.close();
//        } catch (IOException e) {
//        }
//
//        for (String[] entry : highScores) {
//            System.out.println(entry[0] + ", " + entry[1]);
//        }
//
//        return selectionSortTop10(listToStringArr(highScores));
//    }
//
//    public String[][] listToStringArr(ArrayList<String[]> list) {
//        String[][] stringArr = new String[list.size()][2];
//
//        int index = 0;
//        boolean makeNull = false;
//        for (String[] entry : list) {
//            if (entry.length != 2) {
//                makeNull = true;
//            }
//
//            stringArr[index][0] = entry[0];
//            stringArr[index][1] = entry[1];
//            index++;
//        }
//
//        if (makeNull) {
//            return null;
//        } else {
//            return stringArr;
//        }
//    }
//
//    /**
//     * @param stringArr
//     * @return
//     */
//    public String[][] selectionSortTop10(String[][] stringArr) {
//        int size = stringArr.length;
//
//        for (int i = 0; i < size - 1; i++) {
//            int maxIndex = i;
//
//            for (int j = i + 1; j < size; j++) {
//                int scoreMaxIndex;
//                int scoreJ;
//
//                try {
//                    scoreMaxIndex = Integer.parseInt(stringArr[maxIndex][1]);
//                } catch (NumberFormatException e) {
//                    scoreMaxIndex = -1;
//                }
//
//                try {
//                    scoreJ = Integer.parseInt(stringArr[j][1]);
//                } catch (NumberFormatException e) {
//                    scoreJ = -1;
//                }
//
//                if (scoreMaxIndex > 0 && scoreJ > 0) {
//                    if (scoreJ > scoreMaxIndex) {
//                        maxIndex = j;
//                    }
//                }
//
//            }
//
//            String nameTemp = stringArr[i][0];
//            String scoreTemp = stringArr[i][1];
//
//            stringArr[i][0] = stringArr[maxIndex][0];
//            stringArr[i][1] = stringArr[maxIndex][1];
//
//            stringArr[maxIndex][0] = nameTemp;
//            stringArr[maxIndex][1] = scoreTemp;
//        }
//
//        if (size > 10) {
//            String[][] modified = new String[10][2];
//
//            for (int i = 0; i < 10; i++) {
//                modified[i][0] = stringArr[i][0];
//                modified[i][1] = stringArr[i][1];
//            }
//
//            return modified;
//        } else {
//            return stringArr;
//        }
//    }
//
//    public void updateHighScores(String filePath, int newScore) {
//        BufferedWriter buffWriter = null;
//        FileWriter fileWriter = null;
//        String[][] highScores = null;
//        String nextLine = null;
//
//        try {
//            if (filePath != null) {
//                highScores = getHighScores(filePath);
//                fileWriter = new FileWriter(filePath, false);
//            } 
//        } catch (FileNotFoundException e) {
//            System.out.println("file doesnt exist");
//        } finally {
//            if (fileWriter != null) {
//                buffWriter = new BufferedWriter(fileWriter);
//                
//                if (highScores == null) {
//                    
//                } else {
//                    String[][] newStringArr = new String[newStringArr.length + 1][2];
//                    int lowestScore;
//                    try {
//                        lowestScore = Integer.parseInt(highScores[highScores.length - 1][1]);
//                    } catch (NumberFormatException e) {
//                        lowestScore = -1;
//                    }
//                    
//                    if (newScore > lowestScore) {
//                        String inputName = JOptionPane.showInputDialog(this.game.getFrame(), "Please enter a username.", 
//                                "Username", JOptionPane.PLAIN_MESSAGE);
//                    }
//                    
//                    for (int i = 0; i < newStringArr.length; i++) {
//                        newStringArr[i][0] = highScores[i][0];
//                        newStringArr[i][1] = highScores[i][1];
//                    }
//                    
//                    newStringArr[newStringArr.length][0] = ;
//                    newStringArr[newStringArr.length][1] = "" + newScore;
//                    
//                    boolean continueWriting = true;
//                    if (highScores.length == 0) {
//                        
//                    } else {
//                        
//                    }
//                }
//                
//                boolean continueWriting = true;
//                while (iterator.hasNext() && continueWriting) {
//                    try {
//                        bw.write(iterator.next());
//                        bw.newLine();
//                    } catch (IOException e) {
//                        continueWriting = false;
//                    }
//                }
//                
//            }
//
//        }
//
//        while (nextLine != null) {
//            String[] splitLine = nextLine.split(" ");
//
//            if (splitLine != null) {
//                if (splitLine.length == 2) {
//                    int score;
//                    try {
//                        score = Integer.parseInt(splitLine[1].trim());
//                    } catch (NumberFormatException e) {
//                        score = -1;
//                    }
//
//                    if (score > 0) {
//                        highScores.add(splitLine);
//                    }
//
//                }
//            }
//
//            try {
//                if (buffReader != null) {
//                    nextLine = buffReader.readLine();
//                }
//            } catch (IOException e) {
//                return null;
//            }
//
//        }
//        
//        try {
//            buffReader.close();
//        } catch (IOException e) {
//        }
//
//        for (String[] entry : highScores) {
//            System.out.println(entry[0] + ", " + entry[1]);
//        }
//
//        return selectionSortTop10(highScores);
//    }
//
//    public static void main(String[] args) {
//        String[][] result = getHighScores("files/high_scores.txt");
//
//        System.out.println("");
//        if (result != null) {
//            for (int i = 0; i < result.length; i++) {
//                System.out.println(result[i][0] + ", " + result[i][1]);
//            }
//        }
//    }

//    
//    @Override
//    public boolean hasNext() {
//        boolean result = false;
//
//        if (this.buffReader != null) {
//            if (this.nextLine != null) {
//                result = true;
//            } else {
//                try {
//                    this.buffReader.close();
//                } catch (IOException e) {
//
//                } finally {
//                    result = false;
//                }
//            }
//        } else {
//            result = false;
//        }
//
//        return result;
//    }
//
//    static List<String> csvFileToTweets(String pathToCSVFile, int tweetColumn) {
//        FileLineIterator iterator = new FileLineIterator(pathToCSVFile);
//        List<String> stringList = new LinkedList<String>();
//
//        while (iterator.hasNext()) {
//            String stringToAdd = extractColumn(iterator.next(), tweetColumn);
//            if (stringToAdd != null) {
//                stringList.add(stringToAdd);
//            }
//        }
//
//        return stringList;
//    }
//    
//    @Override
//    public String next() {
//        if (this.hasNext()) {
//            String next = this.nextLine;
//
//            try {
//                this.nextLine = this.buffReader.readLine();
//            } catch (IOException e) {
//                System.out.println("IO Help");
//                this.nextLine = null;
//            }
//
//            return next;
//        } else {
//            throw new NoSuchElementException();
//        }
//    }
//    
//    
//    public void writeStringsToFile(List<String> stringsToWrite, String filePath, boolean append) {
//        File file = Paths.get(filePath).toFile();
//        BufferedWriter bw = null;
//
//        Writer fileWriter = null;
//        try {
//            fileWriter = new FileWriter(file, append);
//        } catch (FileNotFoundException e) {
//            throw new IllegalArgumentException();
//        } catch (IOException e) {
//
//        }
//
//        if (fileWriter != null) {
//            bw = new BufferedWriter(fileWriter);
//
//            Iterator<String> iterator = stringsToWrite.listIterator();
//            boolean continueWriting = true;
//            while (iterator.hasNext() && continueWriting) {
//                try {
//                    bw.write(iterator.next());
//                    bw.newLine();
//                } catch (IOException e) {
//                    continueWriting = false;
//                }
//            }
//
//            try {
//                bw.close();
//            } catch (IOException e) {
//
//            }
//        }
//    }
}

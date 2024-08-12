import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static String[][] arr;                       //  2d array containing whole elements in the puzzle with i and j - [i rows/down ,j- columns/right]
    public static int si;                               //  start position i index
    public static int sj;                               //  start position j index
    public static int ei;                               //  finish position i index
    public static int ej;                               //  finish position j index
    public static int [][] blockList;                   //  indexes of block list - {{index}{i,j}}


    public static void main(String[] args){
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter file name in '.txt' format: ");                 // Prompt the user to enter a file name in '.txt' format
            String fileName = scanner.nextLine();                                   // Read the user input as the file name

            File file = new File(fileName);                                         // Create a File object with the provided file name
            if (!file.exists()) {                                                   // Check if the file does not exist
                System.out.println("File not found. Exiting program.");
                return;                                                             //  Stop program execution if file does not exist
            }

            Scanner input = new Scanner(file);
            arr=new String[numOfRows(fileName)][numOfColumns(fileName)];            //  Initialize the size of the 2d array

            int index = 0;                                        //  line index
            while(input.hasNextLine()){
                arr[index] = input.nextLine().split("(?!^)");
                index++;
            }

            startAndEnd();                                      //  Initialize start and end
            initializeBlocks();                                 //  Initialize the blocks [0] into 2d array
            AStar aStar = new AStar(numOfColumns(fileName),numOfRows(fileName), si, sj, ei, ej, blockList);

            aStar.display();                                    //  Displaying the puzzle

            long startTime = System.nanoTime();                 //  This saves the
            aStar.process();                                    //  Doing the process
            long endTime = System.nanoTime();                   //  This records the end time

            aStar.displaySolutionCorrect();                     //  Displaying the solution

            long duration = (endTime - startTime);              //  Calculate the duration

            System.out.println("Elapsed time = " + (double)duration / 1000000 +" milliseconds");

            input.close();
        }catch (IOException e){
            System.out.println("An IOException occurred: " + e.getMessage());
        }
    }



    /**
     * Get the number of rows in the puzzle.
     * @param fileName Name of the puzzle file
     * @return number of rows in the puzzle
     */
    public static int numOfRows(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int lines = 0;
        while (reader.readLine() != null) {
            lines++;
        }
        reader.close();
        return lines;
    }

    /**
     * Get the number of columns in the puzzle.
     * @param fileName Name of the puzzle file
     * @return number of columns in the puzzle
     */
    public static int numOfColumns(String fileName) throws FileNotFoundException {
        int numOfColumns;
        File file = new File(fileName);
        Scanner input = new Scanner(file);
        numOfColumns = input.nextLine().split("(?!^)").length;
        return numOfColumns;
    }

    /**
     * Initialize the start and end positions (i and j).
     */
    public static void startAndEnd(){
        for (int i=0;i<arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j].equals("S")){
                    si=i;
                    sj=j;
                }else if(arr[i][j].equals("F")){
                    ei=i;
                    ej=j;
                }
            }
        }
    }

    /**
     * Initialize the blocks indexes into a 2d array.
     */
    public static void initializeBlocks(){
        ArrayList <int[]> blocks = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if(arr[i][j].equals("0")){
                    blocks.add(new int[]{i,j});
                }
            }
        }

        //  initialize blockList array
        blockList = new int[blocks.size()][2];
        for (int i = 0; i < blocks.size(); i++){
            blockList[i] = blocks.get(i);
        }
    }
}

/*Abdul Rehman
CSC112 Fall 2021
Programming Assignment 2
October 13, 2021
This program dithers different images in different dithering techniques.
*/

//imports
import java.io.*;
import java.util.Random;
import java.util.Scanner;

    public class Main {


        public static void main(String[] args) throws IOException {
        /*Put a loop around all of this so that you can dither a different image file by whatever method
        you choose each time through the loop. This way, you can dither as many images as you
        want by as many methods as you want in one run of the program.*/
            int initiator = 1;
            //loop for dithering multiple files
            while (initiator > 0) {

                //scanner information for dithering
                Scanner scnr = new Scanner(System.in);
                System.out.println("What is the input file name?");
                String inputFile = scnr.next();
                System.out.println("What is width of the input?");
                int w = scnr.nextInt();
                System.out.println("What is height of the input?");
                int h = scnr.nextInt();
                System.out.println("What is name of the output file?");
                String outputFile = scnr.next();
                InputStream inputStream = new FileInputStream(inputFile);
                OutputStream outputStream = new FileOutputStream(outputFile);
                System.out.println("What dithering method do you want to use?");
                System.out.print(" 1 for threshold");
                System.out.print(" 2 for random");
                System.out.print(" 3 for pattern");
                System.out.println(" 4 for error diffusion");
                int ditherMethod = scnr.nextInt();
                //switch for specific dithering methods
                switch (ditherMethod) {
                    case 1:
                        threshold(inputStream, outputStream, w, h);
                        break;
                    case 2:
                        random(inputStream, outputStream, w, h);
                        break;
                    case 3:
                        pattern(inputStream, outputStream, w, h);
                        break;
                    case 4:
                        errDiff(inputStream, outputStream, w, h);
                        break;
                    default:
                        System.out.println("Not a valid choice");
                        System.exit(1);
                }
                //scanner information for repeating loop or
                System.out.println("Is this the last file to dither?");
                System.out.print(" 1 for yes");
                System.out.println(" , otherwise press any other key to continue");
                int answer = scnr.nextInt();
                if (answer == 1) {
                    System.exit(1);
                }
                initiator++;
            }
        }
        //threshold dither
        public static void threshold(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
            int r = 0;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int pixel = inputStream.read();
                    if (pixel < 128) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                }
            }
        }
        //random dither
        public static void random(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
            Random rnd = new Random();
            int r;
            int[][] pixels = new int[h][w];
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pixels[i][j] = inputStream.read();
                    System.out.print(pixels[i][j]);
                    r = rnd.nextInt(256);
                    if (pixels[i][j] < r) {
                        outputStream.write(0);
                        System.out.println(" ----- 0");
                    } else {
                        outputStream.write(255);
                        System.out.println(" ----- 255");
                    }
                }
            }
        }
        //pattern dither
        public static void pattern(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
            //pattern array
            int[][] pattern = {{8, 3, 4}, {6, 1, 2}, {7, 5, 9}};
            double[][] pixels = new double[h][w];
            //comparing pixels array to pattern array
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pixels[i][j] = inputStream.read();
                    pixels[i][j] = pixels[i][j] / 25.6;
                    if (pixels[i][j] < pattern[i % 3][j % 3]) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                }
            }

        }
        //error diffusion dithering
        public static void errDiff(InputStream inputStream, OutputStream outputStream, int w, int h) throws IOException {
            int [][] errDiffMask = {{0, 0, 7}, {3, 5, 1}};
            double [][] pixels = new double[h][w];
            double e;
            int usableHeight = h - 1;
            int usableWidth = w - 1;
            //loop for reading input file information into array
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    pixels[i][j] = inputStream.read();
                }
            }
            //loop for setting neighbor pixel values
            for (int i = 0; i < usableHeight; i++) {
                for (int j = 1; j < usableWidth; j++) {
                    if (pixels[i][j] < 128) {
                        e = pixels[i][j];
                    } else {
                        e = pixels[i][j] - 255;
                    }
                    pixels[i][j+1] = pixels[i][j+1] + (e/16)*7;
                    pixels[i+1][j-1] = pixels[i+1][j-1] + (e/16)*3;
                    pixels[i+1][j] = pixels[i+1][j] + (e/16)*5;
                    pixels[i+1][j+1] = pixels[i+1][j+1] + (e/16);


                }
            }
            //loop for output file information
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    if (pixels[i][j] < 128) {
                        outputStream.write(0);
                    } else {
                        outputStream.write(255);
                    }
                }
            }
        }
    }




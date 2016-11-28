/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstPhase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author jessa
 */
public class FileSystem_1 {
     Tree_1 tree;
    Scanner scan;
    String currentDir;
    String absolutePath;
    String command;
    Node tmp;

    FileSystem_1() {
        tree = new Tree_1();
        initialize();
    }

    public static void main(String[] args) throws IOException {

        FileSystem_1 fs = new FileSystem_1();
        fs.scan = new Scanner(System.in);
        ArrayList<String> commands = new ArrayList();
        String line = "";
        boolean fileExists = false;
        do {
            System.out.println("Name of input file: ");
            String fileName = fs.scan.nextLine();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                System.out.println("File exists.");
                fileExists = true;
                while ((line = reader.readLine()) != null) {
                    commands.add(line);
                }
            } catch (FileNotFoundException ex) {
                fileExists = false;
                System.out.println("File not found. Try again.");
            }

        } while (!fileExists);
        fs.command = "";

        //enter function here where all the nodes will be loaded into the current file
        for (int i = 0; i < commands.size(); i++) {
            fs.waiting(commands.get(i));
        }

    }

    private void initialize() {
        currentDir = "/root";
        absolutePath = "/root";
        Node root = tree.addNode("root", null, false);
    }
    
     private void waiting(String command) throws IOException {
     
     }
}

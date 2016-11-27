/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstPhase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jessa
 */
public class FileSystem {

    Tree_1 tree;
    Scanner scan;
    String currentDir;
    String absolutePath;
    String command;

    FileSystem() {
        tree = new Tree_1();
        initialize();
    }

    public static void main(String[] args) throws IOException {

        FileSystem fs = new FileSystem();
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
//        while (!fs.command.equals("exit")) {
//            fs.waiting();
//        }

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

        pathStatus();
        System.out.println("");
        System.out.print("user@root: " + command);
//        command = scan.nextLine();
        String rem = "";
        if (!absolutePath.equals("root")) {
            rem = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
        }

        if (command.contains("cd")) { //navigation

            String nextPath = command.substring(command.lastIndexOf(" ") + 1, command.length());

            if (nextPath.equals("..")) {

                if (currentDir.equals("root")) {

                    System.out.println("Already in root.");

                } else {

                    rem = "/" + rem;
                    absolutePath = absolutePath.replace(rem, "");
                    currentDir = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);

                }
            }
            if (nextPath.contains("/root")) {

                absolutePath = nextPath;
                currentDir = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);

            } else if (tree.isConnected(currentDir.replaceAll("/", ""), nextPath)) {

                absolutePath = (absolutePath + "/" + nextPath);
                currentDir = "/" + nextPath;

            } else {

                System.out.println("No such file or directory.");
            }

        } else if (command.equals("ls")) {//showing contents on current dir

            System.out.println("");
            tree.displayChildren(tree.getNode(currentDir.replaceAll("/", "")));

        } else if (command.contains("ls ")) { //showing contents of a specific dir

            System.out.println("");
            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));
            dirName = dirName.substring(dirName.lastIndexOf("/") + 1);
            tree.displayChildren(tree.getNode(dirName));

        } else if (command.contains("mkdir")) {//making directories

            String[] com = command.replaceAll("mkdir", "").split(" ");
            
            if (com.length < 2) {
                
                System.out.println("\nMissing arguments.");
                
            } else {

                String par = currentDir.replaceAll("/", "");

                if (tree.isConnected(par, com[1])) {
                    System.out.println("");
                    System.out.println(com[1] + " already exists");
                    
                } else {

                    String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));

                    if (dirName.contains("/")) {
                        
                        String finalDirName = dirName.substring(dirName.lastIndexOf("/"));
                        String parent = (dirName.replace(finalDirName, "")).substring((dirName.replace(finalDirName, "")).lastIndexOf("/") + 1);
                        tree.addNode(finalDirName.replaceAll("/", ""), tree.getNode(parent), false);

                    } else {

                        tree.addNode(dirName, tree.getNode(currentDir.replaceAll("/", "")), false);

                    }
                }
            }
        } else if (command.contains("rm")) {//removing directories

            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));
            if (dirName.contains("/root")) {

                String finalDirName = dirName.substring(dirName.lastIndexOf("/") + 1);
                tree.removeNode(tree.getNode(finalDirName));

            } else if (!tree.isChild(tree.getNode(currentDir.replaceAll("/", "")), tree.getNode(dirName))) {

                System.out.println("\nNo such file or directory.");

            } else {

                tree.removeNode(tree.getNode(dirName));

            }
        } else if (command.contains("edit")) {//creation of files

            String fileName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));

            if (fileName.contains("/root")) {
                String finalDirName = fileName.substring(fileName.lastIndexOf("/"));
                String parent = (fileName.replace(finalDirName, "")).substring((fileName.replace(finalDirName, "")).lastIndexOf("/") + 1);
                Node t = tree.addNode(finalDirName.replaceAll("/", ""), tree.getNode(parent), true);
                t.fileFormat = finalDirName.replaceAll("/", "").substring(finalDirName.lastIndexOf("."));

            } else {
                tree.addNode(fileName, tree.getNode(currentDir.replaceAll("/", "")), true);
            }
        } else if (command.contains("rn ")) {//renaming of files

            String[] fileNames = command.replaceAll("rn", "").split(" ");
            if (fileNames.length < 3) {
                System.out.println("Error message. Missing arguments.");

            } else {
                System.out.println(fileNames[1]);
                System.out.println(fileNames[2]);

                //check if file actually exists in directory
                if (tree.isChild(tree.getNode(currentDir.replaceAll("/", "")), tree.getNode(fileNames[1]))) {
                    Node tmp = tree.getNode(fileNames[1]);
                    tmp.setValue(fileNames[2]);
                } else {
                    System.out.println("No such file or directory");
                }
            }

        } else if (command.contains("mv")) {
            String[] args = command.replace("mv", "").split(" ");
            if (args.length < 3) {
                System.out.println("Missing arguments.");
            } else {

            }

        }

        //add code here to implement persistence : meaning files will still be saved even after closing the program
        //@function here will be to create a text file to store all the nodes with their children
//        updateTree(tree.nodes);
    }

    private void pathStatus() {
        System.out.println("");
        System.out.println("Absolute Path: " + absolutePath);
        System.out.println("Current Directory: " + currentDir);
    }

    private void updateTree(ArrayList<Node<Object>> nodes) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        FileWriter fw = new FileWriter("nodes.txt");
        for (int i = 0; i < nodes.size(); i++) {
            fw.write("Parent: \n");
            fw.write((String) nodes.get(i).getValue());
            if (!nodes.get(i).children.isEmpty()) {
                fw.write("\nChildren:\n");
                for (int j = 0; j < nodes.get(i).children.size(); j++) {
                    fw.write((String) nodes.get(i).children.get(j).getValue() + "\n");

                }
            }
            fw.write("\n");
        }
        fw.close();
    }
}

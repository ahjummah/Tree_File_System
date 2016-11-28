/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstPhase;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
    Node tmp;

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
        boolean flag = false;
//        pathStatus();
        System.out.println("");
//        System.out.print("user@root: " + command);
        command = scan.nextLine();
        String rem = "";
        if (!absolutePath.equals("/root")) {
            rem = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
        }

        if (command.contains("cd")) { //navigation
            String[] args = command.split(" ");
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
            } else if (nextPath.contains("../")) {
                absolutePath = nextPath.replaceAll("../", "");

                currentDir = "/" + absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
                absolutePath = "/root" + currentDir;

            } else if (tree.isConnected(currentDir.replaceAll("/", ""), nextPath)) {

                absolutePath = (absolutePath + "/" + nextPath);
                currentDir = "/" + nextPath;
                flag = true;

            } else if (!tree.isConnected(currentDir.replaceAll("/", ""), nextPath)) {
                if (!args[1].equals("..")) {
                    System.out.println("\nNo such file or directory.");
                }
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

            if (com.length != 2) {

                System.out.println("\nusage: mkdir <directory name>");

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

                    } else if (dirName.contains("/")) {
                        //if it starts with a child of the current dir valid sha

                        //example: jessa/asd
                        String parent = "";
                        for (int i = 0; i < com[1].length(); i++) {

                            if (com[1].charAt(i) == '/') {
                                break;
                            } else {
                                parent += com[1].charAt(i);
                            }

                        }
                        Node p = tree.getNode(parent);

                        if (tree.isConnected(parent, dirName.substring(dirName.lastIndexOf("/")))) {
                            tree.addNode(p, tree.getNode(parent), false);

                        }

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
        } else if (command.contains("edit") || command.contains("> ")) {//creation of files
            String[] args = command.split(" ");
            if (args.length != 2) {
                if (command.contains("edit")) {
                    System.out.println("usage: edit <filename>");
                } else if (command.contains("> ")) {
                    System.out.println("usage: > <filename>");
                }
            } else {
                String fileName = "";
                if (args[1].contains("/")) {
                    //absolute value code here
                } else {
                    fileName = args[1];
                }
//            String fileName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));//absolute value
                System.out.println(fileName);

                String par = currentDir.replaceAll("/", "");
                Node parent = tree.getNode(par);
                if (tree.isConnected(par, fileName)) {
                    System.out.println("File already exists.");

                } else {
//                File file = new File(currentDir.replaceAll("/", "") + "-" + fileName);
//                file.createNewFile();
                    tmp = tree.addNode(fileName, parent, true);

                }

                JFrame frame = new JFrame();
                frame.setSize(700, 600);
                JPanel panel = new JPanel();
                JTextArea tf = new JTextArea();
                tf.setBackground(Color.LIGHT_GRAY);
                tf.setCaretPosition(0);

                frame.add(panel.add(tf));
                frame.setTitle("Overwriting "+fileName);

                frame.show();
                String output;

                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                        tmp.setContent(tf.getText());
                    }

                    private void overWriteFile(String title, String text) throws IOException {
                        FileWriter fw = new FileWriter(title, true);
                        fw.write(text);
                        fw.close();
                    }

                });
            }

        } else if (command.contains(">> ")) {
            String[] args = command.split(" ");
            if (args.length != 2) {

                System.out.println("usage: >> <filename>");

            } else {
                String fileName = "";
                if (args[1].contains("/")) {
                    //absolute value handling code here
                } else {
                    fileName = args[1];
                }
//            String fileName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));//absolute value
                System.out.println(fileName);

                String par = currentDir.replaceAll("/", "");
                Node parent = tree.getNode(par);
                if (!tree.isConnected(par, fileName)) {
                    System.out.println("No such file.");

                } else {
                    tmp = tree.addNode(fileName, parent, true);
                }
                
                JFrame frame2 = new JFrame();
                frame2.setSize(700, 600);
                JPanel panel2 = new JPanel();
                JTextArea tf2 = new JTextArea();
                tf2.setBackground(Color.LIGHT_GRAY);
                tf2.setCaretPosition(0);

                frame2.add(panel2.add(tf2));
                frame2.setTitle("Append to "+fileName);

                frame2.show();
                String output;

                frame2.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        String t = tmp.getContent();
                        System.out.println(t);
                        tmp.setContent(t + tf2.getText());
                    }

                });
            }

            
        } else if (command.contains("rn ")) {//renaming of files

            String[] fileNames = command.replaceAll("rn", "").split(" ");
            if (fileNames.length != 3) {
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
            if (args.length != 3) {
                System.out.println("Missing arguments.");
            } else {
                System.out.println(args[1]);
                System.out.println(args[2]);
                System.out.println("Moving " + args[1] + " to " + args[2]);
                Node child = tree.getNode(args[1]);
                Node oldParent = child.getParent();
                oldParent.children.remove(child);
                Node newParent = tree.getNode(args[2]);
                child.setParent(newParent);
                newParent.children.add(child);
            }

        } else if (command.contains("cp")) {

            String[] args = command.split(" ");
            if (args.length != 3) {
                System.out.println("usage: cp source_file/source_directory target_file/target_directory");
            } else {
                //////////////////
            }
        } else if (command.contains("show")) {

            String[] args = command.split(" ");
            if (args.length != 2) {
                System.out.println("usage: show <filename>");
            } else {
                Node parent = tree.getNode(currentDir.replaceAll("/", ""));
                Node child = tree.getNode2(parent, args[1]);
                //check if exists ba
                if (tree.isChild(parent, child)) {
                    System.out.println(child.getContent());
                } else {
                    System.out.println("File does not exist.");
                }
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

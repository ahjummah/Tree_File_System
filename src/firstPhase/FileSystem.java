/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstPhase;

import java.util.Scanner;

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

    public static void main(String[] args) {

        FileSystem fs = new FileSystem();
        fs.scan = new Scanner(System.in);
        fs.command = "";

        while (!fs.command.equals("exit")) {
            fs.waiting();
        }
    }

    private void initialize() {
        currentDir = "/root";
        absolutePath = "/root";
        Node root = tree.addNode("root", null, false);
        Node jessa = tree.addNode("jessa", root, false);
        Node mae = tree.addNode("mae", root, false);
        Node jessaFile = tree.addNode("jessaFile", jessa, false);

    }

    private void waiting() {

        pathStatus();
        System.out.println("");
        System.out.print("user@root: ");
        command = scan.nextLine();
        String rem = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);

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

            tree.displayChildren(tree.getNode(currentDir.replaceAll("/", "")));

        } else if (command.contains("ls ")) { //showing contents of a specific dir

            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));
            dirName = dirName.substring(dirName.lastIndexOf("/") + 1);
            tree.displayChildren(tree.getNode(dirName));

        } else if (command.contains("mkdir")) {//making directories

            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));

            if (dirName.contains("/root")) {

                String finalDirName = dirName.substring(dirName.lastIndexOf("/"));
                String parent = (dirName.replace(finalDirName, "")).substring((dirName.replace(finalDirName, "")).lastIndexOf("/") + 1);
                tree.addNode(finalDirName.replaceAll("/", ""), tree.getNode(parent), false);

            } else {

                tree.addNode(dirName, tree.getNode(currentDir.replaceAll("/", "")), false);

            }
        } else if (command.contains("rm")) {//removing directories

            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));
            if (dirName.contains("/root")) {

                String finalDirName = dirName.substring(dirName.lastIndexOf("/") + 1);
                tree.removeNode(tree.getNode(finalDirName));

            } else if (!tree.isChild(tree.getNode(currentDir.replaceAll("/", "")), tree.getNode(dirName))) {

                System.out.println("No such file or directory.");

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
            if (fileNames.length<3) {
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

        }
        else if(command.contains("mv")){
            String[] args = command.replace("mv", "").split(" ");
            if(args.length<3){
                System.out.println("Missing arguments.");
            }
            else{
                
            }
            
        }

    }

    private void pathStatus() {
        System.out.println("Absolute Path: " + absolutePath);
        System.out.println("Current Directory: " + currentDir);
    }
}

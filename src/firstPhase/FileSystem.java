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

        if (command.contains("cd")) {
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
            }

        } else if (command.equals("ls")) {
            tree.displayChildren(tree.getNode(currentDir.replaceAll("/", "")));
        }

        if (command.contains("mkdir")) {
            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));
            if (dirName.contains("/root")) {
                String finalDirName = dirName.substring(dirName.lastIndexOf("/"));
                String parent = (dirName.replace(finalDirName, "")).substring((dirName.replace(finalDirName, "")).lastIndexOf("/") + 1);
                tree.addNode(finalDirName.replaceAll("/", ""), tree.getNode(parent), false);
            } else {
                tree.addNode(dirName, tree.getNode(currentDir.replaceAll("/", "")), false);

            }
        }
        if (command.contains("rmdir")) {
            String dirName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));
            if (dirName.contains("/root")) {
                String finalDirName = dirName.substring(dirName.lastIndexOf("/")+1);
//                System.out.println(finalDirName);
               tree.removeNode(tree.getNode(finalDirName));

            } else if (!tree.isChild(tree.getNode(currentDir.replaceAll("/", "")), tree.getNode(dirName))) {
                System.out.println("No such file or directory.");
            } else {
                tree.removeNode(tree.getNode(dirName));
//                System.out.println("Directory found.");
            }
        }
    }

    private void pathStatus() {
        System.out.println("Absolute Path: " + absolutePath);
        System.out.println("Current Directory: " + currentDir);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package firstPhase;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
    boolean flag = false;
    FileWriter fw;

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
        String fileName;

        do {
            System.out.println("Name of input file: ");
//            = fs.scan.nextLine();
            fileName = "mp3.in";
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
        fs.fw = new FileWriter(fileName + ".out");

        //enter function here where all the nodes will be loaded into the current file
        for (int i = 0; i < commands.size(); i++) {
            fs.waiting(commands.get(i));
        }
        fs.fw.close();

    }

    private void initialize() {
        currentDir = "/root";
        absolutePath = "/root";
        Node root = tree.addNode("root", null, false);
    }

    private void waiting(String command) throws IOException {
        boolean flag = false;
        System.out.println("");
//        command = scan.nextLine();
        System.out.print("user@root: " + command);
        System.out.println("");

        String rem = "";

        String[] args = command.split(" ");

        if (args[0].equals("cd")) {
            if (args.length != 2) {
                System.out.println("usage: cd <targetDirectory>");
                fw.write("usage: cd <targetDirectory>");
            } else {
                cdDirectory(args[1]);
            }
        } else if (args[0].equals("ls")) {
            if (args.length == 1) {
                tree.displayChildren(tree.getNode(currentDir.replaceAll("/", "")));
            } else if (args.length != 2) {
                System.out.println("usage: ls <targetDirectory> || ls <regex>");
                fw.write(("usage: ls <targetDirectory> || ls <regex>"));
            } else {
                listDirFiles(args[1]);
            }
        } else if (args[0].equals("mkdir")) {
            if (args.length < 2) {
                System.out.println("usage: mkdir <directory name>");
                fw.write("usage: ls <targetDirectory> || ls <regex>");
            } else {
                for (int i = 1; i < args.length; i++) {
                    makeDir(args[i]);
                }
            }
        } else if (args[0].equals("rm")) {
            if (args.length < 2) {
                System.out.println("usage: rm <targetDirectory> || rm <targetFile>");
                fw.write("usage: rm <targetDirectory> || rm <targetFile>");
            } else {
                for (int i = 1; i < args.length; i++) {
                    removeDirFiles(args[i]);
                }
            }
        } else if (args[0].equals(">")) {
            if (args.length != 2) {
                System.out.println("usage: > <filename>");
                fw.write("usage: > <filename>");
            } else {
                overWriteFile(args[1]);
            }
        } else if (args[0].equals(">>")) {
            if (args.length != 2) {
                System.out.println("usage: >> <filename>");
                fw.write(("usage: >> <filename>"));
            } else {
                appendFile(args[1]);
            }
        } else if (args[0].equals("edit")) {
            if (args.length != 2) {
                System.out.println("usage: edit <filename>");
                fw.write("usage: edit <filename>");
            } else {
                editFile(args[1]);
            }
        } else if (args[0].equals("show")) {
            if (args.length != 2) {
                System.out.println("usage: show <filename>");
                fw.write("usage: show <filename>");
            } else {
                Node parent = tree.getNode(currentDir.replaceAll("/", ""));
                Node child = tree.getNode2(parent, args[1]);
                if (tree.isChild(parent, child)) {
                    System.out.println(child.getContent());
                } else {
                    System.out.println("File does not exist.");
                    fw.write(args[1] + " does not exist.");
                }
            }
        } else if (args[0].equals("rn")) {
            if (args.length != 3) {
                System.out.println("usage: rn <source_filename> <target_filename>");
                fw.write("usage: rn <source_filename> <target_filename>");
            } else {
                renameDirFiles(args[1], args[2]);
            }
        } else if (args[0].equals("mv")) {
            if (args.length != 3) {
                System.out.println("usage: mv <source_file/source_directory> <target_file/target_directory>");
                fw.write("usage: mv <source_file/source_directory> <target_file/target_directory>");
            } else {
                moveDirFiles(args[1], args[2]);
            }
        } else if (args[0].equals("whereis")) {
            if (args.length != 2) {
                System.out.println("usage: whereis <filename>");
                fw.write("usage: whereis <filename>");
            } else {
                findDirFiles(args[1]);
            }
        } else if (args[0].equals("cp")) {
            if (args.length != 3) {
                System.out.println("usage: cp <source> <target>");
            } else {
                copyDirFiles(args[1], args[2]);
            }
        }

        checkCurDir();
        pathStatus();

    }

    private void pathStatus() {
        System.out.println("");
        System.out.println("Absolute Path: " + absolutePath);
        System.out.println("Current Directory: " + currentDir);
    }

    private void cdDirectory(String arg) throws IOException {
        if (arg.contains("../")) {
            while (arg.contains("../")) {
                goUp();
                arg = arg.substring(3, arg.length());
            }
            currentDir = "/" + absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
            if (!currentDir.equals("/root")) {
                absolutePath = "/root" + currentDir;
            } else {
                absolutePath = currentDir;
            }
        }
        if (arg.equals("..")) {
            if (currentDir.equals("/root")) {
                System.out.println("Already in root.");
            } else {
                goUp();
            }
        } else if (arg.contains("/root")) { //absolute value
            absolutePath = arg;
            currentDir = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
        } else if (tree.isConnected(currentDir.replaceAll("/", ""), arg)) {

            absolutePath = (absolutePath + "/" + arg);
            currentDir = "/" + arg;
            flag = true;

        } else if (!tree.isConnected(currentDir.replaceAll("/", ""), arg)) {
            if (!arg.equals("..")) {
                noSuchFileExists(arg);
            }
        }
    }

    private void goUp() {
        if (!currentDir.equals("/root")) {
            String rem = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
            System.out.println(rem);
            rem = "/" + rem;
            absolutePath = absolutePath.replace(rem, "");
            currentDir = absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
        }
    }

    private void listDirFiles(String arg) throws IOException {
        if (!arg.contains("*")) {
            arg = arg.substring(arg.lastIndexOf("/") + 1);
            tree.displayChildren(tree.getNode(arg));
        } else//regular expression
        {
            if (arg.startsWith("*")) {
                if (arg.endsWith("*")) {
                    for (int i = 0; i < tree.nodes.size(); i++) {
                        if (!tree.nodes.get(i).data.endsWith(arg.replace("*", "")) && !tree.nodes.get(i).data.startsWith(arg.replace("*", "")) && tree.nodes.get(i).data.contains(arg.replace("*", ""))) {
                            System.out.println(tree.nodes.get(i).data);
                            fw.write(tree.nodes.get(i).data);
                        }
                    }
                } else {
                    for (int i = 0; i < tree.nodes.size(); i++) {
                        if (tree.nodes.get(i).data.startsWith(arg.replace("*", ""))) {
                            System.out.println(tree.nodes.get(i).data);
                            fw.write(tree.nodes.get(i).data);
                        }
                    }
                }
            } else if (arg.endsWith("*")) {
                for (int i = 0; i < tree.nodes.size(); i++) {
                    if (tree.nodes.get(i).data.endsWith(arg.replace("*", ""))) {
                        System.out.println(tree.nodes.get(i).data);
                        fw.write(tree.nodes.get(i).data);
                    }
                }
            }
        }
    }

    private void makeDir(String arg) throws IOException {
        String par = currentDir.replaceAll("/", "");
        if (arg.contains("../")) {
            while (arg.contains("../")) {
//                goUp();
                arg = arg.substring(3, arg.length());
            }
        }
        System.out.println(arg);
        if (tree.isConnected(par, arg)) {
            System.out.println("");
            alreadyExistsPrint(arg);
        } else if (tree.isConnected(par, arg)) {
            System.out.println("");
            alreadyExistsPrint(arg);

        } else if (arg.contains("/")) {
            String finalDirName = arg.substring(arg.lastIndexOf("/"));
            String parent = (arg.replace(finalDirName, "")).substring((arg.replace(finalDirName, "")).lastIndexOf("/") + 1);
            if (tree.isConnected(parent, finalDirName)) {
                alreadyExistsPrint(arg);
            } else {
                tree.addNode(finalDirName.replaceAll("/", ""), tree.getNode(parent), false);
            }
        } else if (arg.contains("/")) {
            String parent = "";
            for (int i = 0; i < arg.length(); i++) {
                if (arg.charAt(i) == '/') {
                    break;
                } else {
                    parent += arg.charAt(i);
                }
            }
            Node p = tree.getNode(parent);
            if (tree.isConnected(parent, arg.substring(arg.lastIndexOf("/")))) {
                tree.addNode(p, tree.getNode(parent), false);
            }
        } else {
            tree.addNode(arg, tree.getNode(currentDir.replaceAll("/", "")), false);
        }
    }

    private void removeDirFiles(String arg) throws IOException {
        if (arg.contains("/root")) {
            String finalDirName = arg.substring(arg.lastIndexOf("/") + 1);
            tree.removeNode(tree.getNode(finalDirName));
        } else if (arg.contains("/")) {
            String parent = "";
            for (int i = 0; i < arg.length(); i++) {
                if (arg.charAt(i) == '/') {
                    break;
                } else {
                    parent += arg.charAt(i);
                }
            }
            Node p = tree.getNode(parent);
            if (tree.isConnected(parent, arg.substring(arg.lastIndexOf("/")))) {
                Node tmp = tree.getNode2(p, arg.substring(arg.lastIndexOf("/")));
                tree.removeNode(tmp);
            }
        } else if (!tree.isChild(tree.getNode(currentDir.replaceAll("/", "")), tree.getNode(arg))) {

            noSuchFileExists(arg);
        } else {
            tree.removeNode(tree.getNode(arg));
        }
    }

    private void overWriteFile(String arg) throws IOException {
        String fileName = "";
        if (arg.contains("/")) {
            //absolute value code here
        } else {
            fileName = arg;
        }
//            String fileName = (command.substring(command.lastIndexOf(" ") + 1, command.length()));//absolute value
        System.out.println(fileName);

        String par = currentDir.replaceAll("/", "");
        Node parent = tree.getNode(par);
        if (tree.isConnected(par, fileName)) {
            alreadyExistsPrint(arg);
        } else {
            tmp = tree.addNode(fileName, parent, true);

        }

        JFrame frame = new JFrame();
        frame.setSize(700, 600);
        JPanel panel = new JPanel();
        JTextArea tf = new JTextArea();
        tf.setBackground(Color.LIGHT_GRAY);
        tf.setCaretPosition(0);

        frame.add(panel.add(tf));
        frame.setTitle("Overwriting " + fileName);

        frame.show();
        String output;

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                tmp.setContent(tf.getText());
            }
        });
    }

    private void appendFile(String arg) throws IOException {
        String fileName = "";
        if (arg.contains("/")) {
        } else {
            fileName = arg;
        }
        System.out.println(fileName);

        String par = currentDir.replaceAll("/", "");
        Node parent = tree.getNode(par);
        if (!tree.isConnected(par, fileName)) {
            noSuchFileExists(arg);

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
        frame2.setTitle("Append to " + fileName);

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

    private void editFile(String arg) throws IOException {

        String fileName = "";
        if (arg.contains("/")) {
        } else {
            fileName = arg;
        }
        System.out.println(fileName);

        String par = currentDir.replaceAll("/", "");
        Node parent = tree.getNode(par);
        if (!tree.isConnected(par, fileName)) {
            noSuchFileExists(arg);

        } else {
            Node tmp = tree.getNode2(parent, fileName);
            JFrame frame3 = new JFrame();
            frame3.setSize(700, 600);
            JPanel panel3 = new JPanel();
            JTextArea tf3 = new JTextArea();
            tf3.setBackground(Color.LIGHT_GRAY);
            tf3.setText(tmp.getContent());

            frame3.add(panel3.add(tf3));
            frame3.setTitle("Edit " + fileName);

            frame3.show();
            String output;

            frame3.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    tmp.setContent(tf3.getText());
                }

            });
        }

    }

    private void renameDirFiles(String source, String target) throws IOException {
        if (tree.isChild(tree.getNode(currentDir.replaceAll("/", "")), tree.getNode(source))) {
            Node tmp = tree.getNode(source);
            tmp.setValue(target);
        } else {
            noSuchFileExists(source);
        }
    }

    private void moveDirFiles(String source, String target) {
        Node child = tree.getNode(source);
        Node oldParent = child.getParent();
        oldParent.children.remove(child);
        Node newParent = tree.getNode(target);
        child.setParent(newParent);
        newParent.children.add(child);
    }

    private void findDirFiles(String arg) {
        tree.search(arg);
    }

    private void alreadyExistsPrint(String arg) {
        System.out.println(arg + " already exists.");
    }

    private void noSuchFileExists(String arg) {
        System.out.println(arg + ": no such file or directory");
    }

    private void checkCurDir() {
        if (!currentDir.startsWith("/")) {
            currentDir = "/" + currentDir;
        }
    }

    private void copyDirFiles(String source, String target) {
        
    }
}

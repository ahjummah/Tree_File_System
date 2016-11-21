package firstPhase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jessa
 */
public class Tree_1 {

    ArrayList<Node<Object>> nodes = new ArrayList<Node<Object>>();

    public static void main(String[] args) {
        Tree_1 tree = new Tree_1();

        Node node1 = tree.addNode("Node 1", null, false);
        Node node2 = tree.addNode("Node 2", node1, false);
        Node node3 = tree.addNode("Node 3", node1, false);
        Node node4 = tree.addNode("Node 4", node3, true);
        Node node5 = tree.addNode("Node 5", node2, true);
        Node node6 = tree.addNode("Node 6", node2, true);
        Node node7 = tree.addNode("Node 7", node2, true);

//        System.out.println("Tree size: " + tree.nodes.size());
        tree.display();
        tree.removeNode(node3);
//        tree.removeNode(node2);

        tree.display();
    }

    /*
        @params
        Object data is the name of the node
        Node parent is the parent of the new node added
        boolean isFile is the indicator of the file type
    
     */
    public boolean isExist(String data) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).data.equals(data)) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnected(String parent, String child) {

        for (int i = 0; i < nodes.size(); i++) {
            Node tmp = nodes.get(i);
            if (tmp.data.equals(parent)) {
                for (int j = 0; j < tmp.children.size(); j++) {
                    Node tmpChild = (Node) tmp.children.get(j);
                    if (tmpChild.data.equals(child)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isChild(Node parent, Node child) {
        if (parent.children.contains(child)) {
            return true;
        }
        return false;

    }

    public Node<Object> addNode(Object data, Node parent, boolean isFile) {

        Node<Object> node = new Node<Object>(data);
        nodes.add(node);
        node.isFile = isFile;
        if (parent != null) {
            parent.children.add(node);
            node.setParent(parent);
        }
        return node;
    }

    public void removeNode(Node node) {
//        System.out.println("\nRemoving..." + node.getValue());
        while (!node.children.isEmpty()) {

            for (int i = 0; i < node.children.size(); i++) {
                Node tmp = (Node) node.children.get(i);

//                System.out.println(tmp.getValue() + " children: " + tmp.children.size());

                for (int j = 0; j < tmp.children.size(); j++) {
                    Node child = (Node) tmp.children.get(j);
                    nodes.remove(child);
                }
                nodes.remove(tmp);

            }
            node.children.clear();

        }
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).children.remove(node);
        }
        
        nodes.remove(node);
    }

    public Node getNode(Object data) {
        for (int i = 0; i < nodes.size(); i++) {
            Node tmp = nodes.get(i);
            if (tmp.getValue().equals(data)) {
                return tmp;
            }
        }
        return null;
    }

    public boolean searchNode(Node node) {
        System.out.println("\nSearching...");
        boolean doesExist = false;
        if (nodes.contains(node)) {
            System.out.println("Name: " + node.getValue());
            System.out.print("File Type: ");
            if (node.isFile == true) {
                System.out.println("File");
            } else {
                System.out.println("File Folder");
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            System.out.println("Date Created: " + df.format(node.dateCreated));
            doesExist = true;
        } else {
            System.out.println("File does not exist.");
        }
        return doesExist;

    }

    public void display() {
        System.out.println("/***************DISPLAY*******************/");
        if (nodes.isEmpty()) {
//            System.out.println("There are no nodes.");
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node<Object> node = nodes.get(i);
            System.out.println("");
            System.out.println("Node: " + nodes.get(i).getValue()+": ");
            if (!node.children.isEmpty()) {
                System.out.println("With " + node.children.size() + " children");
                for (int j = 0; j < node.children.size(); j++) {
                    System.out.println(node.children.get(j).getValue());
                }
            }
        }
    }

    void displayChildren(Node node) {
        for (int i = 0; i < node.children.size(); i++) {
            Node tmp = (Node) node.children.get(i);
            System.out.println(tmp.data);
        }
//        for (int i = 0; i < nodes.size(); i++) {
//            Node tmp = nodes.get(i);
//            if (tmp.data.equals(currentPath)) {
//                for (int j = 0; j < tmp.children.size(); j++) {
//                    Node tmpChild = (Node) tmp.children.get(j);
//                    System.out.println(tmpChild.getValue());
//                }
//            }
//        }

    }

}

class Node<Object> {

    Object data = null;
    ArrayList<Node<Object>> children = new ArrayList<Node<Object>>();
    Node parent = null;
    boolean isFile;
    Date dateCreated;
    Date dateModified;

    Node(Object data) {
        this.data = data;

        Date dateobj = new Date();
        dateCreated = dateobj;
    }

    Node(Object data, ArrayList<Node<Object>> children) {
        this.data = data;
        this.children = children;
    }

    public void setParent(Node<Object> parent) {
        this.parent = parent;
    }

    public Object getValue() {
        return this.data;
    }

    public Node getParent() {
        return this.parent;
    }

    public boolean isFile() {
        return this.isFile;
    }

    public boolean isRoot() {
        if (this.parent == null) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList getChildren() {
        return this.children;
    }

}

class Commands {

    String command;

//    public void mkdir(){
//        System.out.println("Making directory...");
//    }
}

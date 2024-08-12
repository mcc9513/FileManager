package cats;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;





public class GUI {
    private FileManager fileManager;
    private JTextArea displayArea;

    public GUI(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Simple File Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        frame.add(panel, BorderLayout.WEST);

        // Create a JTextArea to display the contents
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        JButton displayButton = new JButton("Display Contents");
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> contents = fileManager.displayDirectoryContents();
                if (contents.isEmpty()) {
                    displayArea.setText("No files found in the directory.");
                } else {
                    displayArea.setText(""); // Clear previous contents
                    for (String line : contents) {
                        displayArea.append(line + "\n");
                    }
                }
            }
        });
        panel.add(displayButton);
        JButton displayFileContentsButton = new JButton("Display File Contents");
        displayFileContentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("Enter file name to display contents:");
                if (fileName != null && !fileName.trim().isEmpty()) {
                    String fileContents = fileManager.readFileContents(fileName);
                    JOptionPane.showMessageDialog(null, fileContents, "File Contents", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "File name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(displayFileContentsButton);

        JButton copyButton = new JButton("Copy File");
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sourceFile;
                while (true) {
                    sourceFile = JOptionPane.showInputDialog("Enter source file name:");
                    if (sourceFile == null || sourceFile.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Copy operation canceled.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Path sourcePath = fileManager.getPath(sourceFile);
                    if (Files.exists(sourcePath)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "File not found. Please enter an existing file name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                String targetFile = JOptionPane.showInputDialog("Enter name of file copy:");
                if (targetFile == null || targetFile.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Copy operation canceled or no name for file copy provided.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                fileManager.copyFile(sourceFile, targetFile);
            }
        });
        panel.add(copyButton);

        JButton moveButton = new JButton("Move File");
        moveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sourceFile;
                while (true) {
                    sourceFile = JOptionPane.showInputDialog("Enter source file name:");
                    if (sourceFile == null || sourceFile.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Move operation canceled.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Path sourcePath = fileManager.getPath(sourceFile);
                    if (Files.exists(sourcePath)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "File not found. Please enter an existing file name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                String targetFile = JOptionPane.showInputDialog("Enter target file name:");
                if (targetFile == null || targetFile.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Move operation canceled or no target file name provided.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                fileManager.moveFile(sourceFile, targetFile);
            }
        });
        panel.add(moveButton);


        JButton deleteFileButton = new JButton("Delete File");
        deleteFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName;
                while (true) {
                    fileName = JOptionPane.showInputDialog("Enter file name to delete:");
                    if (fileName == null || fileName.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Delete operation canceled.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Path filePath = fileManager.getPath(fileName);
                    if (Files.exists(filePath)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "File not found. Please enter an existing file name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                fileManager.deleteFile(fileName);
            }
        });
        panel.add(deleteFileButton);


        JButton createDirButton = new JButton("Create Directory");
        createDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dirName = JOptionPane.showInputDialog("Enter directory name:");
                fileManager.createDirectory(dirName);
            }
        });
        panel.add(createDirButton);

        JButton deleteDirButton = new JButton("Delete Directory");
        deleteDirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dirName;
                while (true) {
                    dirName = JOptionPane.showInputDialog("Enter directory name to delete:");
                    if (dirName == null || dirName.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Delete operation canceled.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Path dirPath = fileManager.getPath(dirName);
                    if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Directory not found. Please enter an existing directory name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                fileManager.deleteDirectory(dirName);
            }
        });
        panel.add(deleteDirButton);


        JButton searchButton = new JButton("Search Files");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query;
                while (true) {
                    query = JOptionPane.showInputDialog("Enter file name or extension to search:");
                    if (query == null || query.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Search operation canceled.", "Operation Canceled", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Path filePath = fileManager.getPath(query);
                    if (Files.exists(filePath)) {
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "File not found. Please enter an existing file name or extension.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                List<String> results = fileManager.searchFiles(query);
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No files found matching the query.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    displayArea.setText(""); // Clear previous contents
                    for (String result : results) {
                        displayArea.append(result + "\n");
                    }
                }
            }
        });
        panel.add(searchButton);

    }
}

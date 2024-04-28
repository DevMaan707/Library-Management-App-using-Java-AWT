import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LibraryManagementApp extends JFrame {
    private JTabbedPane tabbedPane;
    private String fileName;

    public LibraryManagementApp() {
        setTitle("Library Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mainHeading = new JLabel("Library");
        mainHeading.setFont(new Font("Arial", Font.BOLD, 24));
        homePanel.add(mainHeading);

        JLabel featuredBooksLabel = new JLabel("Featured Books");
        featuredBooksLabel.setFont(new Font("Arial", Font.BOLD, 18));
        homePanel.add(featuredBooksLabel);

        JPanel bookContainerPanel = new JPanel();
        bookContainerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10)); 
        bookContainerPanel.setPreferredSize(new Dimension(780, 300)); 

        // Fetch book data from the database and create book containers without delete buttons
        fetchBooksFromDatabase(bookContainerPanel, false);

        homePanel.add(bookContainerPanel);

        JPanel addPanel = new JPanel();
        JTextField bookNameField = new JTextField(20);
        JButton uploadButton = new JButton("Upload Image");
        JButton addButton = new JButton("Add this book"); 
        addPanel.add(new JLabel("Book Name:"));
        addPanel.add(bookNameField);
        addPanel.add(uploadButton);
        addPanel.add(addButton);

        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    fileName = selectedFile.getAbsolutePath();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookName = bookNameField.getText();
                if (bookName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a book name.");
                    return;
                }

                byte[] fileBytes = null;
                try {
                    fileBytes = Files.readAllBytes(Paths.get(fileName)); 
                } catch (IOException ex) {
                    System.out.println("Error reading image file: " + ex.getMessage());
                    return;
                }

                DataBase db = new DataBase();
                db.addBook(bookName, fileBytes);

                // Update the displayed books after adding a new book
                fetchBooksFromDatabase(bookContainerPanel, false);
                tabbedPane.setSelectedIndex(0); // Switch to the Home tab
            }
        });

        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10)); 

        // Fetch book data from the database and create delete book containers
        fetchBooksFromDatabase(deletePanel, true);

        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Add Books", addPanel);
        tabbedPane.addTab("Delete Books", deletePanel);

        add(tabbedPane);
        setVisible(true);
    }

    private void fetchBooksFromDatabase(JPanel panel, boolean includeDeleteButton) {
        // Connect to the database and fetch book data
        try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "manager")) {
            String sql = "SELECT name, pic FROM books";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String bookName = rs.getString("name");
                    byte[] imageBytes = rs.getBytes("pic");
                    ImageIcon bookImage = new ImageIcon(imageBytes);
                    JPanel bookContainer = createBookContainer(bookImage, bookName, includeDeleteButton);
                    panel.add(bookContainer);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
        }
    }

    private JPanel createBookContainer(ImageIcon bookImage, String bookName, boolean hasDeleteButton) {
        JPanel bookContainer = new JPanel();
        bookContainer.setLayout(new BorderLayout());
        bookContainer.setPreferredSize(new Dimension(150, 280)); 
        bookContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Adjust image size to fit in the container
        Image img = bookImage.getImage().getScaledInstance(150, 210, Image.SCALE_SMOOTH);
        ImageIcon scaledImage = new ImageIcon(img);
        JLabel imageLabel = new JLabel(scaledImage);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookContainer.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(bookName);
        nameLabel.setPreferredSize(new Dimension(150, 70)); 
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookContainer.add(nameLabel, BorderLayout.SOUTH);

        if (hasDeleteButton) {
            JButton deleteButton = new JButton("Delete this book");
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Implement book deletion functionality here
                    System.out.println("Delete button clicked for book: " + bookName);
                }
            });
            bookContainer.add(deleteButton, BorderLayout.SOUTH);
        }

        return bookContainer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementApp());
    }
}

class DataBase {
    public void addBook(String name, byte[] imageBytes) {
        try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "manager")) {
            String sql = "INSERT INTO books (name, pic) VALUES (?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setBytes(2, imageBytes);
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Book added successfully.");
                } else {
                    System.out.println("Failed to add book.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }
}

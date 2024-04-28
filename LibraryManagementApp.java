import java.awt.*;
import javax.swing.*;

public class LibraryManagementApp extends JFrame {
    private JTabbedPane tabbedPane;

    public LibraryManagementApp() {
        setTitle("Library Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left

        JLabel mainHeading = new JLabel("Library");
        mainHeading.setFont(new Font("Arial", Font.BOLD, 24));
        homePanel.add(mainHeading);

        JLabel featuredBooksLabel = new JLabel("Featured Books");
        featuredBooksLabel.setFont(new Font("Arial", Font.BOLD, 18));
        homePanel.add(featuredBooksLabel);

        JPanel bookContainerPanel = new JPanel();
        bookContainerPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10)); // Flow layout with horizontal gap
        bookContainerPanel.setPreferredSize(new Dimension(780, 300)); // Adjust width as needed

        String[] bookNames = {"Book 1", "Book 2", "Book 3"};
        ImageIcon[] bookImages = {new ImageIcon("book1.jpg"), new ImageIcon("book2.jpg"), new ImageIcon("book3.jpg")};

        for (int i = 0; i < bookNames.length; i++) {
            JPanel bookContainer = createBookContainer(bookImages[i], bookNames[i], false); // No delete button in Home tab
            bookContainerPanel.add(bookContainer);
        }

        homePanel.add(bookContainerPanel);

        JPanel addPanel = new JPanel();
        JTextField bookNameField = new JTextField(20);
        JButton uploadButton = new JButton("Upload Image");
        JButton addButton = new JButton("Add this book"); // Added "Add this book" button
        addPanel.add(new JLabel("Book Name:"));
        addPanel.add(bookNameField);
        addPanel.add(uploadButton);
        addPanel.add(addButton);

        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10)); // Flow layout with horizontal gap

        for (int i = 0; i < bookNames.length; i++) {
            JPanel deleteContainer = createBookContainer(bookImages[i], bookNames[i], true); // With delete button in Delete Books tab
            JButton deleteButton = new JButton("Delete this book");
            deleteContainer.add(deleteButton, BorderLayout.SOUTH);
            deletePanel.add(deleteContainer);
        }

        tabbedPane.addTab("Home", homePanel);
        tabbedPane.addTab("Add Books", addPanel);
        tabbedPane.addTab("Delete Books", deletePanel);

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createBookContainer(ImageIcon bookImage, String bookName, boolean hasDeleteButton) {
        JPanel bookContainer = new JPanel();
        bookContainer.setLayout(new BorderLayout());
        bookContainer.setPreferredSize(new Dimension(150, 280)); // Adjust dimensions as needed
        bookContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border

        JLabel imageLabel = new JLabel(bookImage);
        imageLabel.setPreferredSize(new Dimension(150, 210)); // 75% height for image
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookContainer.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(bookName);
        nameLabel.setPreferredSize(new Dimension(150, 70)); // 25% height for name
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookContainer.add(nameLabel, BorderLayout.SOUTH);

        if (hasDeleteButton) {
            JButton deleteButton = new JButton("Delete this book");
            bookContainer.add(deleteButton, BorderLayout.SOUTH);
        }

        return bookContainer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementApp());
    }
}

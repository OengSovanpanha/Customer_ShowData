import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerShowSwing extends JFrame {
    private List<Customer> customers;
    private int currentIndex;
    private final JTextField idField;
    private final JTextField lastNameField;
    private final JTextField firstNameField;
    private final JTextField phoneField;

    record Customer(int customerId, String lastName, String firstName, String phone) {
    }

    public CustomerShowSwing() {
        setTitle("Customer Viewer");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        JLabel idLabel = new JLabel("Customer ID:");
        idField = new JTextField();
        idField.setEditable(false);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        lastNameField.setEditable(false);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        firstNameField.setEditable(false);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        phoneField.setEditable(false);

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousRecord();
            }
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextRecord();
            }
        });

        add(idLabel);
        add(idField);
        add(lastNameLabel);
        add(lastNameField);
        add(firstNameLabel);
        add(firstNameField);
        add(phoneLabel);
        add(phoneField);
        add(previousButton);
        add(nextButton);

        loadCustomerData();
        showRecord(0);

        setVisible(true);
    }

    private void loadCustomerData() {
        customers = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/TestJBDC";
        String user = "root";
        String password = "123451";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int customerId = rs.getInt("customer_id");
                String lastName = rs.getString("customer_last_name");
                String firstName = rs.getString("customer_first_name");
                String phone = rs.getString("customer_phone");

                customers.add(new Customer(customerId, lastName, firstName, phone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load customer data from database.");
        }

        currentIndex = 0;
    }

    private void showRecord(int index) {
        if (index >= 0 && index < customers.size()) {
            Customer customer = customers.get(index);
            idField.setText(String.valueOf(customer.customerId()));
            lastNameField.setText(customer.lastName());
            firstNameField.setText(customer.firstName());
            phoneField.setText(customer.phone());
        }
    }

    private void showNextRecord() {
        if (currentIndex < customers.size() - 1) {
            currentIndex++;
            showRecord(currentIndex);
        } else {
            JOptionPane.showMessageDialog(this, "This is the last record.");
        }
    }

    private void showPreviousRecord() {
        if (currentIndex > 0) {
            currentIndex--;
            showRecord(currentIndex);
        } else {
            JOptionPane.showMessageDialog(this, "This is the first record.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerShowSwing::new);
    }
}

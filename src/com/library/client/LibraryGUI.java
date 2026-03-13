package com.library.client;  

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.library.cilent.LibraryCilent;
import com.library.common.Book;

public class LibraryGUI extends JFrame {

    // input fields 
    private final JTextField idField    = new JTextField(5);
    private final JTextField titleField = new JTextField(12);
    private final JTextField authorField= new JTextField(12);
    private final JTextField yearField  = new JTextField(5);

    // buttons
    private final JButton addBtn    = new JButton("Add");
    private final JButton viewBtn   = new JButton("View");
    private final JButton updateBtn = new JButton("Update");
    private final JButton deleteBtn = new JButton("Delete"); 

    // table + model
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Title","Author","Year"},0);
    private final JTable table = new JTable(model);

    
    private final LibraryCilent api = new LibraryCilent("127.0.0.1",5000);

    public LibraryGUI() {
        setTitle("Library System Database");
        setSize(800,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // layout setUp
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill= GridBagConstraints.HORIZONTAL;

        // ID row
        gbc.gridx=0; gbc.gridy=0;
        add(new JLabel("ID/Update/Delete :"),gbc);
        gbc.gridx=1;
        add(idField,gbc);

        // title 
        gbc.gridx=0; gbc.gridy=1;
        add(new JLabel("Title:"),gbc);
        gbc.gridx=1;
        add(titleField,gbc);

        // author 
        gbc.gridx=0; gbc.gridy=2;
        add(new JLabel("Author:"),gbc);
        gbc.gridx=1;
        add(authorField,gbc);

        // year 
        gbc.gridx=0; gbc.gridy=3;
        add(new JLabel("Year:"),gbc);
        gbc.gridx=1;
        add(yearField,gbc);

        // buttons
        gbc.gridx=0; gbc.gridy=4;
        add(addBtn,gbc);
        gbc.gridx=1;
        add(viewBtn,gbc);

        gbc.gridx=0; gbc.gridy=5;
        add(updateBtn,gbc);
        gbc.gridx=1;
        add(deleteBtn,gbc);

        // Row 6: Table
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        add(new JScrollPane(table), gbc);

        // Button actions
        addBtn.addActionListener(wrap(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String resp = api.add(title, author, year);
                msg(resp);
                refreshTable();
                clearInputsExceptId();
            } catch (Exception ex) {
                msg("ERROR|" + ex.getMessage());
            }
        }));

        viewBtn.addActionListener(wrap(e -> refreshTable()));

        updateBtn.addActionListener(wrap(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String resp = api.update(id, title, author, year);
                msg(resp);
                refreshTable();
            } catch (Exception ex) {
                msg("ERROR|" + ex.getMessage());
            }
        }));

        deleteBtn.addActionListener(wrap(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String resp = api.delete(id);
                msg(resp);
                refreshTable();
                idField.setText("");
            } catch (Exception ex) {
                msg("ERROR|" + ex.getMessage());
            }
        }));

        // Table selection → populate fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(String.valueOf(model.getValueAt(row, 0)));
                titleField.setText(String.valueOf(model.getValueAt(row, 1)));
                authorField.setText(String.valueOf(model.getValueAt(row, 2)));
                yearField.setText(String.valueOf(model.getValueAt(row, 3)));
            }
        });
    }

    // Refresh JTable with server data
    private void refreshTable() {
        try {
            List<Book> books = api.view();
            model.setRowCount(0);
            for (Book b : books) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getYear()});
            }
        } catch (Exception ex) {
            msg("ERROR|" + ex.getMessage());
        }
    }

    private void clearInputsExceptId() {
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
    }

    private void msg(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private ActionListener wrap(ActionListener al) {
        return e -> {
            setButtonsEnabled(false);
            try {
                al.actionPerformed(e);
            } finally {
                setButtonsEnabled(true);
            }
        };
    }

    private void setButtonsEnabled(boolean enabled) {
        addBtn.setEnabled(enabled);
        viewBtn.setEnabled(enabled);
        updateBtn.setEnabled(enabled);
        deleteBtn.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}

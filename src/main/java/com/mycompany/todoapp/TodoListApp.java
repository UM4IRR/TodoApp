package com.mycompany.todoapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// Main Application Class
public class TodoListApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListGUI().setVisible(true));
    }
}

// Base class for a Task
class Task {
    private String title;
    private String description;
    private String deadline;
    private boolean isCompleted;

    public Task(String title, String description, String deadline) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void toggleCompleted() {
        isCompleted = !isCompleted;
    }
}

// GUI Class
class TodoListGUI extends JFrame {
    private ArrayList<Task> tasks;
    private DefaultTableModel tableModel;
    private JTable taskTable;

    public TodoListGUI() {
        tasks = new ArrayList<>();

        setTitle("TODO List App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel headerLabel = new JLabel("TODO List Application");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Task Table Panel
        String[] columnNames = {"Title", "Description", "Deadline", "Completed"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only allow editing the "Completed" column directly
            }
        };
        taskTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Task"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Title:");
        JTextField taskField = new JTextField(15);
        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField(15);
        JLabel deadlineLabel = new JLabel("Deadline:");
        JTextField deadlineField = new JTextField(10);
        JButton addTaskButton = new JButton("Add Task");

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(titleLabel, gbc);
        gbc.gridx = 1; inputPanel.add(taskField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(descLabel, gbc);
        gbc.gridx = 1; inputPanel.add(descField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(deadlineLabel, gbc);
        gbc.gridx = 1; inputPanel.add(deadlineField, gbc);

        gbc.gridx = 1; gbc.gridy = 3; inputPanel.add(addTaskButton, gbc);

        controlPanel.add(inputPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Manage Tasks"));

        JButton deleteTaskButton = new JButton("Delete Task");
        JButton editTaskButton = new JButton("Edit Task");
        JButton toggleTaskButton = new JButton("Mark Completed");

        buttonPanel.add(deleteTaskButton);
        buttonPanel.add(editTaskButton);
        buttonPanel.add(toggleTaskButton);

        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.SOUTH);

        // Button Actions
        addTaskButton.addActionListener(e -> {
            String taskTitle = taskField.getText().trim();
            String taskDescription = descField.getText().trim();
            String taskDeadline = deadlineField.getText().trim();
            if (!taskTitle.isEmpty() && !taskDeadline.isEmpty()) {
                Task newTask = new Task(taskTitle, taskDescription, taskDeadline);
                tasks.add(newTask);
                tableModel.addRow(new Object[]{taskTitle, taskDescription, taskDeadline, false});
                taskField.setText("");
                descField.setText("");
                deadlineField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Title and deadline cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteTaskButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                tasks.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        editTaskButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                Task selectedTask = tasks.get(selectedRow);
                JTextField newTitleField = new JTextField(selectedTask.getTitle());
                JTextField newDescField = new JTextField(selectedTask.getDescription());
                JTextField newDeadlineField = new JTextField(selectedTask.getDeadline());

                JPanel editPanel = new JPanel(new GridLayout(3, 2));
                editPanel.add(new JLabel("Title:"));
                editPanel.add(newTitleField);
                editPanel.add(new JLabel("Description:"));
                editPanel.add(newDescField);
                editPanel.add(new JLabel("Deadline:"));
                editPanel.add(newDeadlineField);

                int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String updatedTitle = newTitleField.getText().trim();
                    String updatedDescription = newDescField.getText().trim();
                    String updatedDeadline = newDeadlineField.getText().trim();

                    if (!updatedTitle.isEmpty() && !updatedDeadline.isEmpty()) {
                        selectedTask.setTitle(updatedTitle);
                        selectedTask.setDescription(updatedDescription);
                        selectedTask.setDeadline(updatedDeadline);

                        tableModel.setValueAt(updatedTitle, selectedRow, 0);
                        tableModel.setValueAt(updatedDescription, selectedRow, 1);
                        tableModel.setValueAt(updatedDeadline, selectedRow, 2);
                    } else {
                        JOptionPane.showMessageDialog(this, "Title and deadline cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        toggleTaskButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                Task selectedTask = tasks.get(selectedRow);
                selectedTask.toggleCompleted();
                tableModel.setValueAt(selectedTask.isCompleted(), selectedRow, 3);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to mark as completed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

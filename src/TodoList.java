import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodoList extends JFrame implements ActionListener {

    private JTextField taskInput;
    private JTextArea taskList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton saveButton;
    private List<String> tasks;

    public TodoList() {
        super("Todo List");

        tasks = new ArrayList<>();

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Task Input Panel
        JPanel taskInputPanel = new JPanel(new BorderLayout());
        taskInputPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel taskLabel = new JLabel("Task:");
        taskInput = new JTextField(20);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        taskInputPanel.add(taskLabel, BorderLayout.WEST);
        taskInputPanel.add(taskInput, BorderLayout.CENTER);
        taskInputPanel.add(addButton, BorderLayout.EAST);

        // Task List Panel
        JPanel taskListPanel = new JPanel(new BorderLayout());
        taskListPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel taskListLabel = new JLabel("Tasks:");
        taskList = new JTextArea();
        taskList.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        taskListPanel.add(taskListLabel, BorderLayout.NORTH);
        taskListPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(saveButton);

        // Add Panels to Main Panel
        mainPanel.add(taskInputPanel, BorderLayout.NORTH);
        mainPanel.add(taskListPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add Main Panel to Frame
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addTask();
        } else if (e.getSource() == deleteButton) {
            deleteTask();
        } else if (e.getSource() == updateButton) {
            updateTask();
        } else if (e.getSource() == saveButton) {
            saveTasks();
        }
    }

    private void addTask() {
        String task = taskInput.getText();
        if (task != null && !task.trim().isEmpty()) {
            tasks.add(task);
            updateTaskList();
            taskInput.setText("");
        }
    }
    private void deleteTask() {
        int start = taskList.getSelectionStart();
        int end = taskList.getSelectionEnd();
        int index = taskList.getText().substring(0, end).split("\n").length - 1;

        if (index != -1) {
            tasks.remove(index);
            updateTaskList();
        }
    }

    private void updateTask() {
        int start = taskList.getSelectionStart();
        int end = taskList.getSelectionEnd();
        int index = taskList.getText().substring(0, end).split("\n").length - 1;

        if (index != -1) {
            String newTask = JOptionPane.showInputDialog(this, "Enter new task:", tasks.get(index));
            if (newTask != null && !newTask.trim().isEmpty()) {
                tasks.set(index, newTask);
                updateTaskList();
            }
        }
    }

    private void saveTasks() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                for (String task : tasks) {
                    LocalDateTime now = LocalDateTime.now();
                    String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    writer.write(String.format("%s\n%s\n", date, task));
                }
                JOptionPane.showMessageDialog(this, "Tasks saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTaskList() {
        taskList.setText("");
        for (String task : tasks) {
            taskList.append(task + "\n");
        }
    }

    public static void main(String[] args) {
        new TodoList();
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TodoList extends JFrame {
    private JTextField taskTextField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private ArrayList<String> tasks;
    private JTextField searchTextField;

    public TodoList() {
        super("ToDoList");

        tasks = new ArrayList<String>();
        taskListModel = new DefaultListModel<String>();
        for (String task : tasks) {
            taskListModel.addElement(task);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        getContentPane().add(mainPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        mainPanel.add(inputPanel);

        JLabel taskLabel = new JLabel("Task:");
        inputPanel.add(taskLabel);

        taskTextField = new JTextField(20);
        inputPanel.add(taskTextField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonListener());
        inputPanel.add(addButton);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        mainPanel.add(listPanel);

        JLabel listLabel = new JLabel("Tasks:");
        listPanel.add(listLabel);

        taskList = new JList<String>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        listPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        mainPanel.add(buttonPanel);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonListener());
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonListener());
        buttonPanel.add(deleteButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        mainPanel.add(searchPanel);

        JLabel searchLabel = new JLabel("Search:");
        searchPanel.add(searchLabel);

        searchTextField = new JTextField(20);
        searchPanel.add(searchTextField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());
        searchPanel.add(searchButton);

        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.X_AXIS));
        mainPanel.add(savePanel);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveButtonListener());
        savePanel.add(saveButton);

        pack();
        setVisible(true);
    }

    // 추가하는 버튼 이벤트 처리
    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String task = taskTextField.getText().trim();
            if (!task.isEmpty()) {
                tasks.add(task);
                taskListModel.addElement(task);
                taskTextField.setText("");
            }
        }
    }

    // 수정하는 버튼 이벤트 처리
    private class EditButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                String task = taskListModel.getElementAt(index);
                String newTask = JOptionPane.showInputDialog("Edit task:", task);
                if (!newTask.isEmpty()) {
                    tasks.set(index, newTask);
                    taskListModel.setElementAt(newTask, index);
                }
            }
        }
    }

    // 삭제하는 버튼 이벤트 처리
    private class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                tasks.remove(index);
                taskListModel.remove(index);
            }
        }
    }

    // 검색하는 버튼 이벤트 처리
    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String searchText = searchTextField.getText().trim();
            DefaultListModel<String> resultListModel = new DefaultListModel<String>();
            if (!searchText.isEmpty() && taskListModel != null) {
                for (int i = 0; i < taskListModel.getSize(); i++) {
                    String item = taskListModel.getElementAt(i);
                    if (item.contains(searchText)) {
                        resultListModel.addElement(item);
                    }
                }
            }
            if (resultListModel.getSize() > 0) {  // 검색 결과가 있는 경우에만 새로운 GUI를 생성하여 표시
                JList<String> resultList = new JList<String>(resultListModel);
                JScrollPane scrollPane = new JScrollPane(resultList);
                scrollPane.setPreferredSize(new Dimension(300, 300));
                JFrame searchResultFrame = new JFrame("Search Result");
                searchResultFrame.getContentPane().add(scrollPane);
                searchResultFrame.pack();
                searchResultFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(TodoList.this, "No matching tasks found!");
            }
        }
    }

    // 저장하는 버튼 이벤트 처리
    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(TodoList.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    FileWriter writer = new FileWriter(fileName);
                    for (String task : tasks) {
                        writer.write(task + "\n");
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(TodoList.this, "Tasks saved successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(TodoList.this, "Error saving tasks: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new TodoList();
    }
}

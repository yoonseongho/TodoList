import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoList extends JFrame implements ActionListener {
    private JPanel contentPane;
    private JTextField taskField;
    private JTextField dateField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JList<String> list;
    private DefaultListModel<String> listModel;

    public TodoList() {
        setTitle("Todo List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        contentPane.add(inputPanel, BorderLayout.NORTH);

        JLabel taskLabel = new JLabel("Task:");
        inputPanel.add(taskLabel);

        taskField = new JTextField();
        inputPanel.add(taskField);
        taskField.setColumns(10);

        JLabel dateLabel = new JLabel("Date (MM/dd/yyyy):");
        inputPanel.add(dateLabel);

        dateField = new JTextField();
        inputPanel.add(dateField);
        dateField.setColumns(10);

        // 추가
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        inputPanel.add(addButton);

        // 수정
        editButton = new JButton("Edit");
        editButton.addActionListener(this);
        editButton.setEnabled(false);
        inputPanel.add(editButton);

        // 삭제
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(false);
        inputPanel.add(deleteButton);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(e -> {
            if (list.getSelectedIndex() == -1) {
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else {
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                String[] selected = list.getSelectedValue().split(" - ");
                taskField.setText(selected[0]);
                dateField.setText(selected[1]);
            }
        });
        JScrollPane scrollPane = new JScrollPane(list);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TodoList frame = new TodoList();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String task = taskField.getText();
            String dateString = dateField.getText();
            if (!task.isEmpty() && !dateString.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = dateFormat.parse(dateString);
                    String formattedDate = dateFormat.format(date);
                    listModel.addElement(task + " - " + formattedDate);
                    taskField.setText("");
                    dateField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use MM/dd/yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill out both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else if (e.getSource() == editButton) {
            int selectedIndex = list.getSelectedIndex();
            String task = taskField.getText();
            String dateString = dateField.getText();
            if (!task.isEmpty() && !dateString.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = dateFormat.parse(dateString);
                    String formattedDate = dateFormat.format(date);
                    listModel.set(selectedIndex, task + " - " + formattedDate);
                    taskField.setText("");
                    dateField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use MM/dd/yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill out both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    listModel.remove(selectedIndex);
                    taskField.setText("");
                    dateField.setText("");
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            }
        }
    }

    /* public void showTodoList() {
        ArrayList<String> todoList = new ArrayList<>();
        todoList.add("Complete math homework - 03/14/2023");
        todoList.add("Buy groceries - 03/15/2023");
        todoList.add("Go for a walk - 03/16/2023");
        for (String item : todoList) {
            listModel.addElement(item);
        }
    } */
}
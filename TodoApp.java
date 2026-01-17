package REALPROJECTS;
import javax.swing.*;

public class TodoApp {
    public static void main(String[] args) {
        JFrame f = new JFrame("To-Do List");

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        list.setBounds(50, 50, 200, 100);

        JTextField t = new JTextField();
        t.setBounds(50, 160, 200, 30);

        JButton add = new JButton("Add");
        add.setBounds(50, 200, 80, 30);
        JButton del = new JButton("Delete");
        del.setBounds(170, 200, 80, 30);

        add.addActionListener(e -> {
            model.addElement(t.getText());
            t.setText("");
        });

        del.addActionListener(e -> model.remove(list.getSelectedIndex()));

        f.add(list); f.add(t); f.add(add); f.add(del);
        f.setSize(320, 300);
        f.setLayout(null);
        f.setVisible(true);
    }
}

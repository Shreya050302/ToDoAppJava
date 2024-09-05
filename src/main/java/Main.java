import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
   static MongoDatabase  todoDB;
    // create Labels and textFields for todoApp
    static JLabel taskNamelb, taskDatelb, tasktimelb;
    static JTextField taskNametf, taskDatetf, tasktimetf;
    static JButton addTaskbt, deleteTaskbt,updateTaskbt,viewTaskbt;
    public static void main(String[] args) {
 // Add maven in your project
        //Create client and establish the mongo database connection
        var client = MongoClients.create("mongodb://localhost:27017/");
        // create access the database
         todoDB = client.getDatabase("todoDB");
        //to create the collection
        //todoDB.createCollection("todoList");
        // create GUI for TodoApp using swing
        createTodoUI();
    }

    private static void createTodoUI() {
        JFrame frame = new JFrame("Todo App");
        // to initialize the UI components
        taskNamelb = new JLabel("Enter the task name");
        taskDatelb = new JLabel("Enter the task date");
        tasktimelb = new JLabel("Enter the task time");

        taskNametf = new JTextField();
        taskDatetf = new JTextField();
        tasktimetf = new JTextField();

        addTaskbt = new JButton("Add Task");
        deleteTaskbt = new JButton("Delete");
        updateTaskbt =  new JButton("update");
        viewTaskbt = new JButton("view");

        //to set the size and position of UI component
        taskNamelb.setBounds(50, 50, 150, 40);
        taskDatelb.setBounds(50, 80, 150, 40);
        tasktimelb.setBounds(50, 110, 150, 40);

        taskNametf.setBounds(180, 50, 120, 30);
        taskDatetf.setBounds(180, 80, 120, 30);
        tasktimetf.setBounds(180, 110, 120, 30);
        addTaskbt.setBounds(10, 150, 100, 30);
        deleteTaskbt.setBounds(110,150,100,30);
        updateTaskbt.setBounds(210,150,100,30);
        viewTaskbt.setBounds(310,150,100,30);


        frame.add(taskNamelb);
        frame.add(taskDatelb);
        frame.add(tasktimelb);
        frame.add(taskNametf);
        frame.add(taskDatetf);
        frame.add(tasktimetf);
        frame.add(addTaskbt);
        frame.add(deleteTaskbt);
        frame.add(updateTaskbt);
        frame.add(viewTaskbt);


        // to set the size of frame
        frame.setSize(500, 300);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setVisible(true);



        addTaskbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               insertTaskINtoDB();
            }
        });


        updateTaskbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });

        deleteTaskbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletetask();
            }
        });

        viewTaskbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewtask();
            }
        });
    }

    private static void viewtask() {
        JOptionPane.showInputDialog(null,"Enter task id to view");
        var todoCollection = todoDB.getCollection("todoList");
        BasicDBObject whereQuery= new BasicDBObject();
        whereQuery.put("taskID",394);
        FindIterable<Document> cursor = todoCollection.find(whereQuery);
        taskNametf.setText(String.valueOf(cursor.iterator().next().get("taskName")));
        taskNametf.setText(String.valueOf(cursor.iterator().next().get("taskDate")));
        taskNametf.setText(String.valueOf(cursor.iterator().next().get("taskTime")));
    }

    private static void deletetask() {
        var todoCollection = todoDB.getCollection("todoList");
        String deletetaskID=  JOptionPane.showInputDialog(null,"Enter task ID to delete the task");
         todoCollection.deleteOne(new Document("taskID",Integer.parseInt(deletetaskID)));
    }

    private static void updateTask(){
       String taskID=  JOptionPane.showInputDialog(null,"Enter the ID to update the status");
       var todoCollection  = todoDB.getCollection("todoList");

         todoCollection.updateOne(new Document("taskId",Integer.parseInt(taskID)),new Document("$set",new Document("taskStatus",true)));
    }


    private static void insertTaskINtoDB() {
        //to access the collection
        var todoCollection = todoDB.getCollection("todoList");


        // to create the document and add in collection
        var random = new Random();
        var document = new Document();
        // add data into document
        document.append("taskName", taskNametf.getText());
        document.append("taskDate", taskDatetf.getText());
        document.append("taskTime", tasktimetf.getText());
        document.append("taskStatus", false);
        document.append("taskId",random.nextInt(999));

        todoCollection.insertOne(document);

        JOptionPane.showMessageDialog(null,"Task Added Successfully");
        //clear the
        clearForm();

    }

    private static void clearForm() {
        taskDatetf.setText("");
        taskNametf.setText("");
        tasktimetf.setText("");
    }

}
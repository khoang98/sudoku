import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JComboBox;

/**
  * Description: A class which creates a frame for the user to interact with to run tests of the various sovlers and view results.
  */

public class MyFrame extends JFrame {

  public HashMap<String,Boolean> toRun = new HashMap<String,Boolean>(4);

  private JToggleButton btnEmb  = new JToggleButton("Emabrrasingly Parallel");
  private JToggleButton btnProlog = new JToggleButton("Prolog");
  private JToggleButton btnParallel = new JToggleButton("Recursive Parralel");
  private JToggleButton btnfjp = new JToggleButton("Fork-Join");

  private JLabel lblTestsToPerform= new JLabel("Select Which Tests to Perform, how many iterations of tests and difficulty");

  private JLabel lblRunTests= new JLabel("Run Tests at selected difficulty :");
  private JButton btnRunTests = new JButton("Run");

  private JTextArea testResults = new JTextArea();


  private int x=10;
  private JTextField jTextField1 = new JTextField();

  String[] choices = { "EASY","MEDIUM", "HARD"};
  private JComboBox<String> dropdown = new JComboBox(choices);

  public MyFrame(){
    setTitle("Sudoku");
    setSize(500,500);
    setLocation(new Point(300,200));
    setLayout(null);
    setResizable(false);

    initComponent();
    initEvent();
  }


  private void initComponent(){

    jTextField1.setBounds(0, 100, 50, 50);

    dropdown.setVisible(true);
    dropdown.setBounds(0, 140,50,50);

    btnEmb.setBounds(20,30, 80,25);
    btnProlog.setBounds(150,30, 80,25);
    btnfjp.setBounds(280, 30, 80, 25);
    btnParallel.setBounds(410, 30, 80, 25);

    lblTestsToPerform.setBounds(175, 10, 200, 10);

    lblRunTests.setBounds(175, 75, 200, 10);
    btnRunTests.setBounds(190, 100, 50, 50);

    toRun.put(btnEmb.getText(),false);
    toRun.put(btnProlog.getText(),false);
    toRun.put(btnfjp.getText(),false);
    toRun.put(btnParallel.getText(),false);

    testResults.setBounds(0, 200, 300, 200);


    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent itemEvent) {
        AbstractButton aButton = (AbstractButton)itemEvent.getSource();
        int state = itemEvent.getStateChange();
        String label = aButton.getText();
        if (state == ItemEvent.SELECTED) {
          toRun.put(label,true);
        }  else {
          toRun.put(label,false);
        }
      }
    };

    add(testResults);
    add(dropdown);
    add(lblTestsToPerform);
    add(btnEmb);
    add(btnfjp);
    add(btnProlog);
    add(btnParallel);
    add(btnRunTests);
    add(lblRunTests);
    add(jTextField1);
    btnEmb.addItemListener(itemListener);
    btnfjp.addItemListener(itemListener);
    btnProlog.addItemListener(itemListener);
    btnParallel.addItemListener(itemListener);

  }

  private void initEvent(){

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e){
       System.exit(1);
      }
    });

    btnRunTests.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        testResults.setText("Running Tests, Please Wait");
        runTests(e);
      }
    });
  }

  private void runTests(ActionEvent evt){
    try{

    try {
        x = Integer.parseInt(jTextField1.getText());
    } catch (NumberFormatException nfe) {
       x=5;
    }

    List<HashMap<String,Result>> listOfMaps = new ArrayList<HashMap<String,Result>>();
      for(int i =0; i< x; i++){
        HashMap<String,Result> result = SudokuTester.test(toRun.get(btnEmb.getText()), toRun.get(btnProlog.getText()),toRun.get(btnParallel.getText()),toRun.get(btnfjp.getText()), String.valueOf(dropdown.getSelectedItem()));
        listOfMaps.add(result);
      }

    


    HashMap<String,Result> r = SudokuTester.avg(listOfMaps);

    String results ="Solvers Completed an easy solve on average in : \n";
    if(r.containsKey("Baseline")){
      results= results + "The baseline Solver: " + r.get("Baseline").time + "ms \n";
    }
    if(r.containsKey("Prolog")){
      results= results + "The Prolog Solver: "+ r.get("Prolog").time + "ms \n";
    }
    if(r.containsKey("emb")){
      results= results + "The Embarrasingly parralell Solver: "+ r.get("emb").time + "ms \n";
    }
    if(r.containsKey("Recursive")){
      results= results + "The Recursive Parralell Solver: "+ r.get("Recursive").time + " ms \n";
    }
    if(r.containsKey("fjp")){
      results= results + "The Fork-Join Pool Solver: "+ r.get("fjp").time + " ms \n";
    }
    testResults.setText(results);


    }catch(Exception e){
      System.out.println(e);
      JOptionPane.showMessageDialog(null,
          e.toString(),
          "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}

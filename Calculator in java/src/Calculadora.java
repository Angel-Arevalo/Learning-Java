import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Calculadora extends JFrame implements ActionListener {
    private JTextField screen;
    private JButton[] numeros = new JButton[12]; // Modificado a 11 para incluir el punto decimal
    private JButton sumar, restar, multiplicar, dividir, igual, borrar;
    private JPanel panel;

    private double ANS;
    private boolean catched = false;
    private boolean cleanConsole = false;

    public Calculadora() {
        setTitle("Calculadora");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        screen = new JTextField();
        screen.setEditable(false);
        screen.setHorizontalAlignment(JTextField.RIGHT);
        add(screen, BorderLayout.NORTH);

        // El panel principal usa GridBagLayout
        panel = new JPanel(new GridBagLayout());
        add(panel, BorderLayout.CENTER);

        buildNumbers();
        buildActions();
    }

    public void buildNumbers() {
        JPanel buttons = new JPanel(new GridLayout(4, 3));

        for (int i = 0; i < 10; i++) {
            numeros[i] = new JButton(String.valueOf((9 - i)));
            numeros[i].addActionListener(this);
            buttons.add(numeros[i]);
        }
        numeros[10] = new JButton(".");
        numeros[10].addActionListener(this);
        buttons.add(numeros[10]);
        numeros[11] = new JButton("ANS");
        numeros[11].addActionListener(this);
        buttons.add(numeros[11]);

        // Configuración de restricciones de GridBag
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Añadir el panel de botones al panel principal con las restricciones
        panel.add(buttons, gbc);
    }

    public void buildActions() {
        // Crear un panel para los botones de acción
        JPanel actions = new JPanel(new GridLayout(3, 2)); // 3x2 grid para operaciones

        // Crear botones de operación
        sumar = new JButton("+");
        restar = new JButton("-");
        multiplicar = new JButton("*");
        dividir = new JButton("/");
        igual = new JButton("=");
        borrar = new JButton("C");

        // Añadir ActionListeners a los botones
        sumar.addActionListener(this);
        restar.addActionListener(this);
        multiplicar.addActionListener(this);
        dividir.addActionListener(this);
        igual.addActionListener(this);
        borrar.addActionListener(this);

        // Añadir botones de operación al panel de acciones
        actions.add(sumar);
        actions.add(restar);
        actions.add(multiplicar);
        actions.add(dividir);
        actions.add(igual);
        actions.add(borrar);

        // Configuración de restricciones de GridBag para el panel de acciones
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.4;  // El 36% del ancho para el panel de acciones
        gbc.weighty = 1.0;
        gbc.gridx = 1;
        gbc.gridy = 0;

        // Añadir el panel de acciones al panel principal con las restricciones
        panel.add(actions, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.charAt(0) >= '0' && comando.charAt(0) <= '9' || comando.equals(".")) {
            if(!cleanConsole) screen.setText(screen.getText() + comando);
            else {
                screen.setText("" + comando);
                cleanConsole = false;
            }
        } else if(comando.equals("+") || comando.equals("-")) {

            screen.setText(screen.getText() + " " + comando + " ");
        } else if(comando.equals("*") || comando.equals("/")) {

            screen.setText(screen.getText() + comando);
        } else if(comando.equals("=")) {
            ANS = calc(screen.getText().split(" "));
            if (!catched) {
                
                screen.setText(screen.getText() + " = "+ ANS);
            }else {
                ANS = 0;
            }
            catched = false;
            cleanConsole = true;
        }else if (comando.equals("C")) {

            screen.setText("");
            cleanConsole = false;
        }else if(comando.equals("ANS")) {

            screen.setText("" + ANS);
            cleanConsole = false;
        }
    }


    public double calc(String[] args) {
        try {
            if (args.length == 1 && args[0].equals("")) {
                throw new RuntimeException("No input provided.");
            }
            double total = 0;
            for (int i = 0; i < args.length; i++) {
                if(!args[i].equals("*") && !args[i].equals("/")) {
                    total += (i == 0 || args[i-1].equals("+")) ? readString(args[i]) : -readString(args[i]);
                }
            }

            return total;

        } catch (RuntimeException e) {
            screen.setText("Not arguments in input");
            this.catched = true;
            return -1;
        }
    }

    private double readString(String readable) {
        Scanner readerBy = new Scanner(readable);
        readerBy.useDelimiter("([*/])");
        int lengthString = 0;
        double totalSub = 0;

        if(readerBy.hasNextDouble()) {
            totalSub = readerBy.nextDouble();
        } 

        while (lengthString < readable.length()) {
            char simbol = readable.charAt(lengthString);
            if (simbol == '*' || simbol == '/') {
                if(readerBy.hasNextDouble()) {
                    double read = readerBy.nextDouble();
                    if(simbol == '*') totalSub *= read;
                    else totalSub /= read;
                }
            }
            lengthString ++;
        }
        readerBy.close();
        return totalSub;
    }
}

package datos;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cuadro extends JFrame {
    private JTextField mensajeField;
    private JTextArea chatArea;

    private String mensaje;
    public Cuadro(String nombre) {
        super("Chat de "+nombre);

        // Configuración de la ventana
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creación de componentes
        mensajeField = new JTextField(30);
        JButton enviarButton = new JButton("Enviar");
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        // Layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        panel.add(mensajeField, BorderLayout.SOUTH);
        panel.add(enviarButton, BorderLayout.EAST);

        JPanel enviarPanel = new JPanel();
        enviarPanel.setLayout(new BorderLayout());
        enviarPanel.add(mensajeField, BorderLayout.CENTER);
        enviarPanel.add(enviarButton, BorderLayout.EAST);

        panel.add(enviarPanel, BorderLayout.SOUTH);
        // Agregar panel al frame
        add(panel);

        // Acción del botón de enviar
        enviarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        // Escucha del campo de texto para enviar al presionar Enter
        mensajeField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                enviarMensaje();
            }
        });
    }

    // Método para enviar mensaje
    public String enviarMensaje() {
        String mensaje = mensajeField.getText();
        this.setMensaje(mensaje);
        mensajeField.setText("");
        return mensaje;
    }

    public void recibirMensaje(String mensaje) {
        chatArea.append(mensaje + "\n");
    }


    public String esperarEnter() {
        try {
            // Espera hasta que el usuario presione Enter
            while (true) {
                Thread.sleep(100);
                if (this.getMensaje()!=null && !this.getMensaje().equalsIgnoreCase("")) {
                    String texto = this.getMensaje();
                    this.setMensaje("");
                    mensajeField.setText("");
                    return texto;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}

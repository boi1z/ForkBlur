package JavaBase2;

import javax.swing.JOptionPane;

/**
 *
 * @author cody barr & c.j. meakim
 */
public class JBInputException_V2 extends Exception {

    /**
     * Creates a new instance of <code>JBInputException</code> without detail
     * message.
     */
    private String msg;
    public JBInputException_V2() {
    }

    /**
     * Constructs an instance of <code>JBInputException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public JBInputException_V2(String msg) {
        JOptionPane alert=new JOptionPane();
        JOptionPane.showMessageDialog(alert,msg);
    }
}
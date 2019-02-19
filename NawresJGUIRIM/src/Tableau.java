
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class Tableau extends JPanel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public Tableau( ResultSetTableModel data )
  {
	
    table = new JTable( (TableModel) data );
    table.getPreferredSize();
    setLayout( new BorderLayout() );
    add( new JScrollPane( table ), BorderLayout.CENTER );
    table.setPreferredScrollableViewportSize(table.getPreferredSize());
    table.setFillsViewportHeight(true);
    
  }
  private JTable table;
}
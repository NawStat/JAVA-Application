
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

/**Cette classe est la superclasse de défilement et le modèle de table dee l'ensemble de rèsultats en cache.
 * Elle stocke l'ensemble de rèsultats et ses méta-données.
	 * 
	 */
public class ResultSetTableModel extends AbstractTableModel
{
  /**
   * Construit le modèle de table.
   * @param result l'ensemble de résultats à afficher
   */
	private static final long serialVersionUID = 1L;
  public ResultSetTableModel( ResultSet result )
  {
    resultSet = result;
    
    try 
    {
      this.resultSetMetaData = resultSet.getMetaData();
    } 
    catch (SQLException e1) 
    {
    	System.out.println("Erreur : " +e1.getMessage());
    }
  }

  public int getColumnCount()
  {
	try 
    {
      return resultSetMetaData.getColumnCount();
    } 
    catch (SQLException e2) 
    {
    	System.out.println("Erreur : " +e2.getMessage());
      return 0;
    }
  }

 
  public int getRowCount()
  {
	try
    {
      resultSet.last();
      return resultSet.getRow();
    } 
	catch (SQLException e3)
    {
		System.out.println("Erreur : " +e3.getMessage());
      return 0;
    }
  }

  
  public Object getValueAt(int rowIndex, int columnIndex) 
  {
    try
    {
      resultSet.absolute( rowIndex + 1 );
      return resultSet.getObject(columnIndex + 1 );
    } 
    catch (SQLException e4)
    {
    	System.out.println("Erreur : " +e4.getMessage());
      return null;
    }
  }
  
  
  public String getColumnName( int column )
  {
    try 
    {
      return resultSetMetaData.getColumnName( column + 1 );
    } 
    catch (SQLException e5) 
    {
    	System.out.println("Erreur : " +e5.getMessage());
      return "";
    }
  }
  
  private ResultSet resultSet;
  private ResultSetMetaData resultSetMetaData;

}

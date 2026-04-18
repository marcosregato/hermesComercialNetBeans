import java.sql.*;
import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;

public class debug_usuario {
    public static void main(String[] args) {
        try {
            Connection conn = PostgreSQLConnection.getConnection();
            
            // Verificar estrutura da tabela
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "usuarios", null);
            
            System.out.println("Colunas da tabela usuarios:");
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println(count + ": " + rs.getString("column_name") + " - " + rs.getString("type_name"));
            }
            
            rs.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

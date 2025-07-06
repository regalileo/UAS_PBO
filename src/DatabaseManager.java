import java.sql.CallableStatement;
import java.sql.Connection;        
import java.sql.DriverManager;     
import java.sql.PreparedStatement; 
import java.sql.ResultSet;         
import java.sql.SQLException;      
import java.util.ArrayList;        
import java.util.List;             

public class DatabaseManager {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/toko"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    private Connection connection;

    // Constructor untuk inisialisasi koneksi database
    public DatabaseManager() throws SQLException {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
            throw e; 
        }
    }

    public boolean insertBarang(Barang barang) {
        CallableStatement callableStatement = null; 
        try {  
            String callSQL = "{CALL InsertBarang(?, ?, ?, ?)}";
            callableStatement = connection.prepareCall(callSQL); 

            callableStatement.setString(1, barang.getKode()); 
            callableStatement.setString(2, barang.getNama()); 
            callableStatement.setInt(3, barang.getHarga());   
            callableStatement.setInt(4, barang.getStok());    

            callableStatement.execute(); 
            return true; 
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan data barang: " + e.getMessage());
            System.err.println("Peringatan: Pastikan kode barang unik dan database terhubung.");
            return false; 
        } finally {
            closeStatement(callableStatement);
        }
    }

    public List<Barang> getAllBarang() {
        List<Barang> daftarBarang = new ArrayList<>(); 
        PreparedStatement preparedStatement = null;  
        ResultSet resultSet = null; 

        try {
            String selectSQL = "SELECT kode, nama, harga, stok FROM barang";
            preparedStatement = connection.prepareStatement(selectSQL);
            resultSet = preparedStatement.executeQuery(); 
            while (resultSet.next()) {
                String kode = resultSet.getString("kode");
                String nama = resultSet.getString("nama");
                int harga = resultSet.getInt("harga");
                int stok = resultSet.getInt("stok");

                Barang barang = new Barang(kode, nama, harga, stok);
                daftarBarang.add(barang); 
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data barang: " + e.getMessage());
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
        return daftarBarang;
    }

    public void closeConnection() {
        if (connection != null) { 
            try {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    private void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup PreparedStatement: " + e.getMessage());
            }
        }
    }

    private void closeStatement(CallableStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup CallableStatement: " + e.getMessage());
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup ResultSet: " + e.getMessage());
            }
        }
    }
}
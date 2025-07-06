import java.sql.SQLException; 
import java.util.List;      
import java.util.Scanner;   

public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        DatabaseManager dbManager = null;   

        try {
            dbManager = new DatabaseManager();

            int choice;
            do {
                displayMenu(); 
                choice = getUserChoice(scanner); 

                switch (choice) {
                    case 1:
                        addBarang(dbManager, scanner); 
                        break;
                    case 2:
                        displayAllBarang(dbManager); 
                        break;
                    case 3:
                        System.out.println("Terima kasih! Sampai jumpa.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } while (choice != 3); 

        } catch (SQLException e) {
            System.err.println("Aplikasi tidak dapat berjalan karena masalah koneksi database.");
            System.err.println("Pastikan XAMPP MySQL berjalan dan kredensial database benar.");
            System.err.println("Pesan Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan tak terduga dalam aplikasi: " + e.getMessage());
            e.printStackTrace(); 
        } finally {
            if (dbManager != null) {
                dbManager.closeConnection();
            }
            scanner.close();
        }
    }

    private static void displayMenu() {
        System.out.println("\n Menu Input");
        System.out.println("1. Tambah Data Barang");
        System.out.println("2. Tampilkan Data Barang");
        System.out.println("3. Keluar");
        System.out.print("Pilih opsi: ");
    }

    private static int getUserChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Input tidak valid. Masukkan angka.");
            scanner.next(); 
            System.out.print("Pilih opsi: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void addBarang(DatabaseManager dbManager, Scanner scanner) {
        System.out.println("\n Tambah Data Barang ");
        System.out.print("Masukkan Kode Barang: ");
        String kode = scanner.nextLine();

        System.out.println("DEBUG: kode sebelum dikirim ke DB -> '" + kode + "'");

        
        System.out.print("Masukkan Nama Barang: ");
        String nama = scanner.nextLine();

        int harga = getPositiveIntInput(scanner, "Masukkan Harga Barang: ");
        int stok = getPositiveIntInput(scanner, "Masukkan Stok Barang: ");

        Barang newBarang = new Barang(kode, nama, harga, stok);

        if (dbManager.insertBarang(newBarang)) {
            System.out.println("Data barang berhasil ditambahkan!");
            System.out.println("Trigger 'AfterInsertBarang' juga telah mencatat log.");
        } else {
            System.out.println("Gagal menambahkan data barang. Silakan cek pesan error di atas.");
        }
    }

    private static void displayAllBarang(DatabaseManager dbManager) {
        System.out.println("\n Data Barang ");
        List<Barang> daftarBarang = dbManager.getAllBarang();

        if (daftarBarang.isEmpty()) {
            System.out.println("Tidak ada data barang tersedia.");
            return;
        }

        System.out.printf("%-10s %-20s %-10s %-10s %-15s\n", "KODE", "NAMA", "HARGA", "STOK", "TOTAL NILAI");
        System.out.println("------------------------------------------------------------------");

        for (Barang barang : daftarBarang) {
            long totalNilai = (long) barang.getHarga() * barang.getStok();

            System.out.printf("%-10s %-20s %-10d %-10d %-15d\n",
            barang.getKode(), barang.getNama(), barang.getHarga(),
            barang.getStok(), totalNilai);
        }
    }

    private static int getPositiveIntInput(Scanner scanner, String prompt) {
        int value = -1; 
        while (value < 0) {
            try {
                System.out.print(prompt);
                value = Integer.parseInt(scanner.nextLine()); 
                if (value < 0) {
                    System.out.println("Nilai tidak boleh negatif. Masukkan angka positif.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
            }
        }
        return value;
    }
}
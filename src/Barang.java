public class Barang {
    private String kode;
    private String nama;
    private int harga;
    private int stok;

    // Konstruktor 
    public Barang(String kode, String nama, int harga, int stok) {
        this.kode = kode; // 'this' merujuk ke properti objek, bukan parameter
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // Getter

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }
}
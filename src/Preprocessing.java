
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Amalia Kartika
 */
public class Preprocessing {

    public String[] term = new String[100];
    public int jumlah = 0;
    public String dok[][] = new String[5][1];
    public String tf[][] = new String[5][1];

    public void get_dok(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(file)));
            String line;
            int counter = 0;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                dok[baris][0] = line;
                baris++;
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
    }

    public String tokenizing(String dok) {
        dok = dok.toLowerCase();
        dok = dok.replace(',', ' ');
        dok = dok.replace('(', ' ');
        dok = dok.replace(')', ' ');
        dok = dok.replace('.', ' ');
        String dok_token = "";
        String term[];
        term = dok.split(" ");
        for (int i = 0; i < term.length; i++) {
            dok_token = dok_token + term[i] + " ";
        }
        return dok_token.trim();
    }

    public String filtering(String dok_token) throws IOException {
        String dok_filter = "";
        String[] listStop = new String[1000];
        String term[];
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("stopword.txt")));
            String line;
            int counter = 0;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                String data[] = line.split("\t");
                listStop[baris] = data[0];
                baris++;
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        term = dok_token.split(" ");
        for (int i = 0; i < term.length; i++) {
            boolean cek = false;
            for (int j = 0; j < listStop.length; j++) {
                if (term[i].equals(listStop[j])) {
                    cek = true;
                }
            }
            if (cek == false) {
                dok_filter = dok_filter + term[i] + " ";
            }
        }
        return dok_filter.trim();
    }

    public boolean cek_kata_dasar(String term) {
        boolean hasil = false;
        String[] kata_dasar = new String[1000000];
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("kata-dasar.txt")));
            String line;
            int counter = 0;
            int baris = 0;
            while ((line = br.readLine()) != null) {
                String data[] = line.split(" ");
                kata_dasar[baris] = data[0];
                baris++;
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        for (int j = 0; j < kata_dasar.length; j++) {
            if (term.equals(kata_dasar[j])) {
                hasil = true;
            }
        }

        return hasil;
    }

    public String stemming(String dok_filter) throws IOException {
        String dok_stem = "";
        String term[];
        boolean cek_kd;
        term = dok_filter.split(" ");
        for (int i = 0; i < term.length; i++) {
            term[i] = term[i].trim();
            //hapus partikel
            cek_kd = cek_kata_dasar(term[i]);
            if (cek_kd == true) {
                dok_stem = dok_stem + term[i] + " ";
                continue;
            }
            if (cek_kd == false) {
                if (term[i].length() > 3) {
                    if ((term[i].substring(term[i].length() - 3).equals("kah"))
                            || (term[i].substring(term[i].length() - 3).equals("lah"))
                            || (term[i].substring(term[i].length() - 3).equals("pun"))) {
                        term[i] = term[i].substring(0, term[i].length() - 3);
                    }
                }
            }

            //hapus possesive pronoun
            cek_kd = cek_kata_dasar(term[i]);
            if (cek_kd == false) {
                if (term[i].length() > 4) {
                    if ((term[i].substring(term[i].length() - 2).equals("ku"))
                            || (term[i].substring(term[i].length() - 2).equals("mu"))) {
                        term[i] = term[i].substring(0, term[i].length() - 2);
                    } else if ((term[i].substring(term[i].length() - 3).equals("nya"))) {
                        term[i] = term[i].substring(0, term[i].length() - 3);
                    }
                }
            }

            //hapus first order prekis
            cek_kd = cek_kata_dasar(term[i]);
            if (cek_kd == false) {
                if (term[i].length() > 3) {
                    if (term[i].substring(0, 4).equals("meng")) {
                        if ((term[i].substring(4, 5).equals("e")) || (term[i].substring(4, 5).equals("u"))) {
                            term[i] = "k" + term[i].substring(4);
                            term[i] = term[i].substring(0, term[i].length());
                        } else {
                            term[i] = term[i].substring(4, term[i].length());
                        }
                    } else if (term[i].substring(0, 3).equals("men")) {
                        if ((term[i].substring(3, 4).equals("e"))) {
                            term[i] = "t" + term[i].substring(3);
                            term[i] = term[i].substring(0, term[i].length());
                        } else {
                            term[i] = term[i].substring(4, term[i].length());
                        }
                    } else if (term[i].substring(0, 3).equals("mem")) {
                        if ((term[i].substring(3, 4).equals("a"))) {
                            term[i] = "p" + term[i].substring(3);
                            term[i] = term[i].substring(0, term[i].length());
                        } else {
                            term[i] = term[i].substring(4, term[i].length());
                        }
                    } else if (term[i].substring(0, 4).equals("meny")) {
                        term[i] = "s" + term[i].substring(4);
                        term[i] = term[i].substring(0, term[i].length());
                    } else if (term[i].substring(0, 3).equals("men")) {
                        term[i] = term[i].substring(3, term[i].length());
                    } else if (term[i].substring(0, 3).equals("per")) {
                        term[i] = term[i].substring(3, term[i].length());
                    } else if (term[i].substring(0, 2).equals("di")) {
                        term[i] = term[i].substring(2, term[i].length());
                    } else if (term[i].substring(0, 2).equals("ke")) {
                        term[i] = term[i].substring(2, term[i].length());
                    } else if (term[i].substring(0, 2).equals("se")) {
                        term[i] = term[i].substring(2, term[i].length());
                    } else if (term[i].substring(0, 2).equals("pe")) {
                        term[i] = term[i].substring(2, term[i].length());
                    } else if (term[i].substring(0, 2).equals("me")) {
                        term[i] = term[i].substring(2, term[i].length());
                    } else if (term[i].substring(0, 2).equals("te")) {
                        term[i] = term[i].substring(2, term[i].length());
                    } else if (term[i].substring(0, 3).equals("ter")) {
                        term[i] = term[i].substring(3, term[i].length());
                    }
                }
            }

            //hapussecon order prefiks
            cek_kd = cek_kata_dasar(term[i]);
            if (cek_kd == false) {
                if (term[i].length() > 3) {
                    if (term[i].substring(0, 3).equals("ber")) {
                        term[i] = term[i].substring(3, term[i].length());
                    } else if (term[i].substring(0, 3).equals("bel")) {
                        if (term[i].contains("belaj")) {
                            term[i] = term[i].substring(3, term[i].length());
                        } else if (term[i].contains("belak")) {
                            term[i] = term[i].substring(0);
                        }
                    }
                }
            }

            //hapus suffiks
            cek_kd = cek_kata_dasar(term[i]);
            if (cek_kd == false) {
                if (term[i].length() > 3) {
                    if (term[i].substring(term[i].length() - 3).equals("kan")) {
                        term[i] = term[i].substring(0, term[i].length() - 3);
                    } else if (term[i].substring(term[i].length() - 1).equals("i")) {
                        term[i] = term[i].substring(0, term[i].length() - 1);
                    } else if (term[i].substring(term[i].length() - 2).equals("an")) {
                        term[i] = term[i].substring(0, term[i].length() - 2);
                    }
                }
            }
            dok_stem = dok_stem + term[i] + " ";
        }
        return dok_stem.trim();
    }

    public String[] getTerm() {
        return term;
    }

    public void set_term(String dok_stem) {
        int baris = 0;
        String term[];
        term = dok_stem.split(" ");
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("term.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String data[] = line.split("\t");
                this.term[baris] = data[0];
                baris++;
            }
            jumlah = baris;
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        for (int i = 0; i < term.length; i++) {
            for (int j = 0; j < baris; j++) {
                if (term[i].equals(this.term[j])) {
                    term[i] = "";
                }
            }
            try {
                BufferedWriter fOut = new BufferedWriter(new FileWriter("term.txt"));
                for (int a = 0; a < baris; a++) {
                    fOut.write(this.term[a]);
                    fOut.newLine();
                }
                for (int a = 0; a < term.length; a++) {
                    if (!term[a].equals("")) {
                        fOut.write(term[a]);
                        fOut.newLine();
                        jumlah++;
                    }
                }
                fOut.close();
            } catch (IOException e) {
                System.out.println("Eror");
            }

        }

    }
    public double[][] tf1 = new double[100][100];

    public void set_tf(String dok) {
        double tf_dok[] = new double[jumlah];
        String term[];
        term = dok.split(" ");
        int baris = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("term.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                this.term[baris] = line;
                baris++;
                jumlah = baris;
            }
        } catch (Throwable t) {
            System.out.println(t.getMessage());
        }
        for (int i = 0; i < baris; i++) {
            tf_dok[i] = 0;
            for (int j = 0; j < term.length; j++) {
                if (this.term[i].equals(term[j])) {
                    tf_dok[i]++;
                }
            }
        }

        try {
            BufferedReader fIn = new BufferedReader(new FileReader(new File("tf.txt")));
            int brs = 0;
            int klm = 0;
            String tf[][] = new String[100][100];
            String temp[];
            String line;
            boolean kosong = true;
            while ((line = fIn.readLine()) != null) {
                temp = line.split(" ");
                for (int j = 0; j < temp.length; j++) {
                    tf[brs][j] = temp[j];
                }
                brs++;
                klm = temp.length;
                kosong = false;
            }
            BufferedWriter fOut = new BufferedWriter(new FileWriter(new File("tf.txt")));
            for (int j = 0; j < brs; j++) {
                for (int i = 0; i < klm; i++) {
//                    System.out.print(tf[brs][klm] + " ");
                    fOut.write(tf[j][i] + " ");
                }
                fOut.newLine();
            }
            for (int a = 0; a < baris; a++) {
                String x = tf_dok[a] + " ";
                fOut.write(x);
            }
            fOut.newLine();
            fOut.close();
        } catch (IOException e) {
            System.out.println("Eror");
        }
    }

    public void set_idf() {
        try {
            BufferedReader fIn = new BufferedReader(new FileReader(new File("tf.txt")));
            int brs = 0;
            int klm = 0;
            String tf[][] = new String[100][100];
            String temp[];
            String line;
            while ((line = fIn.readLine()) != null) {
                temp = line.split(" ");
                for (int j = 0; j < temp.length; j++) {
                    tf[brs][j] = temp[j];
                }
                brs++;
                klm = temp.length;
            }
            double df_dok[][] = new double[2][klm];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < klm; j++) {
                    df_dok[i][j] = 0;
                }
            }
            for (int i = 0; i < brs; i++) {
                for (int j = 0; j < klm; j++) {
                    if (Double.parseDouble(tf[i][j]) > 0.0) {
                        df_dok[0][j] += 1;
                    }
                }
            }
            for (int j = 0; j < klm; j++) {
                df_dok[1][j] = Math.log10(brs / df_dok[0][j]);
            }
            BufferedWriter fOut = new BufferedWriter(new FileWriter(new File("idf.txt")));
            for (int j = 0; j < klm; j++) {
                fOut.write(df_dok[0][j] + " ");
            }
            fOut.newLine();
            for (int j = 0; j < klm; j++) {
                fOut.write(df_dok[1][j] + " ");
            }
            fOut.close();
        } catch (IOException e) {
            System.out.println("Eror");
        }
    }

    public void wtf() {
        try {
            BufferedReader fIn = new BufferedReader(new FileReader(new File("tf.txt")));
            int brs = 0;
            int klm = 0;
            String tf[][] = new String[100][100];
            String temp[];
            String line;
            while ((line = fIn.readLine()) != null) {
                temp = line.split(" ");
                for (int j = 0; j < temp.length; j++) {
                    tf[brs][j] = temp[j];
                }
                brs++;
                klm = temp.length;
            }
            double wtf_dok[][] = new double[brs][klm];
            for (int i = 0; i < brs; i++) {
                for (int j = 0; j < klm; j++) {
                    if (Double.parseDouble(tf[i][j]) != 0) {
                        wtf_dok[i][j] = 1 + (Math.log10(Double.parseDouble(tf[i][j])));
                    }
                }
            }
            BufferedWriter fOut = new BufferedWriter(new FileWriter(new File("wtf.txt")));
            for (int j = 0; j < brs; j++) {
                for (int i = 0; i < klm; i++) {
                    fOut.write(wtf_dok[j][i] + " ");
                }
                fOut.newLine();
            }
            //fOut.newLine();
            fOut.close();
        } catch (IOException e) {
            System.out.println("Eror");
        }
    }
    private double jml[][] = new double[100][1];
    double tf_idf[][] = new double[100][100];
    int brs = 0;
    int klm = 0;

    public void tf_idf() {

        String tf[][] = new String[100][100];
        try {
            BufferedReader fIn = new BufferedReader(new FileReader(new File("wtf.txt")));
            String temp[];
            String line;
            while ((line = fIn.readLine()) != null) {
                temp = line.split(" ");
                for (int j = 0; j < temp.length; j++) {
                    tf[brs][j] = temp[j];
                }
                brs++;
                klm = temp.length;
            }
        } catch (IOException e) {
            System.out.println("Eror");
        }
        String idf[][] = new String[2][100];
        try {
            BufferedReader fIn = new BufferedReader(new FileReader(new File("idf.txt")));
            String temp2[];
            String line;
            int baris = 0;
            while ((line = fIn.readLine()) != null) {
                temp2 = line.split(" ");
                for (int j = 0; j < temp2.length; j++) {
                    idf[baris][j] = temp2[j];
                }
                baris++;
            }
        } catch (IOException e) {
            System.out.println("Eror");
        }

        for (int i = 0; i < brs; i++) {
            for (int j = 0; j < klm; j++) {
                tf_idf[i][j] = Double.parseDouble(tf[i][j]) * Double.parseDouble(idf[1][j]);
            }
        }
        try {
            BufferedWriter fOut = new BufferedWriter(new FileWriter("tf-idf.txt"));
            for (int i = 0; i < brs; i++) {
                for (int j = 0; j < klm; j++) {
                    fOut.write(tf_idf[i][j] + " ");
                }
                fOut.newLine();
            }
            fOut.close();
        } catch (IOException e) {
            System.out.println("Eror");
        }
        double xx = 0;
        for (int i = 0; i < brs; i++) {
            for (int j = 0; j < klm; j++) {
                xx = xx + Math.pow(tf_idf[i][j], 2);
            }
            jml[i][0] = Math.sqrt(xx);
        }
    }
    private double norm[][] = new double[100][100];

    public void normalization() {
        for (int i = 0; i < brs; i++) {
            for (int j = 0; j < klm; j++) {
                norm[i][j] = tf_idf[i][j] / jml[i][0];
            }
        }
        try {
            BufferedWriter fOut = new BufferedWriter(new FileWriter("norm.txt"));
            for (int i = 0; i < brs; i++) {
                for (int j = 0; j < klm; j++) {
                    fOut.write(norm[i][j] + " ");
                }
                fOut.newLine();
            }
            fOut.close();
        } catch (IOException e) {
            System.out.println("Eror");
        }
    }

    public void cossim() {
        double[][] cos = new double[100][100];
        String norm2[][] = new String[brs][klm];
        try {
            BufferedReader fIn = new BufferedReader(new FileReader(new File("norm.txt")));
            String temp2[];
            String line;
            int baris = 0;
            while ((line = fIn.readLine()) != null) {
                temp2 = line.split(" ");
                for (int j = 0; j < temp2.length; j++) {
                    norm2[baris][j] = temp2[j];
                }
                baris++;
            }
        } catch (IOException e) {
            System.out.println("Eror");
        }
        int y = 0;
        int x = 0;
        for (int j = 0; j < brs; j++) {
            for (int k = 0; k < klm; k++) {
                cos[x][y] = cos[x][y] + (norm[x][k] * Double.parseDouble(norm2[j][k]));
                System.out.print(cos[x][y] +" ");
                y++;
            }
            System.out.println("");
            y = 0;
            x++;
            j = 0;
        }
     //  System.out.println ("nilai y = " +y);
//        for (int j = 0; j < y; j++) {
//            System.out.print(cos[y] + " ");
//        }
    }

    public void display() {
        String norm2[][] = new String[100][100];
        for (int i = 0; i < brs; i++) {
            for (int j = 0; j < klm; j++) {
                norm2[i][j] = norm[i][j] + "";
            }
        }
        System.out.println("Normalisasi");
        for (int i = 0; i < term.length; i++) {
            System.out.print(term[i] + "\t\t");
            System.out.println("");
        }
        for (int i = 0; i < brs; i++) {
            System.out.print("Dok" + (i + 1) + "\t");
            for (int j = 0; j < klm; j++) {
                if (norm2[i][j].length() < 4) {
                    System.out.print(norm2[i][j] + "\t");
                } else {
                    System.out.print(norm2[i][j].substring(0, 5) + "\t");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        Preprocessing a = new Preprocessing();
        a.get_dok("dokumen.txt");
        for (int i = 0; i < a.dok.length; i++) {
            System.out.println(a.dok[i][0]);
            a.dok[i][0] = a.tokenizing(a.dok[i][0]);
            System.out.println(a.dok[i][0]);
            a.dok[i][0] = a.filtering(a.dok[i][0]);
            System.out.println(a.dok[i][0]);
            a.dok[i][0] = a.stemming(a.dok[i][0]);
            System.out.println(a.dok[i][0]);
            a.set_term(a.dok[i][0]);
            System.out.println();
            a.set_tf(a.dok[i][0]);
        }
        a.wtf();
        a.set_idf();
        a.tf_idf();
        a.normalization();
        a.display();
        a.cossim();
    }
}

package simo.lopez.spatify;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ArtistasController {

    private ObservableList<String> listArtistas = FXCollections.observableArrayList();
    private Map<String, Integer> artistasId = new HashMap<>();

    @FXML
    private ListView<String> listViewArtistas;

    private Connection conn;

    public void cargar(Connection conn) {
        this.conn = conn;
        String query = "SELECT ArtistId, Name FROM artists";
        listArtistas.clear();
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int artistId = rs.getInt("ArtistId");
                String artistName = rs.getString("Name");
                listArtistas.add(artistName);
                artistasId.put(artistName, artistId);
            }
            listViewArtistas.setItems(listArtistas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generar(ActionEvent actionEvent) {
        if (listViewArtistas.getSelectionModel().getSelectedItem() == null) return;
        int id = artistasId.get(listViewArtistas.getSelectionModel().getSelectedItem());

        try {
            String jasperFilePath = "informe_2.jrxml";
            InputStream inputStream = MainApplication.class.getResourceAsStream(jasperFilePath);

            System.out.println("Compilando " + jasperFilePath);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ARTISTA", id);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
